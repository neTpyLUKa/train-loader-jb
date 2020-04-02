import java.util.*

data class Value(val prev: Long?, val value: Long, val id: Int)

fun solve(trains: MutableList<Train>): Pair<List<Int>, Long> {
    trains.sortBy { it.timeEnd }
    val dp = TreeMap<Long, Value>()
    var mx = 0L
    var prev: Long? = null
    for (train in trains) {
        val tEnd = train.timeEnd
        val kv = dp.floorEntry(train.timeStart)
        if (kv == null) {
            if (mx < train.payment) {
                mx = train.payment
                prev = null
            }
        } else {
            val cand = kv.value.value + train.payment
            if (mx < cand) {
                mx = cand
                prev = kv.key
            }
        }
        dp[tEnd] = Value(prev, mx, train.id)
        prev = tEnd
    }

    val ans = mutableListOf<Int>()
    while (prev != null) {
        ans.add(dp[prev]!!.id)
        prev = dp[prev]!!.prev
    }
    return Pair(ans, mx)
}