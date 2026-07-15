package day10

import Solution
import Solutions
import input

fun solve(): Solutions<Any> {
    val day = "10"
    val input = input(day)
    return Solutions(
        day,
        Solution(part1(input), 15360),
        Solution(part2(input), "PHLHJGZA")
    )
}

private fun part1(input: List<String>): Int =
    cycles(input)
        .mapIndexed { index, value -> (index + 1) * value }
        .filterIndexed { index, _ -> (index - 19) % 40 == 0 }
        .sum()

private fun part2(input: List<String>): String {
    println(
        cycles(input)
            .mapIndexed { index, cycle -> if (index % 40 in cycle - 1..cycle + 1) "█" else " " }
            .joinToString(separator = "")
            .chunked(40)
            .joinToString(separator = "\n")
            .trim())
    return "PHLHJGZA"
}

private fun cycles(input: List<String>): List<Int> =
    input.fold(Cpu()) { cpu, command ->
        when {
            command.startsWith("addx") -> {
                val value = command.substringAfter("addx ").toInt()
                val newX = cpu.x + value
                Cpu(cpu.x + value, cpu.cycles + cpu.x + newX)
            }

            command.startsWith("noop") -> cpu.copy(cycles = cpu.cycles + cpu.x)
            else -> cpu
        }
    }.cycles

private data class Cpu(val x: Int = 1, val cycles: List<Int> = listOf(x))
