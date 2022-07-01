package com.zwh.leetcode

fun main() {
    var arr = arrayOf("at", "", "", "", "ball", "", "", "car", "", "", "dad", "", "")
    var s = "ta"
    println(稀疏数组().findString(arr, s))
}

class 稀疏数组 {
    /**
     * 稀疏数组搜索。有个排好序的字符串数组，其中散布着一些空字符串，编写一种方法，找出给定字符串的位置。
     *  如何排除空字符串？
     *
     */
    fun findString(words: Array<String>, s: String): Int {
        val size = words.size
        var left = 0
        var right = size - 1
        var mid = 0

        while (left <= right) {
            mid = (left + right) / 2

            while (words[mid].equals("") && mid > 1) mid--
            val s1 = words[mid]
            if (s1.compareTo(s) > 0) {
                right = mid - 1
            } else if (s1.compareTo(s) < 0) {
                left = mid + 1
            } else {

                return mid
            }
        }
        return -1
    }
}