package day09

import Solution
import Solutions
import input
import kotlin.math.abs
import kotlin.math.sign

fun solve(): Solutions<Int> {
    val day = "09"
    val input = input(day)
    return Solutions(
        day,
        Solution(part1(input), 6464),
        Solution(part2(input), 2604)
    )
}

private fun part1(input: List<String>): Int {
    var head = Point()
    var tail = Point()
    val visits = mutableSetOf(tail)
    input.map { Instruction.fromString(it) }
        .forEach { instruction ->
            repeat(instruction.distance) {
                head = moveHead(head, instruction.direction)
                tail = moveTail(tail, head)
                visits.add(tail)
            }
        }
    return visits.size
}

private fun part2(input: List<String>): Int {
    val knots = MutableList(10) { Point() }
    val visits = mutableSetOf(knots.last())
    input.map { Instruction.fromString(it) }
        .forEach { instruction ->
            repeat(instruction.distance) {
                knots[0] = moveHead(knots[0], instruction.direction)
                (1 until knots.size).forEach { i ->
                    knots[i] = moveTail(knots[i], knots[i - 1])
                }
                visits.add(knots.last())
            }
        }
    return visits.size
}

private fun moveHead(head: Point, direction: Direction): Point =
    when (direction) {
        Direction.U -> head.up()
        Direction.R -> head.right()
        Direction.D -> head.down()
        Direction.L -> head.left()
    }

private fun moveTail(tail: Point, head: Point): Point {
    val dx = head.x - tail.x
    val dy = head.y - tail.y
    return if (abs(dx) == 2 || abs(dy) == 2) Point(tail.x + dx.sign, tail.y + dy.sign) else tail
}

private data class Point(val x: Int = 0, val y: Int = 0) {
    fun up() = copy(y = y - 1)
    fun down() = copy(y = y + 1)
    fun left() = copy(x = x - 1)
    fun right() = copy(x = x + 1)
}

private enum class Direction { U, R, D, L }

private data class Instruction(val direction: Direction, val distance: Int) {
    companion object {
        fun fromString(line: String): Instruction {
            val (direction, distance) = line.split(" ")
            return Instruction(Direction.valueOf(direction), distance.toInt())
        }
    }
}
