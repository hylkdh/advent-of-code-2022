package day15

import Solution
import Solutions
import input
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun solve(): Solutions<Any> {
    val day = "15"
    val input = input(day)
    val sensors = input.map { line ->
        val match = Regex("^Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)").find(line)!!
        val (xSensor, ySensor, xBeacon, yBeacon) = match.destructured
        Sensor(Point(xSensor.toInt(), ySensor.toInt()), Point(xBeacon.toInt(), yBeacon.toInt()))
    }
    return Solutions(
        day,
        Solution(part1(sensors), 5511201),
        Solution(part2(sensors), 11318723411840L)
    )
}

private fun part1(sensors: List<Sensor>): Int {
    val row = 2_000_000
    val minMax = sensors
        .mapNotNull { sensor -> sensor.rangeOnRow(row)?.toPair() }
        .unzip()
    return Range(minMax.first.min(), minMax.second.max()).length()
}

private fun part2(sensors: List<Sensor>): Long {
    val max = 4_000_000

    for (row in 0..max) {
        val ranges = sensors
            .mapNotNull { it.rangeOnRow(row) }
            .map { range -> range.clamp(Range(0, max)) }
            .mergeOverlapping()

        if (ranges.size == 2 && ranges[0].end + 2 == ranges[1].start) {
            val x = ranges[0].end + 1
            return x.toLong() * max + row.toLong()
        }
    }
    throw IllegalStateException()
}

private data class Sensor(val position: Point, val beacon: Point) {
    val beaconDistance = position.distanceTo(beacon)
    val xMin = position.x - beaconDistance
    val xMax = position.x + beaconDistance
    fun coversRow(y: Int): Boolean = abs(position.y - y) <= beaconDistance
    fun rangeOnRow(y: Int): Range? =
        if (coversRow(y)) Range(xMin + abs(position.y - y), xMax - abs(position.y - y)) else null
}

private data class Point(val x: Int, val y: Int) {
    fun distanceTo(probe: Point): Int = abs(probe.x - x) + abs(probe.y - y)
}

private data class Range(val start: Int, val end: Int) {
    fun toPair() = start to end
    fun length() = end - start
    fun overlapsWith(other: Range): Boolean = end + 1 >= other.start // assume this.start <= other.start
    fun merge(other: Range): Range = Range(min(start, other.start), max(end, other.end)) // assume overlap
    fun clamp(other: Range): Range = Range(max(start, other.start), min(end, other.end))
}

private fun List<Range>.mergeOverlapping(): List<Range> {
    val sorted = sortedBy { it.start }
    var current = sorted[0]
    val result = mutableListOf<Range>()
    for (i in 1 until size) {
        if (current.overlapsWith(sorted[i])) {
            current = current.merge(sorted[i])
        } else {
            result.add(current)
            current = sorted[i]
        }
    }
    result.add(current)
    return result
}
