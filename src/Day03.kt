fun main() {
    val symbolRegex = Regex("""[^.0-9]""")
    val partRegex = Regex("""[0-9]+""")

    data class PartNumber(val number: Int, val start: Pair<Int, Int>, val length: Int)

    fun parse(input: List<String>): Triple<Set<Pair<Int, Int>>, List<PartNumber>, Set<Pair<Int, Int>>> {
        val symbolSet = input.flatMapIndexed { row, s -> symbolRegex.findAll(s).map { it.range.first to row } }.toSet()
        val gearSet = input.flatMapIndexed { row, s ->
            symbolRegex.findAll(s).filter { it.value == "*" }.map { it.range.first to row }
        }.toSet()
        val partNumbers = input.flatMapIndexed { row, s ->
            partRegex.findAll(s)
                .map { PartNumber(it.value.toInt(), it.range.first to row, it.range.last - it.range.first + 1) }
        }
        return Triple(symbolSet, partNumbers, gearSet)
    }

    fun part1(input: List<String>): Int {
        fun List<PartNumber>.adjacentTo(symbols: Set<Pair<Int, Int>>): List<PartNumber> = this.filter {
            IntRange(it.start.first - 1, it.start.first + it.length)
                .any { col ->
                    IntRange(it.start.second - 1, it.start.second + 1)
                        .any { row ->
                            symbols.contains(col to row)
                        }
                }
        }

        val (symbolSet, partNumbers) = parse(input)
        return partNumbers.adjacentTo(symbolSet).sumOf { it.number }
    }

    fun part2(input: List<String>): Int {

        fun List<PartNumber>.gearParts(gearSet: Set<Pair<Int, Int>>): List<Pair<PartNumber, PartNumber>> =
            gearSet.mapNotNull { gear ->
                this.filter {
                    (it.start.first - 1..(it.start.first + it.length)).contains(gear.first)
                            && (it.start.second - 1..(it.start.second + 1)).contains(gear.second)
                }.let { parts ->
                    if (parts.count() == 2) {
                        parts.first() to parts.drop(1).first()
                    } else {
                        null
                    }
                }
            }
        val (_, partNumbers, gearSet) = parse(input)

        return partNumbers.gearParts(gearSet).sumOf { it.first.number * it.second.number }
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)

    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}