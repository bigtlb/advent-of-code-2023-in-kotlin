fun main() {
    data class InputMap(val moves: String, val nodes: Map<String, Pair<String, String>>) {
        fun moveSequence() = sequence {
            var idx = 0;
            while (true) {
                if (idx >= moves.length) idx = 0;
                yield(moves[idx++])
            }
        }

        fun makeMove(currentLocation: String, move: Char): String =
            nodes[currentLocation]?.let { if (move == 'L') it.first else it.second }
                ?: error("Invalid move ${currentLocation} to $move")

        fun walk(start: String): Int {
            var cur = start
            return moveSequence().indexOfFirst {
                cur = makeMove(cur, it)
                cur.endsWith('Z')
            }.let {
                it + 1
            }
        }

    }

    fun parse(input: List<String>): InputMap = InputMap(
        input.first(),
        input.drop(2).associate {
            it.substring(0..2) to Pair(it.substring(7..9), it.substring(12..14))
        })

    fun part1(input: List<String>): Int = parse(input).let { moveMap ->
        moveMap.walk("AAA")
    }

    fun greatestCommonDenominator(x: Long, y: Long): Long = if (y==0L) x else greatestCommonDenominator(y,x%y)

    fun leastCommonMultiple(x: Long, y: Long): Long = (x * y)/greatestCommonDenominator(x,y)

    fun part2(input: List<String>): Long = parse(input).let { moveMap ->
        var nodes = moveMap.nodes.keys.filter { it.endsWith('A') }
        nodes.fold(1L){
            acc, node ->
            val moves = moveMap.walk(node).toLong()
            leastCommonMultiple(acc, moves)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 2)
    val testInput2 = readInput("Day08_test2")
    check(part1(testInput2) == 6)
    val testInput3 = readInput("Day08_test3")
    check(part2(testInput3) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}