package com.zwh.leetcode.easy

import com.zwh.leetcode.java.ListNode


fun main() {

}

/**
 * Example:
 * var li = ListNode(5)
 * var v = li.`val`
 * Definition for singly-linked list.
 * class ListNode(var `val`: Int) {
 *     var next: ListNode? = null
 * }
 */
class Solution {

    fun addTwoNumbers(l: ListNode?, l2: ListNode?): ListNode? {
        var listNode = ListNode()
        var cur = listNode
        var carry = 0
        var l11 = l
        var l22 = l2

        while (l11 != null || l22 != null) {
            var x = l11?.`val` ?: 0
            var y = l22?.`val` ?: 0

            var sum = x + y + carry
            carry = sum / 10
            sum %= 10

            cur.next = ListNode(sum)
            cur = cur.next

            if (l11 != null) {
                l11 = l11.next
            }
            if (l22 != null) {
                l22 = l22.next
            }
        }
        return listNode
    }
}