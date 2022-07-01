package com.zwh.leetcode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int length = new String().length();

    }


    public static void main(String[] args) {
        ListNode1 listNode0 = new ListNode1(2);
        ListNode1 listNode1 = new ListNode1(4);
        ListNode1 listNode2 = new ListNode1(3);
        listNode0.next = listNode1;
        listNode1.next = listNode2;

        ListNode1 listNode3 = new ListNode1(5);
        ListNode1 listNode4 = new ListNode1(6);
        ListNode1 listNode5 = new ListNode1(4);
        listNode3.next = listNode4;
        listNode4.next = listNode5;

        ListNode1 listNode = addTwoNumbers(listNode0, listNode3);
        while (listNode.next != null) {
            System.out.print(listNode.val);
            listNode = listNode.next;
        }
        System.out.println(listNode.val);
    }

    public static ListNode1 addTwoNumbers(ListNode1 l1, ListNode1 l2) {
        //定义一个新联表伪指针，用来指向头指针，返回结果
        ListNode1 prev = new ListNode1(0);
        //定义一个进位数的指针，用来存储当两数之和大于10的时候，
        int carry = 0;
        //定义一个可移动的指针，用来指向存储两个数之和的位置
        ListNode1 cur = prev;
        //当l1 不等于null或l2 不等于空时，就进入循环
        while (l1 != null || l2 != null) { //
            //如果l1 不等于null时，就取他的值，等于null时，就赋值0，保持两个链表具有相同的位数
            int x = l1 != null ? l1.val : 0;
            //如果l2 不等于null时，就取他的值，等于null时，就赋值0，保持两个链表具有相同的位数
            int y = l2 != null ? l2.val : 0;
            //将两个链表的值，进行相加，并加上进位数
            int sum = x + y + carry;
            //计算进位数
            carry = sum / 10;
            //计算两个数的和，此时排除超过10的请况（大于10，取余数）
            sum = sum % 10;
            //将求和数赋值给新链表的节点，
            //注意这个时候不能直接将sum赋值给cur.next = sum。这时候会报，类型不匹配。
            //所以这个时候要创一个新的节点，将值赋予节点
            cur.next = new ListNode1(sum);
            //将新链表的节点后移
            cur = cur.next;
            //当链表l1不等于null的时候，将l1 的节点后移
            if (l1 != null) {
                l1 = l1.next;
            }
            //当链表l2 不等于null的时候，将l2的节点后移
            if (l2 != null) {
                l2 = l2.next;
            }
        }
        //如果最后两个数，相加的时候有进位数的时候，就将进位数，赋予链表的新节点。
        //两数相加最多小于20，所以的的值最大只能时1
        if (carry == 1) {
            cur.next = new ListNode1(carry);
        }
        //返回链表的头节点
        return prev.next;
    }

}