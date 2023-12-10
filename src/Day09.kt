fun main() {
    fun history(readings: String): MutableList<MutableList<Int>> =
        readings.split(' ').map { it.trim() }.filter { it.isNotBlank() }.map { it.toInt() }.let { start ->
            val history = mutableListOf<MutableList<Int>>(start.toMutableList())
            do {
                history.add(history.last().windowed(2, 1).map { (first, second) -> second - first }.toMutableList())
            } while (history.last().any { it != 0 })
            history
        }

    fun MutableList<MutableList<Int>>.addNextPrediction(): MutableList<MutableList<Int>> {
        this.last().add(0)
        ((this.count() - 2) downTo 0).map { this[it].add(this[it].last() + this[it + 1].last()) }
        return this
    }


    fun MutableList<MutableList<Int>>.addPreviousPrediction(): MutableList<MutableList<Int>> {
        this.last().add(0, 0)
        ((this.count() - 2) downTo 0).map { this[it].add(0, this[it].first() - this[it + 1].first()) }
        return this
    }

    fun part1(input: List<String>): Int = input.sumOf { history(it).addNextPrediction()[0].last() }

    fun part2(input: List<String>): Int = input.sumOf { history(it).addPreviousPrediction()[0].first() }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}