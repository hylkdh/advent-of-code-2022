package day11

import Solution
import Solutions
import input

fun solve(): Solutions<Long> {
    val day = "11"
    val input = input(day)
        .chunked(7)
        .map { it.joinToString("\n") }
    return Solutions(
        day,
        Solution(part1(input), 58794L),
        Solution(part2(input), 20151213744L)
    )
}

private fun part1(input: List<String>): Long {
    val monkeys = input.map(::toMonkey)
    play(monkeys, 20) { it / 3 }
    return monkeyBusiness(monkeys)
}

private fun part2(input: List<String>): Long {
    val monkeys = input.map(::toMonkey)

    // "Find another way to keep your worry levels manageable"
    // The worry level should be reduced in a way that tests by future monkeys still pass and fail like they did before.
    // Each monkey divides the worry level by a certain divisor. If this leaves no remainder, the worry level modulo the
    // least common multiple of all divisors still won't. The reverse is also true. Because all divisors are prime their
    // least common multiple is their product. (This took a hint from Reddit...)
    val leastCommonMultiple = monkeys.map(Monkey::divisor).product()

    play(monkeys, 10_000) { it % leastCommonMultiple }
    return monkeyBusiness(monkeys)
}

private fun play(monkeys: List<Monkey>, rounds: Int, relieve: (Long) -> Long) =
    repeat(rounds) {
        monkeys.forEach { monkey ->
            monkey.items.forEach { item ->
                val worry = calculateWorryLevel(monkey, item)
                val relieved = relieve(worry)
                val next = determineNext(relieved, monkey)
                monkeys[next].items.add(relieved)
                monkey.inspections++
            }
            monkey.items = mutableListOf()
        }
    }

private fun calculateWorryLevel(monkey: Monkey, item: Long) =
    when (monkey.operationType) {
        OperationType.ADD -> item + if (monkey.operationValue == "old") item else monkey.operationValue.toLong()
        OperationType.MULTIPLY -> item * if (monkey.operationValue == "old") item else monkey.operationValue.toLong()
    }

private fun determineNext(worry: Long, monkey: Monkey) =
    if (worry % monkey.divisor == 0L) monkey.ifTrue else monkey.ifFalse

private fun monkeyBusiness(monkeys: List<Monkey>): Long =
    monkeys
        .map(Monkey::inspections)
        .sortedDescending()
        .take(2)
        .product()

private fun toMonkey(s: String): Monkey {
    val template = """
        Monkey \d:
        \s+Starting items: ([\d,\s]+)
        \s+Operation: new = old ([+|*]) (\w+)
        \s+Test: divisible by (\d+)
        \s+If true: throw to monkey (\d)
        \s+If false: throw to monkey (\d)
        """.trimIndent()
    val matches = template.toRegex().find(s)?.groups
    return Monkey(
        matches?.get(1)?.value?.split(", ")?.map(String::toLong)?.toMutableList() ?: mutableListOf(),
        if (matches?.get(2)?.value == "+") OperationType.ADD else OperationType.MULTIPLY,
        matches?.get(3)?.value ?: "",
        matches?.get(4)?.value?.toInt() ?: -1,
        matches?.get(5)?.value?.toInt() ?: -1,
        matches?.get(6)?.value?.toInt() ?: -1
    )
}

private fun List<Int>.product(): Int = this.reduce { accumulator, element -> accumulator * element }

private fun List<Long>.product(): Long = this.reduce { accumulator, element -> accumulator * element }

private data class Monkey(
    var items: MutableList<Long>,
    val operationType: OperationType,
    val operationValue: String,
    val divisor: Int,
    val ifTrue: Int,
    val ifFalse: Int,
    var inspections: Long = 0
)

private enum class OperationType { ADD, MULTIPLY }
