package com.zwh.leetcode;

public class ListNode1 {
    public int val;
    public ListNode1 next;

    public void setNext(ListNode1 next) {
        this.next = next;
    }

    public ListNode1() {
    }

    public ListNode1(int val) {
        this.val = val;
    }

    public ListNode1(int val, ListNode1 next) {
        this.val = val;
        this.next = next;
    }
}