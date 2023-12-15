fun main() {
    data class SpringRow(val states: String, val badList: List<Int>) {

        val cache = mutableMapOf<Pair<String, List<Int>>, Long>()
        fun validArrangements(segment: String, badList: List<Int>): Long {
            if (segment.isEmpty())
                return if (badList.isEmpty()) 1L else 0L

            if (badList.isEmpty())
                return if (segment.any { it == '#' }) 0L else 1L

            var result = 0L
            val key = segment to badList
            if (cache.containsKey(key)) return cache.getValue(key)

            if (".?".contains(segment.first()))
                result += validArrangements(segment.drop(1), badList)

            if ("#?".contains(segment.first())) {
                if (badList.first() <= segment.length
                    && segment.take(badList.first()).none { it == '.' }
                    && (segment.length == badList.first() || segment[badList.first()] != '#')
                ) {
                    result += validArrangements(segment.drop(badList.first() + 1), badList.drop(1))
                }
            }

            cache[key] = result
            return result
        }

    }

    fun part1(input: List<String>): Long {

        fun parse(input: List<String>): List<SpringRow> {
            return input.map { line ->
                val parts = line.split(' ', ',')
                SpringRow(parts[0], parts.drop(1).map { it.toInt() })
            }
        }

        return parse(input).sumOf {
            it.validArrangements(it.states, it.badList)
        }
    }

    fun part2(input: List<String>): Long {

        fun parse(input: List<String>): List<SpringRow> {
            return input.map { line ->
                val parts = line.split(' ', ',')
                val result = SpringRow(
                    (0..4).joinToString("?") { parts[0] },
                    (0..4).flatMap { parts.drop(1).map { it.toInt() } })
                result
            }
        }
        return parse(input).sumOf {
            it.validArrangements(it.states, it.badList)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 21L)
    check(part2(testInput) == 525152L)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}