fun main() {
    val input = input("Day06")[0].toCharArray().toList()
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<Char>): Int = minSizeContainingMarker(input, 4)

private fun part2(input: List<Char>): Int = minSizeContainingMarker(input, 14)

private fun minSizeContainingMarker(input: List<Char>, markerSize: Int) =
    (markerSize..input.size)
        .map { input.subList(it - markerSize, it).toSet().size }
        .indexOfFirst { it == markerSize } + markerSize
