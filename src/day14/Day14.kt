package day14

import Solution
import Solutions
import input
import kotlin.math.max
import kotlin.math.min

fun solve(): Solutions<Int> {
    val day = "14"
    val input = input(day)
        .flatMap { line ->
            line.split(" -> ")
                .map { coordinate ->
                    val (x, y) = coordinate.split(",")
                    Point(x.toInt(), y.toInt())
                }
                .zipWithNext()
                .map { Vertex.fromPair(it) }
        }.toSet()

    return Solutions(
        day,
        Solution(part1(input), 625),
        Solution(part2(input), 25193)
    )
}

private fun part1(vertices: Set<Vertex>): Int {
    val rocks = vertices.flatMap(Vertex::toPoints).toSet()
    val yMax = rocks.maxBy { it.y }.y
    return placeSand(rocks, yMax, yMax + 99)
}

private fun part2(vertices: Set<Vertex>): Int {
    val rocks = vertices.flatMap(Vertex::toPoints).toSet()
    val yMax = rocks.maxBy { it.y }.y
    return placeSand(rocks, yMax + 99, yMax + 2)
}

private fun placeSand(rocks: Set<Point>, yCutoff: Int, yFloor: Int): Int {
    val sand = hashSetOf<Point>()

    do {
        val grain = placeGrain(Point(), rocks, sand, yCutoff, yFloor)
        grain?.let { sand.add(it) }
    } while (grain != null)

    return sand.size
}

private fun placeGrain(grain: Point, rocks: Set<Point>, sand: Set<Point>, yCutoff: Int, yFloor: Int): Point? {
    if (grain.y > yCutoff || sand.contains(Point()))
        return null

    val down = grain.down()
    return if (!rocks.contains(down) && !sand.contains(down) && down.y < yFloor) {
        return placeGrain(down, rocks, sand, yCutoff, yFloor)
    } else {
        val left = down.left()
        if (!rocks.contains(left) && !sand.contains(left) && left.y < yFloor) {
            placeGrain(left, rocks, sand, yCutoff, yFloor)
        } else {
            val right = down.right()
            if (!rocks.contains(right) && !sand.contains(right) && right.y < yFloor) {
                placeGrain(right, rocks, sand, yCutoff, yFloor)
            } else {
                grain
            }
        }
    }
}

private data class Point(val x: Int = 500, val y: Int = 0) {
    fun down() = copy(y = y + 1)
    fun left() = copy(x = x - 1)
    fun right() = copy(x = x + 1)
}

private data class Vertex(val first: Point, val second: Point) {
    fun toPoints(): Set<Point> = when {
        first.x == second.x -> {
            toVerticalPoints()
        }
        first.y == second.y -> {
            toHorizontalPoints()
        }
        else -> emptySet()
    }

    private fun toVerticalPoints() =
        (min(first.y, second.y)..max(first.y, second.y))
            .map { y -> Point(first.x, y) }
            .toSet()

    private fun toHorizontalPoints() =
        (min(first.x, second.x)..max(first.x, second.x))
            .map { x -> Point(x, first.y) }
            .toSet()

    companion object {
        fun fromPair(pair: Pair<Point, Point>): Vertex = Vertex(pair.first, pair.second)
    }
}
