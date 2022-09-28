fun main() {
//    var s: String? = null
//    println("ssss")

    val intArrayOf = intArrayOf(30, 70, 5, 20)
    println(maxProfit(intArrayOf))
}

/**
 * 超出时间限制
 */
fun maxProfit0(prices: IntArray): Int {
    if (prices.size <= 1) return 0
    var max = 0
    for (i in prices.indices) {
        val cur = prices[i]
        for (i1 in 0 until i) {
            val cur1 = prices[i1]
            val range = cur - cur1
            if (range > max) max = range
        }
        for (i2 in i + 1 until prices.size) {
            val cur2 = prices[i2]
            val range = cur2 - cur
            if (range > max) max = range
        }
    }
    return max
}

fun maxProfit(prices: IntArray): Int {
    if (prices.size <= 1) return 0
    var min = prices[0]
    var max = 0
    for (i in 1 until prices.size) {
        max = Math.max(max, prices[i] - min)
        min = Math.min(min, prices[i])
    }
    return max
}


