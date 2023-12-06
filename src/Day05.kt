fun main() {
    data class Map(val destinationsStart: Long, val sourceStart: Long, val rangeLength: Long) {
        val end = sourceStart + rangeLength - 1
    }

    data class Section(val name: String, val maps: List<Map>)

    fun parse(input: List<String>): Pair<MutableList<Long>, List<Section>> {
        var seeds = mutableListOf<Long>()
        var sections = mutableListOf<Section>()
        var workingOnSeeds = true
        var currentMapList = mutableListOf<Map>()
        var currentSectionName: String = ""
        input.forEach { line ->
            when {

                line.isBlank() -> {
                    if (!workingOnSeeds) {
                        sections.add(Section(currentSectionName, currentMapList))
                        currentSectionName = ""
                        currentMapList = mutableListOf()
                    }
                    workingOnSeeds = false
                }

                workingOnSeeds == true -> {
                    seeds.addAll(line.split(":").last().split(" ").filter { it.isNotBlank() }
                        .map { it.trim().toLong() })
                }

                else -> {
                    if (line.contains(":")) {
                        currentSectionName = line.substring(0, line.indexOf(":"))
                    } else {
                        currentMapList.add(line.split(" ").filter { it.isNotBlank() }.map { it.trim() }
                            .let { (first, second, third) ->
                                Map(first.toLong(), second.toLong(), third.toLong())
                            })
                    }

                }
            }
        }
        if (currentMapList.isNotEmpty()) {
            sections.add(Section(currentSectionName, currentMapList))
        }
        return Pair(seeds, sections)
    }

    data class SeedRange(val start: Long, val end: Long)

    fun part1(input: List<String>): Long {
        val (seeds, sections) = parse(input)

        sections.forEach { section ->
            seeds.replaceAll { seed ->
                section.maps.firstOrNull { (it.sourceStart..<it.rangeLength + it.sourceStart).contains(seed) }
                    ?.let {
                        seed + (it.destinationsStart - it.sourceStart)
                    }
                    ?: seed
            }
        }
        return seeds.min()
    }

    fun part2(input: List<String>): Long {
        val (seeds, sections) = parse(input)

        val realSeeds =
            seeds.windowed(2, 2).map { (start, range) -> SeedRange(start, start + range - 1) }.toMutableList()

        println("Beginnnning seeds: ${realSeeds}")
        sections.forEach { section ->
            val newSeeds = mutableListOf<SeedRange?>()
            val realCopy = mutableListOf(*realSeeds.toTypedArray())
            section.maps.forEach { map ->
                realSeeds.clear()
                realSeeds.addAll(mutableListOf(*realCopy.toTypedArray()))
                realSeeds.map { seed ->
                    if (map.sourceStart <= seed.end && map.end >= seed.start) {
                        realCopy.remove(seed)
                        var offset = map.destinationsStart - map.sourceStart

                        if (seed.start < map.sourceStart) {
                            realCopy.add(SeedRange(seed.start, (map.sourceStart - 1).coerceAtMost(seed.end)))
                        }

                        newSeeds.add(
                            SeedRange(
                                map.sourceStart.coerceAtLeast(seed.start) + offset,
                                map.end.coerceAtMost(seed.end) + offset
                            )
                        )

                        if (map.end < seed.end) {
                            realCopy.add(SeedRange(map.end + 1, seed.end))
                        }
                    }
                }
            }

            newSeeds.addAll(realCopy)

            newSeeds.mapIndexed { index, seedRange ->
                if (index < newSeeds.count() - 1 && seedRange != null) {
                    newSeeds.slice(index + 1..newSeeds.count() - 1).mapIndexed { cdx, testRange ->
                        if (testRange != null && seedRange.start <= testRange.end && seedRange.end >= testRange.start) {
                            newSeeds[index] = SeedRange(
                                seedRange.start.coerceAtMost(testRange.start),
                                seedRange.end.coerceAtLeast(testRange.end)
                            )
                            newSeeds[cdx + index + 1] = null
                        }
                    }
                }
            }
            realSeeds.clear()
            realSeeds.addAll(newSeeds.filterNotNull().sortedBy { it.start })
        }
        return realSeeds.minOf { it.start }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)

    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
