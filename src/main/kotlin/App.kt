package main

import java.util.*

data class Train(var id: Int, val timeStart: Long, val timeEnd: Long, val payment: Long)

data class Value(val start: Long?, val prev: Long?, val value: Long, val id: Int)

fun comp() = kotlin.Comparator<Train> { lhs, rhs ->
    if (lhs.timeEnd != rhs.timeEnd) {
        (lhs.timeEnd - rhs.timeEnd).toInt()
    } else {
        (rhs.timeStart - lhs.timeStart).toInt()
    }
}

fun solve(trains: MutableList<Train>): Pair<List<Int>, Long> {
    trains.sortWith(comp())
    val dp = TreeMap<Long, Value>()
    var mx: Long = 0L
    var prev: Long? = null
    var start: Long? = null

    for (train in trains) {
        val tEnd = train.timeEnd
        val kv = dp.floorEntry(train.timeStart)
        if (kv == null) {
            if (mx < train.payment) {
                mx = train.payment
                start = tEnd
                prev = null
            }
        } else {
            val cand = kv.value.value + train.payment
            if (mx < cand) {
                mx = cand
                start = tEnd
                prev = kv.value.start
            }
        }
        val value = dp[tEnd]
        if (value != null && value.value == mx) {
            continue
        }
        dp[tEnd] = Value(start, prev, mx, train.id)
    }

    val ans = mutableListOf<Int>()
    while (start != null) {
        ans.add(dp[start]!!.id)
        start = dp[start]!!.prev
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