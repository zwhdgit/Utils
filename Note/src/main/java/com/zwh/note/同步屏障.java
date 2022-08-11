package com.zwh.note;

import android.os.Binder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import com.zwh.test.SyncBarrierTestActivity;

/**
 * 同步屏障
 *     顾名思义，拦截同步消息，提高异步消息的优先级。
 *     test 移步
 *     @see SyncBarrierTestActivity
 *     源码分析 Api 32
 */
public class 同步屏障 {



    /**
     * @1、创建同步屏障
     * <p>
     * UnsupportedAppUsage 修饰，所以只能反射使用：
     * MessageQueue queue = handler.getLooper().getQueue();
     * Method method = MessageQueue.class.getDeclaredMethod("postSyncBarrier");
     * method.setAccessible(true);
     * token = (int) method.invoke(queue);
     */
    @UnsupportedAppUsage
    @TestApi
    public int postSyncBarrier() {
        return postSyncBarrier(SystemClock.uptimeMillis());
    }

    private int postSyncBarrier(long when) {
        // Enqueue a new sync barrier token.
        // We don't need to wake the queue because the purpose of a barrier is to stall it.
        synchronized (this) {
            final int token = mNextBarrierToken++;
            /** 注意此处，没有target赋值，也就是没有对应的handler处理，这就是它和其他消息的区别
             * 它的插入逻辑和普通消息的插入是一样，但是这个插入不会唤醒 block 住的 poll 循环。
             * 所以，从代码层面上来讲，同步屏障就是一个Message，一个target字段为空的Message。
             */
            final Message msg = Message.obtain();
            msg.markInUse();
            msg.when = when;
            msg.arg1 = t;
//                    *******   此处省略代码    ************
            return token;
        }
    }

    /**
     * @2、MessageQueue next()相关处理代码
     */
    @UnsupportedAppUsage
    Message next() {
        //                    *******   此处省略代码    ************

        for (; ; ) {
            if (nextPollTimeoutMillis != 0) {
                Binder.flushPendingCommands();
            }

            nativePollOnce(ptr, nextPollTimeoutMillis);

            synchronized (this) {
                // Try to retrieve the next message.  Return if found.
                final long now = SystemClock.uptimeMillis();
                Message prevMsg = null;
                Message msg = mMessages;
                /**
                 * if (msg != null && msg.target == null) 对应的就是屏障消息
                 */
                if (msg != null && msg.target == null) {
                    // Stalled by a barrier.  Find the next asynchronous message in the queue.
                    do {
                        prevMsg = msg;
                        msg = msg.next;
                        /**
                         * !msg.isAsynchronous() 如果是同步消息那么一直循环直到next==null或者是异步消息（Asynchronous = true）
                         */
                    } while (msg != null && !msg.isAsynchronous());
                }
                if (msg != null) {
                    if (now < msg.when) {
                        // Next message is not ready.  Set a timeout to wake up when it is ready.
                        nextPollTimeoutMillis = (int) Math.min(msg.when - now, Integer.MAX_VALUE);
                    } else {
                        // Got a message.
                        mBlocked = false;
                        /**
                         * prevMsg！=null 表示有同步屏障， mMessages（同步屏障）一直不更新复制，
                         * 那么同步消息一直不会处理，所以使用玩需要手动删除同步屏障
                         */
                        if (prevMsg != null) {
                            /**
                             * 异步消息插队了，那么把链表连起来
                             *  1-》2-》3
                             *  1-》= 2-》= 3
                             *  1-》3
                             */
                            prevMsg.next = msg.next;
                        } else {
                            mMessages = msg.next;
                        }
                        msg.next = null;
                        if (DEBUG) Log.v(TAG, "Returning message: " + msg);
                        msg.markInUse();
                        return msg;
                    }
                } else {
                    // No more messages.
                    nextPollTimeoutMillis = -1;
                }
//                    *******   此处省略代码    ************

            }
        }
    }
}
