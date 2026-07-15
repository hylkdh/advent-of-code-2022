import java.io.File
import kotlin.time.measureTimedValue

fun main() {
    val bar = "-".repeat(54)
    println(bar)
    println("Advent of Code 2022")
    println(bar)
    listOf<() -> Solutions<*>>(
        { day01.solve() },
        { day02.solve() },
        { day03.solve() },
        { day04.solve() },
        { day05.solve() },
        { day06.solve() },
        { day07.solve() },
        { day08.solve() },
        { day09.solve() },
        { day10.solve() },
        { day11.solve() },
        { day12.solve() },
        { day13.solve() },
        { day14.solve() },
        { day15.solve() },
    ).forEach { solutions ->
        val timedValue = measureTimedValue {
            try {
                solutions.invoke()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        println(
            timedValue.value.toString() +
            timedValue.duration.inWholeMilliseconds.toString().padStart(10, ' ') + " ms"
        )
    }
    println(bar)
}

data class Solutions<T>(val day: String, val part1: Solution<T>, val part2: Solution<T>) {
    override fun toString(): String = "Day $day:" +
        part1.toString().padStart(13, ' ')  +
        part2.toString().padStart(18, ' ')
}

data class Solution<T>(val actual: T, val expected: T) {
    override fun toString(): String = actual.toString() + ' ' + icon()
    private fun icon(): String = if (actual == expected) "⭐" else "❌"
}

fun input(day: String) =
    File("src/day$day", "input.txt")
        .readLines()
