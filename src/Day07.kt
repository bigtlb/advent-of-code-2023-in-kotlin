fun main() {
    val ordering = "23456789TJQKA"
    val ordering2 = "J23456789TQKA"

    data class Hand(val cards: CharArray, val bid: Int, val joker: Boolean) : Comparable<Hand> {
        override fun compareTo(other: Hand): Int {
            val a = this.cards.groupBy { it }.map {
                if (it.key == 'J' && joker) 0 else it.value.count()
            }.sortedDescending()
            val aj = if (joker) this.cards.count { it == 'J' } else 0

            val b = other.cards.groupBy { it }.map {
                if (it.key == 'J' && joker) 0 else it.value.count()
            }.sortedDescending()
            val bj = if (joker) other.cards.count { it == 'J' } else 0

            return when {
                a.first() + aj > b.first() + bj -> 1
                b.first() + bj > a.first() + aj -> -1
                else -> {

                    val secondPair = if (a.count()>1 && b.count() >1 && (a[1] > 1 || b[1] > 1)) {
                        a[1].compareTo(b[1])
                    } else 0

                    if (secondPair != 0) {
                        secondPair
                    } else {
                        this.cards.zip(other.cards) { a, b ->
                            val i = (if (joker) ordering2 else ordering).indexOf(a)
                            val j = (if (joker) ordering2 else ordering).indexOf(b)
                            when {
                                i > j -> 1
                                j > i -> -1
                                else -> 0
                            }
                        }.first { it != 0 }
                    }
                }
            }
        }
    }

    fun parseHands(input: List<String>, joker: Boolean = false): List<Hand> = input.map {
        it.split(" ").let { (cards, bid) ->
            Hand(cards.toCharArray(), bid.toInt(), joker)
        }
    }

    fun part1(input: List<String>): Int {
        val hands = parseHands(input).sorted()

        return hands.mapIndexed { idx, hand -> hand.bid * (idx + 1) }.sum()
    }

    fun part2(input: List<String>): Int {
        val hands = parseHands(input, true).sorted()

        return hands.mapIndexed { idx, hand -> hand.bid * (idx + 1) }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)

    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}