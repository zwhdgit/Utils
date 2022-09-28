package com.zwh.leetcode

fun main() {
//    val s = "A man, a plan, a canal: Panama"
    println(isPalindrome("ab_a"))
//    println(".".length)
}

fun isPalindrome(s: String): Boolean {
//    val replace = s.replace("\\w{*}", "")
        val replace = s.replace("[^a-zA-Z0-9]".toRegex(), "").toLowerCase()
    if (replace=="")return true
    val indices = replace.length - 1
    for (i in 0..replace.length / 2) {
        val h = replace[i]
        val l = replace[indices - i]
        if (h != l) return false
    }
    return true
}
