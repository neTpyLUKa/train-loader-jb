data class Train(val id: Int, val timeStart: Long, val timeEnd: Long, val payment: Long)

fun main() {
    val trains = readInput()

    val (ids, total) = solve(trains)

    ids.forEach { print("$it ") }
    println(total)
}