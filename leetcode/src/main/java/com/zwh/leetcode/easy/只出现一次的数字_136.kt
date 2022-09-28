fun main() {

}

fun singleNumber(nums: IntArray): Int {
    var i = 0;
    for (num in nums) {
        // xor 等同java ^
        i = i xor num
    }
    return i
}