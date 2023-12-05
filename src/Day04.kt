fun main() {
    fun parse(str: String): Pair<Set<Int>, Set<Int>> = str.split(":", "|").drop(1).let { (winners, card) ->
        winners.split(" ").map { it.trim() }.filter { it.isNotBlank() }.map { it.toInt() }.toSet() to
                card.split(" ").map { it.trim() }.filter { it.isNotBlank() }.map { it.toInt() }.toSet()
    }

    tailrec fun score(count: Int, accum: Int = 1): Int =
        when (count) {
            0 -> 0
            1 -> accum
            else ->
                score(count - 1, accum * 2)
        }

    fun part1(input: List<String>): Int =
        input.sumOf {
            val (winners, cardNumbers) = parse(it)
            val matches = winners.intersect(cardNumbers).count()
            score(matches)
        }


    fun part2(input: List<String>): Int {
        val cards = input.mapIndexed{
            idx, it ->
            val (winners, cardNumbers) = parse(it)
            val matches = winners.intersect(cardNumbers).count()
            Triple(idx, matches, mutableListOf(idx))
        }
        (cards.count()-1 downTo 0).forEach {
            val count = cards[it].second
            if (count > 0) {
                (it + 1..it + count).map { cdx ->
                    cards[it].third.addAll(cards[cdx].third)
                }
            }
        }

        return cards.sumOf{it.third.count()}
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)

    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}