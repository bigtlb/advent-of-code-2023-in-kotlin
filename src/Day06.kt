fun main() {
    fun parse1(input: List<String>): List<Pair<Int, Int>> {
        val times = input[0].split(":").last().trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        val distances = input[1].split(":").last().trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        return times.zip(distances)
    }

    fun parse2(input: List<String>): Pair<Long, Long> =
        input[0].split(":").last().replace(" ", "").toLong() to
                input[1].split(":").last().replace(" ", "").toLong()


    fun part1(input: List<String>): Int {

        fun computeWaysToWin(time: Int, distance: Int): Int =
            (0..time).map { it * (time - it) }.count { it > distance }

        val races = parse1(input)
        val result = races.map {
            computeWaysToWin(it.first, it.second)
        }
        return result.filter { it > 0 }.reduce(Int::times)
    }

    fun part2(input: List<String>): Long {
        fun computeWaysToWin2(time: Long, distance: Long):Long {
            var failingFirst = 0L
            var t=0L
            while(t*(time-t) <= distance) {
                failingFirst++
                t++
            }
            var failingLast = 0L
            t=time
            while(t*(time-t)<= distance){
                failingLast++
                t--
            }
            return time+1-failingLast-failingFirst
        }

        val race = parse2(input)
        return computeWaysToWin2(race.first, race.second)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)

    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}