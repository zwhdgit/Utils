fun main() {
    println(convertToTitle(2147483647))
}
// todo 这叫简单？？？搞不定，以后在来整
fun convertToTitle(columnNumber: Int): String {
    var sb: StringBuffer = StringBuffer()
    val toCharArray = columnNumber.toString().toCharArray()
    for (c in toCharArray) {
        sb.append(intFromAbc(Integer.valueOf(c.toString())))
    }
    return sb.toString()
}

fun intFromAbc(n: Int): String {
    return Char(n + 64) + ""
}
