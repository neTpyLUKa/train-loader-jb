package main

import java.util.*

data class Train(var id: Int, val timeStart: Long, val timeEnd: Long, val payment: Long)

data class Value(val value: Long, val id: Int?)

fun comp() = kotlin.Comparator<Train> { lhs, rhs ->
    if (lhs.timeEnd != rhs.timeEnd) {
        (lhs.timeEnd - rhs.timeEnd).toInt()
    } else {
        (rhs.timeStart - lhs.timeStart).toInt()
    }
}

fun solve(trains: MutableList<Train>): Pair<List<Int>, Long> {
    trains.sortWith(comp())
    val trainMap = mutableMapOf<Int, Train>()
    trains.forEach { trainMap[it.id] = it }
    val dp = TreeMap<Long, Value>()
    var mx: Long = 0L
    var id: Int? = null
    for (train in trains) {
        val tEnd = train.timeEnd
        val kv = dp.floorEntry(train.timeStart)
        var cand = train.payment
        if (kv != null) {
            cand += kv.value.value
        }
        if (mx < cand) {
            mx = cand
            id = train.id
        }
        val value = dp[tEnd]
        if (value != null && value.value == mx) {
            continue
        }
        dp[tEnd] = Value(mx, id)
    }

    val ans = mutableListOf<Int>()
    while (id != null) {
        ans.add(id)
        id = dp.floorEntry(trainMap[id]!!.timeStart)?.value?.id
    }
    return Pair(ans, mx)
}

fun getTimeSeconds(time: String): Long {
    val (hours, mins) = time.split(':').map { it.toLong() }
    return (hours * 60 + mins) * 60
}

fun readInput(): MutableList<Train> {
    val n = readLine()!!.toInt()
    val trains = mutableListOf<Train>()
    for (i in 0 until n) {
        val (id, timeStart, duration, payment) = readLine()!!.split(' ')
        val timeStartSeconds = getTimeSeconds(timeStart)
        val timeEndSeconds = getTimeSeconds(duration) + timeStartSeconds
        trains.add(
            Train(
                id = id.toInt(),
                timeStart = timeStartSeconds,
                timeEnd = timeEndSeconds,
                payment = payment.toLong()
            )
        )
    }
    return trains
}

fun main() {
    val trains = readInput()

    val (ids, total) = solve(trains)

    println(total)
    ids.forEach { print("$it ") }
}