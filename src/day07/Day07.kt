package day07

import Solution
import Solutions
import input

fun solve(): Solutions<Int> {
    val day = "07"
    val input = input(day)
    return Solutions(
        day,
        Solution(part1(input), 1908462),
        Solution(part2(input), 3979145)
    )
}

private fun part1(input: List<String>): Int =
    dirSizes(input)
        .map { it.value }
        .filter { it <= 100_000 }
        .sum()

private fun part2(input: List<String>): Int {
    val dirSizes = dirSizes(input)
    val used = dirSizes["/"] ?: 0
    val unused = 70_000_000 - used
    val overflow = 30_000_000 - unused
    return dirSizes
        .map { it.value }
        .filter { it >= overflow }
        .minOf { it }
}

private fun dirSizes(input: List<String>): Map<String, Int> {
    val result = input.fold(FileState()) { state, command ->
        when {
            command.startsWith("$ cd ..") -> state.copy(path = state.path.dropLast(1))
            command.startsWith("$ cd") -> state.copy(path = state.path + command.substringAfter("$ cd "))
            command[0].isDigit() -> {
                val size = command.substringBefore(" ").toInt()
                val newSizes = state.sizes.toMutableMap()
                state.path.indices.forEach { index ->
                    val currentPath = state.subPath(index)
                    newSizes[currentPath] = (newSizes[currentPath] ?: 0) + size
                }
                state.copy(sizes = newSizes)
            }

            else -> state
        }
    }
    return result.sizes.toMap()
}

private data class FileState(val path: List<String> = emptyList(), val sizes: Map<String, Int> = emptyMap()) {
    fun subPath(index: Int): String = path.subList(0, index + 1).joinToString("/")
}
