package com.zwh.leetcode.easy;

import com.zwh.leetcode.java.ListNode;

import java.util.ArrayList;

public class 相交链表_160 {
    public static void main(String[] args) {
        ListNode listNode = new ListNode(1);
        ListNode listNode1 = new ListNode(1);
        listNode.next = listNode;
        listNode1.next = listNode1;
        System.out.println(getIntersectionNode(listNode, listNode).val);
    }

    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode nextA = headA.next;
        ListNode nextB = headB.next;
        ArrayList<ListNode> nodes = new ArrayList<>();
        while (nextA != null || nextB != null) {
            if (nodes.contains(nextA)) {
                return nextA;
            } else {
                nodes.add(nextA);
            }
            if (nodes.contains(nextB)) {
                return nextB;
            } else {
                nodes.add(nextB);
            }
            nextA = nextA != null ? nextA.next : null;
            nextB = nextB != null ? nextB.next : null;
        }
        return null;
    }
}
