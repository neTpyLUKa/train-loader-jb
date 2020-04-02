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