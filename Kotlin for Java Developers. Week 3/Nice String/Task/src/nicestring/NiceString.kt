package nicestring

fun String.isNice(): Boolean {
    val condition1 = condition1(this)

    val condition2 = condition2(this)

    if (condition1 && condition2)
        return true

    if (!condition1 && !condition2)
        return false

    val condition3 = condition3(this)

    return condition3
    TODO()
}

fun condition1(s: String): Boolean {
    return !s.contains("bu")
            && !s.contains("ba")
            && !s.contains("be")
}

fun condition2(s: String): Boolean {
    val vowels = listOf('a', 'e', 'i', 'o', 'u')

    val vowelsCount = s.count { ch: Char -> ch in vowels }

    return vowelsCount >= 3
}

fun condition3(s: String): Boolean {
    for ((index, c) in s.withIndex()) {
        if (
            index < s.length - 1
            && c == s[index + 1]
        ) {
            return true
        }
    }

    return false
}