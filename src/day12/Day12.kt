package day12

import Solution
import Solutions
import input

private const val START_CHAR = 'a'.minus(1)
private const val END_CHAR = 'z'.plus(1)

fun solve(): Solutions<Int> {
    val day = "12"
    val input = input(day)
        .map {
            it.replace('S', START_CHAR)
                .replace('E', END_CHAR)
        }
    return Solutions(
        day,
        Solution(part1(input), 370),
        Solution(part2(input), 363)
    )
}

private fun part1(input: List<String>): Int {
    val heightMap = input.toMatrix()
    val start = heightMap.indexesOf(START_CHAR).first()
    return minDist(heightMap, start)
}

private fun part2(input: List<String>): Int {
    val newStartChar = 'a'
    val heightMap = input.map { it.replace(START_CHAR, newStartChar) }.toMatrix()
    val starts = heightMap.indexesOf(newStartChar)
    return starts.minOfOrNull { minDist(heightMap, it) } ?: -1
}

private fun minDist(heightMap: Matrix<Char>, start: Point): Int {
    val distMap: Matrix<Int> = Array(heightMap.size) { Array(heightMap[0].size) { Int.MAX_VALUE } }
    distMap[start] = 0
    return minDist(heightMap, distMap, start)
}

private fun minDist(heightMap: Matrix<Char>, distMap: Matrix<Int>, start: Point): Int {
    val dist = distMap[start]
    val height = heightMap[start].code

    if (heightMap[start] == END_CHAR) return dist

    return listOf(start.up(), start.down(), start.left(), start.right()).asSequence()
        .filter {
            (heightMap.getOrNull(it)?.code ?: Int.MAX_VALUE) <= height + 1
                    && (distMap.getOrNull(it) ?: 0) > dist + 1
        }
        .onEach { distMap[it] = dist + 1 }
        .minOfOrNull { minDist(heightMap, distMap, it) }
        ?: Int.MAX_VALUE
}

private data class Point(val x: Int, val y: Int) {
    fun up() = copy(y = y - 1)
    fun down() = copy(y = y + 1)
    fun left() = copy(x = x - 1)
    fun right() = copy(x = x + 1)
}

private typealias Matrix<T> = Array<Array<T>>

private fun List<String>.toMatrix(): Matrix<Char> = this.map { it.toCharArray().toTypedArray() }.toTypedArray()

private operator fun <T> Matrix<T>.get(point: Point) = this[point.x][point.y]

private operator fun <T> Matrix<T>.set(point: Point, value: T) {
    this[point.x][point.y] = value
}

private fun <T> Matrix<T>.getOrNull(point: Point): T? =
    this.getOrNull(point.x)?.getOrNull(point.y)

private fun <T> Matrix<T>.indexesOf(value: T): List<Point> =
    this
        .mapIndexed { index, line -> Point(index, line.indexOf(value)) }
        .filter { point -> point.y > -1 }
