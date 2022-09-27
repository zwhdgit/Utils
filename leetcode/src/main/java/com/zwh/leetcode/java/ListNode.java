package com.zwh.leetcode.java;

public class ListNode {
    public int val;
    public ListNode next;

    public void setNext(ListNode next) {
        this.next = next;
    }

    public ListNode() {
    }

    public ListNode(int val) {
        this.val = val;
    }

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}