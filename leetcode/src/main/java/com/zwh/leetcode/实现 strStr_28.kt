fun main() {

}

/**
 * 这题真的是送分题
 *  96.10%
 *  5.19%
 */
fun strStr(haystack: String, needle: String): Int {
    if (needle.equals("")) return 0
    val replace = haystack.replace(needle, ",")
    if (replace.equals(haystack)) return -1
    return replace.split(",")[0].length
}
