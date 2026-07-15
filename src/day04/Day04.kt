package day04

import Solution
import Solutions
import input

fun solve(): Solutions<Int> {
    val day = "04"
    val input = input(day)
    return Solutions(
        day,
        Solution(part1(input), 494),
        Solution(part2(input), 833)
    )
}

private fun part1(input: List<String>): Int =
    input
        .map(::split)
        .count(::overlapCompletely)

private fun part2(input: List<String>): Int =
    input
        .map(::split)
        .count(::overlapPartly)

private fun split(string: String): List<Int> =
    string
        .split(",", "-")
        .map(String::toInt)

private fun overlapCompletely(ranges: List<Int>): Boolean =
    ranges[0] <= ranges[2] && ranges[1] >= ranges[3]
            || ranges[0] >= ranges[2] && ranges[1] <= ranges[3]

private fun overlapPartly(ranges: List<Int>): Boolean =
    ranges[2] <= ranges[1] && ranges[1] <= ranges[3]
            || ranges[0] <= ranges[3] && ranges[3] <= ranges[1]
