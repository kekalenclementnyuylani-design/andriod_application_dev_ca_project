package exercises

// ─────────────────────────────────────────────
// Exercise 1: Generic Function with Constraints
// ─────────────────────────────────────────────

fun <T : Comparable<T>> maxOf(list: List<T>): T? {
    return list.fold(null as T?) { max, element ->
        if (max == null || element > max) element else max
    }
}

fun main() {
    println(maxOf(listOf(3, 7, 2, 9)))                 // 9
    println(maxOf(listOf("apple", "banana", "kiwi")))  // kiwi
    println(maxOf(emptyList<Int>()))                    // null
}
