package day02

import Solution
import Solutions
import input

fun solve(): Solutions<Int> {
    val day = "02"
    val input = input(day)
    return Solutions(
        day,
        Solution(part1(input), 13005),
        Solution(part2(input), 11373)
    )
}

private fun part1(input: List<String>): Int =
    input.sumOf { shapeScore(shape1(it)) + outcomeScore(outcome1(it)) }

private fun part2(input: List<String>): Int =
    input
        .map(::toLDW)
        .sumOf { shapeScore(shape2(it)) + outcomeScore(outcome2(it)) }

private fun shape1(round: String) = round.last().toString()

private fun shape2(round: String) =
    when (round) {
        "A D", "B L", "C W" -> "X"
        "A W", "B D", "C L" -> "Y"
        "A L", "B W", "C D" -> "Z"
        else -> throw IllegalArgumentException()
    }

private fun outcome1(round: String) =
    when (round) {
        "A X", "B Y", "C Z" -> "D"
        "A Y", "B Z", "C X" -> "W"
        else -> "L"
    }

private fun outcome2(round: String) = round.last().toString()

private fun shapeScore(shape: String) =
    when (shape) {
        "X" -> 1
        "Y" -> 2
        "Z" -> 3
        else -> throw IllegalArgumentException()
    }

private fun outcomeScore(outcome: String) =
    when (outcome) {
        "D" -> 3
        "W" -> 6
        "L" -> 0
        else -> throw IllegalArgumentException()
    }

private fun toLDW(round: String) =
    round
        .replace("X", "L")
        .replace("Y", "D")
        .replace("Z", "W")
