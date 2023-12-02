enum class Color { Red, Green, Blue }

fun main() {
    data class Draw(val dice: Map<Color, Int>)
    data class Game(val number: Int, val draws: List<Draw>)

    fun parseGame(str: String): Game {
        val parts = str.split(":", ";").map { it.trim() }

        fun parseDraw(draw: String): Draw {
            val pieces = draw.split(",", " ").filter { it.isNotBlank() }
            return Draw(
                pieces.windowed(2, 2)
                    .associate { Color.entries.first { e -> e.name.equals(it[1], true) } to it[0].toInt() })
        }

        return Game(parts[0].substring(5).toInt(), parts.drop(1).map { parseDraw(it) }.toList())
    }


    fun part1(input: List<String>): Int {

        fun possibleGame(game: Game, limit: Map<Color, Int>): Boolean =
            game.draws.all { it.dice.all { limit.getOrDefault(it.key, 0) >= it.value } }

        val games = input.map { parseGame(it) }

        val limit = mapOf(Color.Red to 12, Color.Green to 13, Color.Blue to 14)

        return games.filter { possibleGame(it, limit) }.sumOf { it.number }
    }

    fun part2(input: List<String>): Int {

        fun gamePower(game: Game): Int {
            val minSet = mutableMapOf<Color, Int>()
            game.draws.forEach {
                it.dice.forEach { result ->
                    minSet.compute(result.key) { _, v ->
                        maxOf(v ?: 0, result.value)
                    }
                }
            }

            return minSet.values.fold(1) { acc, it -> acc * it }
        }

        val games = input.map { parseGame(it) }

        return games.sumOf { gamePower(it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)

    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}