import com.zwh.leetcode.ListNode
import com.zwh.leetcode.ListNode1


fun main() {

}

/**
 * Example:
 * var li = ListNode1(5)
 * var v = li.`val`
 * Definition for singly-linked list.
 * class ListNode1(var `val`: Int) {
 *     var next: ListNode1? = null
 * }
 */
class Solution {

    fun addTwoNumbers(l1: ListNode1?, l2: ListNode1?): ListNode1? {
        var listNode = ListNode1()
        var cur = listNode
        var carry = 0
        var l11 = l1
        var l22 = l2

        while (l11 != null || l22 != null) {
            var x = l11?.`val` ?: 0
            var y = l22?.`val` ?: 0

            var sum = x + y + carry
            carry = sum / 10
            sum %= 10

            cur.next = ListNode1(sum)
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