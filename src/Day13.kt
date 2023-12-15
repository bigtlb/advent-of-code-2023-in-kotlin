fun main() {
    //Pair<Col,Row>
    fun parse(input: List<String>): List<Set<Pair<Int, Int>>> {
        var group = 0
        return input.groupBy { line ->
            if (line.isBlank()) group++
            group
        }.map { entry -> entry.value.filter { it.isNotBlank() } }
            .map {
                it.flatMapIndexed { row, line ->
                    line.mapIndexed { col, c ->
                        if (c == '#') col to row else null
                    }
                }.filterNotNull().toSet()
            }
    }

    fun Set<Pair<Int, Int>>.verticalScore(skipCol: Int = -1): Int {

        fun reflectionColumn(set: Set<Pair<Int, Int>>, bounds: Pair<Int, Int>, col: Int): Boolean =
            (0..Math.min(col, bounds.first - col - 1)).all { cdx ->
                (0..bounds.second).all { rdx ->
                    set.contains(col - cdx to rdx) == set.contains(col + cdx + 1 to rdx)
                }
            }

        val bounds = this.bounds
        val col = (0..<bounds.first).firstOrNull { col ->
            skipCol-1 != col && reflectionColumn(this, bounds, col)
        }
        return if (col == null) 0 else col + 1
    }

    fun Set<Pair<Int, Int>>.horizontalScore(skipRow:Int=-1): Int {

        fun reflectionRow(set: Set<Pair<Int, Int>>, bounds: Pair<Int, Int>, row: Int): Boolean =
            (0..Math.min(row, bounds.second - row - 1)).all { rdx ->
                (0..bounds.first).all { cdx ->
                    set.contains(cdx to row - rdx) == set.contains(cdx to row + rdx + 1)
                }
            }

        val bounds = this.bounds
        val row = (0..<bounds.second).firstOrNull { row ->
            skipRow-1 != row && reflectionRow(this, bounds, row)
        }
        return if (row == null) 0 else row + 1
    }

    // #, Loc Pair, Score Pair
    fun Set<Pair<Int, Int>>.fixSmudge(
        verticalScore: Int,
        horizontalScore: Int
    ): Triple<Boolean, Pair<Int, Int>, Pair<Int, Int>> {
        val bounds = this.bounds
        return (0..bounds.first).asSequence().flatMap { col ->
            (0..bounds.second).asSequence().map { row ->
                val test = col to row
                val addPound = !this.contains(test)
                val testSet = if (addPound) this + test else this - test
                Triple(addPound, test, testSet.verticalScore(verticalScore) to testSet.horizontalScore(horizontalScore))
            }
        }.first { (_, _, score) ->
            (score.first + score.second > 0) && ((score.first != verticalScore) || (score.second != horizontalScore))
        }
    }


    fun part1(input: List<String>): Int =
        parse(input).sumOf {
            it.verticalScore() + it.horizontalScore() * 100
        }

    fun part2(input: List<String>): Int =
        parse(input).sumOf {
            it.fixSmudge(it.verticalScore(), it.horizontalScore()).let { (pound, loc, score) ->
                score.let { (vert, hor) ->
                    //println("$pound $loc $vert $hor  ${vert + hor * 100}")
                    vert + hor * 100
                }
            }
        }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 405)
    val testInput2 = readInput("Day13_test2")
    check(part1(testInput2) == 4)
    check(part2(testInput) == 400)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}



