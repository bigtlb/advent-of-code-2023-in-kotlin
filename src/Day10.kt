enum class NodeDirection {
    North, South, East, West
}

fun main() {

    val translation = mapOf(
        '|' to setOf(NodeDirection.North, NodeDirection.South),
        '-' to setOf(NodeDirection.East, NodeDirection.West),
        'L' to setOf(NodeDirection.North, NodeDirection.East),
        'J' to setOf(NodeDirection.North, NodeDirection.West),
        '7' to setOf(NodeDirection.West, NodeDirection.South),
        'F' to setOf(NodeDirection.East, NodeDirection.South),
        '.' to emptySet(),
        'S' to emptySet()
    )

    data class Node(
        val loc: Pair<Int, Int>,
        var code: Char,
        val isStart: Boolean = code == 'S',
        var directions: Set<NodeDirection> = translation[code] ?: emptySet()
    ) {
        var connections: Set<Node> = emptySet()
    }

    fun parse(input: List<String>): Map<Pair<Int, Int>, Node> =
        input.flatMapIndexed { row, line -> line.mapIndexed { col, char -> Node(col to row, char) } }
            .associateBy { it.loc }
            .apply {
                // Fix start
                entries.find { it.value.isStart }?.let { (loc, value) ->
                    val firstRange = (loc.first - 1..loc.first + 1)
                    val secondRange = (loc.second - 1..loc.second + 1)
                    value.directions =
                        entries.filter { firstRange.contains(it.key.first) && secondRange.contains(it.key.second) }
                            .mapNotNull { (testLoc, node) ->
                                when (testLoc) {
                                    (loc.first - 1) to loc.second -> if (node.directions.contains(NodeDirection.East)) NodeDirection.West else null
                                    (loc.first + 1) to loc.second -> if (node.directions.contains(NodeDirection.West)) NodeDirection.East else null
                                    loc.first to (loc.second - 1) -> if (node.directions.contains(NodeDirection.South)) NodeDirection.North else null
                                    loc.first to (loc.second + 1) -> if (node.directions.contains(NodeDirection.North)) NodeDirection.South else null
                                    else -> null
                                }
                            }.toSet()
                    value.code = translation.entries.find { it.value == value.directions }?.key
                        ?: error("Didn't find Start code")
                }
                //Connect everything
                entries.forEach { (loc, node) ->
                    node.directions.forEach { direction ->
                        when (direction) {
                            NodeDirection.North -> this[loc.first to (loc.second - 1)]?.let { node.connections += it }
                            NodeDirection.South -> this[loc.first to (loc.second + 1)]?.let { node.connections += it }
                            NodeDirection.East -> this[(loc.first + 1) to loc.second]?.let { node.connections += it }
                            NodeDirection.West -> this[(loc.first - 1) to loc.second]?.let { node.connections += it }
                        }
                    }
                }
            }

    fun walkPath(start: Node): List<Node> {
        var cur = start
        var prev = start
        val list = mutableListOf(start)
        do {
            val next = cur.connections.first { it != prev }
            prev = cur
            cur = next
            list.add(cur)
        } while (cur != start)
        return list.dropLast(1)
    }

    fun findInsideTiles(
        tubes: Map<Pair<Int, Int>, Node>,
        nodes: List<Node>
    ): MutableList<Node> {
        val insideTiles = mutableListOf<Node>()
        (0..tubes.maxOf { it.key.second }).forEach { row ->
            var lastCode: Char? = null
            var inside: Boolean = false
            (0..tubes.maxOf { it.key.first }).forEach { col ->
                val checkNode = nodes.find { it.loc == col to row }
                if (checkNode != null) {
                    if (checkNode.code != '-') {
                        val curCode = if (listOf('L', 'J', '7', 'F').contains(checkNode.code)) checkNode.code else null
                        when {
                            lastCode == 'F' && curCode == 'J' -> Unit
                            lastCode == 'L' && curCode == '7' -> Unit
                            else -> inside = !inside
                        }
                        lastCode = curCode
                    }
                } else {
                    if (inside) {
                        insideTiles.add(tubes[col to row] ?: error("Node ${col to row}"))
                    }
                }
            }
        }
        return insideTiles
    }

    fun part1(input: List<String>): Int {
        val tubes = parse(input)
        val nodes = walkPath(tubes.values.find { it.isStart } ?: error("Can't find start"))
        return nodes.count().div(2)
    }

    fun part2(input: List<String>): Int {
        val tubes = parse(input)
        val nodes = walkPath(tubes.values.find { it.isStart } ?: error("Can't find start"))
        val insideTiles = findInsideTiles(tubes, nodes)
//        println()
//        (0..tubes.maxOf{it.key.second}).forEach{row ->
//            (0..tubes.maxOf{it.key.first}).forEach{col->
//                val path = nodes.find{it.loc == col to row}
//                if (path != null) print(path.code) else print(".")
//            }
//            println()
//        }
        return insideTiles.count()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 8)
    val testInput2 = readInput("Day10_test2")
    check(part2(testInput2) == 8)
    val testInput3 = readInput("Day10_test3")
    check(part2(testInput3) == 10)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}