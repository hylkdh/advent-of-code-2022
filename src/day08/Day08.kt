package day08

import Solution
import Solutions
import input

fun solve(): Solutions<Int> {
    val day = "08"
    val input = input(day).toIntMatrix()
    return Solutions(
        day,
        Solution(part1(input), 1672),
        Solution(part2(input), 327180)
    )
}

private fun part1(input: Matrix<Int>): Int =
    input.indices.sumOf { y ->
        input[y].indices.count { x ->
            isVisible(input.row(y), x) || isVisible(input.column(x), y)
        }
    }

private fun part2(input: Matrix<Int>): Int =
    input.indices.maxOf { y ->
        input[y].indices.maxOf { x ->
            score(input.row(y), x) * score(input.column(x), y)
        }
    }

private typealias Matrix<T> = Array<Array<T>>

private fun List<String>.toIntMatrix(): Matrix<Int> =
    this.map { it.toCharArray().map(Char::digitToInt).toTypedArray() }.toTypedArray()

private fun Matrix<Int>.row(i: Int) = this[i].toList()

private fun Matrix<Int>.column(i: Int) = this.map { it[i] }.toList()

private fun isVisible(row: List<Int>, index: Int): Boolean = isVisibleLeft(row, index) || isVisibleRight(row, index)

private fun isVisibleLeft(row: List<Int>, index: Int): Boolean {
    if (index == 0) return true
    return row[index] > row.subList(0, index).max()
}

private fun isVisibleRight(row: List<Int>, index: Int): Boolean {
    if (index == row.lastIndex) return true
    return row[index] > row.subList(index + 1, row.size).max()
}

private fun score(row: List<Int>, index: Int): Int = scoreLeft(row, index) * scoreRight(row, index)

private fun scoreLeft(row: List<Int>, thresholdIndex: Int): Int {
    if (thresholdIndex == 0) return 0
    val left = row.subList(0, thresholdIndex)
    val edgeIndex = left.indexOfLastOrNull { it >= row[thresholdIndex] } ?: 0
    return thresholdIndex - edgeIndex
}

private fun scoreRight(row: List<Int>, thresholdIndex: Int): Int {
    if (thresholdIndex == row.lastIndex) return 0
    val right = row.subList(thresholdIndex + 1, row.size)
    val edgeIndex = thresholdIndex + 1 + (right.indexOfFirstOrNull { it >= row[thresholdIndex] } ?: right.lastIndex)
    return edgeIndex - thresholdIndex
}

private fun List<Int>.indexOfFirstOrNull(predicate: (Int) -> Boolean): Int? {
    val index = this.indexOfFirst(predicate)
    return if (index > -1) index else null
}

private fun List<Int>.indexOfLastOrNull(predicate: (Int) -> Boolean): Int? {
    val index = this.indexOfLast(predicate)
    return if (index > -1) index else null
}
