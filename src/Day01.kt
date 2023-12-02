fun main() {
    fun part1(input: List<String>): Int {
        return input
            .map { s -> s.first(Char::isDigit).toString() + s.last(Char::isDigit) }
            .map(String::toInt).sum()
    }

    val numbers = listOf(
        "zero",
        "one",
        "two",
        "three",
        "four",
        "five",
        "six",
        "seven",
        "eight",
        "nine"
    )

    fun part2(input: List<String>): Int {

        fun checkDigit(str: String): Int? {
            val word = numbers.firstOrNull{str.startsWith(it)}
            return when {
                str[0].isDigit() -> str[0].digitToInt()
                word != null -> numbers.indexOf(word)
                else -> null
            }
        }

        fun List<Int>.collect(): Int = this.first() * 10 + this.last()

        return input.sumOf {it.indices.mapNotNull { n -> checkDigit(it.substring(n)) }.collect()}
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
