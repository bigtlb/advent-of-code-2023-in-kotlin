import kotlin.math.absoluteValue

fun main() {

    fun parseGalaxies(input: List<String>): Set<Pair<Long, Long>> = input.flatMapIndexed { row, line ->
        line.mapIndexed { col, c ->
            if (c == '#') col.toLong() to row.toLong() else null
        }.filterNotNull()
    }.toSet()

    fun Iterable<Pair<Long, Long>>.inflateGalaxy(factor: Long = 2L): Set<Pair<Long, Long>> {
        var col = 0L
        var tempGalaxy = this.toMutableSet()
        var maxCol = this.maxOf { it.first }
        var maxRow = this.maxOf { it.second }
        while (col <= maxCol) {
            if (tempGalaxy.none { it.first == col }) {
                tempGalaxy =
                    tempGalaxy.map { if (it.first > col) it.first + factor-1L to it.second else it }.toMutableSet()
                maxCol += factor-1L
                col += factor-1L
            }
            col++
        }
        var row = 0L
        while (row <= maxRow) {
            if (tempGalaxy.none { it.second == row }) {
                tempGalaxy =
                    tempGalaxy.map { if (it.second > row) it.first to it.second + factor-1L else it }.toMutableSet()
                maxRow += factor-1L
                row += factor-1L
            }
            row++
        }
        return tempGalaxy
    }

    fun Pair<Long, Long>.distanceTo(other: Pair<Long, Long>): Long {
        return (other.first - this.first).absoluteValue + (other.second - this.second).absoluteValue
    }

    fun part1(input: List<String>): Long =
        parseGalaxies(input).inflateGalaxy().combinations(2).map { (start, end) ->
            start.distanceTo(end)
        }.sum()

    fun part2(input: List<String>, factor: Long): Long =
        parseGalaxies(input).inflateGalaxy(factor).combinations(2).map { (start, end) ->
            start.distanceTo(end)
        }.sum()


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374L)
    check(part2(testInput, 10) == 1030L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input, 1000000).println()
}