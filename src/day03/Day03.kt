package day03

import Solution
import Solutions
import input

fun solve(): Solutions<Any> {
    val day = "03"
    val input = input(day)
    return Solutions(
        day,
        Solution(part1(input), 8123),
        Solution(part2(input), 2620)
    )
}

private fun part1(input: List<String>): Int =
    input
        .map(::compartments)
        .map(::sharedItem)
        .sumOf(::priority)

private fun part2(input: List<String>): Int =
    input
        .chunked(3)
        .map(::sharedItem)
        .sumOf(::priority)

private fun compartments(rucksack: String): List<String> =
    listOf(
        rucksack.take(rucksack.length / 2),
        rucksack.substring(rucksack.length / 2)
    )

private fun sharedItem(rucksacks: List<String>): Char =
    rucksacks
        .map(String::toSet)
        .reduce(Set<Char>::intersect)
        .first()

private fun priority(char: Char): Int =
    when (char.code) {
        in 97..122 -> char.code - 96
        in 65..90 -> char.code - 38
        else -> 0
    }
