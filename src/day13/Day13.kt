package day13

import Solution
import Solutions
import input

fun solve(): Solutions<Int> {
    val day = "13"
    val input = input(day)
        .chunked(3) {
            it[0] to it[1]
        }
    return Solutions(
        day,
        Solution(part1(input), 5684),
        Solution(part2(input), 22932)
    )
}

private fun part1(pairs: List<Pair<String, String>>): Int =
    pairs
        .map(::parse)
        .mapIndexed { index, pair ->
            if (compare(pair.first, pair.second) < 0)
                index + 1
            else 0
        }
        .sum()

private fun part2(pairs: List<Pair<String, String>>): Int {
    val divider1 = "[[2]]"
    val divider2 = "[[6]]"

    val sorted = (pairs + (divider1 to divider2))
        .map(::parse)
        .flatMap { it.toList() }
        .sortedWith(::compare)

    return (sorted.indexOf(parse(divider1).first) + 1) *
           (sorted.indexOf(parse(divider2).first) + 1)
}

private fun compare(left: Any, right: Any): Int =
    when {
        left is Int && right is Int -> compareInts(left, right)
        left is List<*> && right is List<*> -> compareLists(left, right)
        left is List<*> -> compareLists(left, listOf(right))
        right is List<*> -> compareLists(listOf(left), right)
        else -> 0
    }

private fun compareLists(left: List<*>, right: List<*>): Int {
    var index = 0
    while (index < left.size && index < right.size) {
        val result = compare(left[index]!!, right[index]!!)
        if (result != 0)
            return result
        index++
    }
    return left.size - right.size
}

private fun compareInts(left: Int, right: Int): Int = left - right

private fun parse(pair: Pair<String, String>): Pair<List<Any>, List<Any>> =
    parse(pair.first).first to parse(pair.second).first

private fun parse(packet: String, index: Int = 1): Pair<List<Any>, Int> {
    var index = index
    var char: Char
    var word = ""
    val result = ArrayList<Any>()

    while (index < packet.length) {
        char = packet[index++]
        when (char) {
            ']' -> {
                if (word != "") result.add(word.toInt())
                return result to index
            }

            ',' -> {
                if (word != "") result.add(word.toInt())
                word = ""
            }

            '[' -> {
                val (result2, index2) = parse(packet, index)
                index = index2
                result.add(result2)
            }

            else -> word += char
        }
    }
    throw IllegalStateException("Parse error (unbalanced brackets?)")
}
