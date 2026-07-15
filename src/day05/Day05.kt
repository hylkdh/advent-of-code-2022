package day05

import Solution
import Solutions
import input

fun solve(): Solutions<String> {
    val day = "05"
    val input = input(day)
    return Solutions(
        day,
        Solution(part1(input), "ZWHVFWQWW"),
        Solution(part2(input), "HZFZCCWWV")
    )
}

private fun part1(input: List<String>): String {
    val separator = input.indexOf("")
    return parseInstructions(input, separator)
        .fold(parseStacks(input, separator)) { currentStacks, instruction ->
            move1(currentStacks, instruction[0], instruction[1], instruction[2])
        }
        .let(::topCrates)
}

private fun part2(input: List<String>): String {
    val separator = input.indexOf("")
    return parseInstructions(input, separator)
        .fold(parseStacks(input, separator)) { currentStacks, instruction ->
            move2(currentStacks, instruction[0], instruction[1], instruction[2])
        }
        .let(::topCrates)
}

private fun move1(stacks: Matrix<Char>, moves: Int, from: Int, to: Int): Matrix<Char> {
    val result = stacks.toMutableList()
    repeat(moves) {
        val fromStack = result[from - 1]
        val toStack = result[to - 1]
        result[to - 1] = toStack.plus(fromStack.last())
        result[from - 1] = fromStack.dropLast(1)
    }
    return result
}

private fun move2(stacks: Matrix<Char>, moves: Int, from: Int, to: Int): Matrix<Char> {
    val result = stacks.toMutableList()
    val fromStack = result[from - 1]
    val toStack = result[to - 1]
    result[to - 1] = toStack.plus(fromStack.subList(fromStack.size - moves, fromStack.size))
    result[from - 1] = fromStack.dropLast(moves)
    return result
}

private fun parseInstructions(input: List<String>, separator: Int) =
    input.subList(separator + 1, input.size)
        .map { Instruction.fromString(it) }

private fun parseStacks(input: List<String>, separator: Int): Matrix<Char> {
    val crateLines = input.subList(0, separator - 1)
    val maxLength = crateLines.maxOf { it.length }
    val crates = crateLines
        .map { it.padEnd(maxLength, ' ') }
        .map(::sanitize)
        .reversed()
        .map { it.toCharArray().toList() }
    return crates.transpose()
}

private fun sanitize(input: String) =
    input
        .replace("    ", " [.]")
        .replace(" ", "")
        .replace(".", " ")
        .replace("[", "")
        .replace("]", "")

private fun topCrates(stacks: Matrix<Char>) =
    stacks
        .map { it.lastOrNull() ?: " " }
        .joinToString(separator = "")

private typealias Matrix<T> = List<List<T>>

private fun <T> Matrix<T>.transpose(): Matrix<T> {
    return List(maxOf { it.size }) { i ->
        this.map { it[i] }.filter { it != ' ' }
    }
}

private data class Instruction(val amount: Int, val from: Int, val to: Int) {
    companion object {
        private val regex = """move (\d+) from (\d+) to (\d+)""".toRegex()

        fun fromString(line: String): List<Int> =
            regex
                .find(line)
                ?.groupValues
                ?.drop(1)
                ?.map(String::toInt)
                ?: emptyList()
    }
}
