package com.zwh.leetcode.easy;

import com.zwh.leetcode.java.ListNode;

import java.util.ArrayList;
import java.util.HashMap;

public class 环形链表_141 {
    public static void main(String[] args) {
        ListNode listNode = new ListNode();
        listNode.val = 0;
        ListNode listNode1 = new ListNode();
        listNode1.val = 1;
        listNode.next = listNode1;
        System.out.println(hasCycle1(listNode));
    }

    /**
     * 执行用时：419 ms, 在所有 Java 提交中击败了5.75%的用户
     * 内存消耗：42 MB, 在所有 Java 提交中击败了95.07%的用户
     */
    public static boolean hasCycle(ListNode head) {
        if (head == null) return false;
        ArrayList<ListNode> listNodes = new ArrayList<>();
        ListNode next = head;
        while (next != null) {
            if (listNodes.contains(next)) {
                return true;
            } else {
                listNodes.add(next);
            }
            next = next.next;
        }
        return false;
    }


    /**
     * 执行用时：0 ms, 在所有 Java 提交中击败了100.00%的用户
     * 内存消耗：42.8 MB, 在所有 Java 提交中击败了27.60%的用户
     * <p>
     * 快慢指针，闭环终会相遇
     * 动画参考 https://tva1.sinaimg.cn/large/e6c9d24ely1go4tquxo12g20fs0b6u0x.gif
     */
    public static boolean hasCycle1(ListNode head) {
        ListNode m = head;
        ListNode k = head;
        while (m != null) {
            m = m.next;
            if (k == null || k.next == null) {
                return false;
            } else {
                k = k.next.next;
            }
            if (m == k) return true;
        }
        return false;
    }

    /**
     * 执行用时：0 ms, 在所有 Java 提交中击败了100.00%的用户
     * 内存消耗：42.1 MB, 在所有 Java 提交中击败了89.13%的用户
     * <p>
     * 也是快慢指针，但是代码更优雅，内存更少，淦
     */
    public static boolean hasCycle2(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        // 空链表、单节点链表一定不会有环
        while (fast != null && fast.next != null) {
            fast = fast.next.next; // 快指针，一次移动两步
            slow = slow.next;      // 慢指针，一次移动一步
            if (fast == slow) {   // 快慢指针相遇，表明有环
                return true;
            }
        }
        return false; // 正常走到链表末尾，表明没有环
    }
}
