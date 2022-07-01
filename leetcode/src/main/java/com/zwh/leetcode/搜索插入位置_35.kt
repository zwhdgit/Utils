fun main() {
    val nums = intArrayOf(1, 3, 5, 6)
    println(searchInsert(nums, 6))
}

/**
 * todo 二分查找，有时间重写一下
 */
fun searchInsert(nums: IntArray, target: Int): Int {
    val len: Int = nums.size
    var left = 0
    var right = len - 1 // 定义target在左闭右闭的区间里，[left, right]

    while (left <= right) { // 当left==right，区间[left, right]依然有效
        val middle = left + (right - left) / 2 // 防止溢出 等同于(left + right)/2
//        val middle = (right - left) ushr 1
        val mid = nums[middle]
        if (mid > target) {
            right = middle - 1 // target 在左区间，所以[left, middle - 1]
        } else if (mid < target) {
            left = middle + 1 // target 在右区间，所以[middle + 1, right]
        } else { // nums[middle] == target
            return middle
        }
    }
    // 分别处理如下四种情况
    // 目标值在数组所有元素之前  [0, -1] left=0,right=-1,返回right+1
    // 目标值等于数组中某一个元素  return middle;
    // 目标值插入数组中的位置 [left, right]，return  right + 1
    // 目标值在数组所有元素之后的情况 [left, right]， return right + 1
    return right + 1 // tod 此处直接反 left 也可以
}


fun searchInsert1(nums: IntArray, target: Int): Int {
    var left = 0
    var right = nums.size - 1
    var mid: Int
    while (left <= right) {
        mid = (left + right) / 2
        if (nums[mid] > target) {
            right = mid - 1
        } else if (nums[mid] < target) {
            left = mid + 1
        } else {
            return mid
        }
    }
    return right + 1

}