package com.zwh.leetcode

import java.util.*

/**
 * 给定一个只包括 '('，')'，'{'，'}'，'['，']' 的字符串 s ，判断字符串是否有效。

有效字符串需满足：

左括号必须用相同类型的右括号闭合。
左括号必须以正确的顺序闭合。
 */

fun main() {
    var s = "([()])[]{}"
    print(isValid1(s))
}

/***
 * 最低粒度的有效括号排除法  用时9分钟
 *
 * 执行用时：264 ms,
 *      在所有 Kotlin 提交中击败了9.00%的用户内存消耗：38.6 MB,
 *      在所有 Kotlin 提交中击败了5.00%的用户
 */

fun isValid(s: String): Boolean {
//    '('，')'，'{'，'}'，'['，']'
    if (s.length and 1 != 0) {
        return false
    }

    val replace = s.replace("()", "")
        .replace("{}", "")
        .replace("[]", "")
    if (replace.equals(s)) {
        if (replace.length == 0) {
            return true
        }
        return false
    }
    return isValid(replace)
}

/**
 *  遍历字符串添加，当匹配到 闭括号时，那么此时栈顶一定是开括号，
 *  其实原理与
 *  @see isValid() 差不离，但是效率差很多，估计是 String.replace()的问题
 *
 *
 *  执行用时：164 ms,
 *      在所有 Kotlin 提交中击败了40.00%的用户内存消耗：32.8 MB,
 *      在所有 Kotlin 提交中击败了70.50%的用户
 */

fun isValid1(s: String): Boolean {
    if (s.length and 1 != 0) return false

    val map = hashMapOf<Char, Char>().apply {
        put(')', '(')
        put('}', '{')
        put(']', '[')
    }
    val stack = Stack<Char>()
    for (c in s) {
        if (map.containsKey(c)) {
            if (stack.isEmpty() || stack.pop() != map.get(c)) {
                return false
            }
        } else {
            stack.push(c)
        }
    }
    return stack.isEmpty()
}