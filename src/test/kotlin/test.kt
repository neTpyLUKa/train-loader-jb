import main.Train
import main.getTimeSeconds
import main.solve
import kotlin.random.Random
import kotlin.test.assertEquals
import org.junit.Test as test

/*
fun getInputString(trains: List<TrainInput>): String {
    var input = "${trains.size}\n"
    for (train in trains) {
        input += "${train.id} ${train.timeStart} ${train.timeEnd} ${train.payment}"
    }
    return input
}*/

fun bruteForce(trains: List<Train>): Long {
    val one = 1
    val size = trains.size
    val bnd = one.shl(size)
    var mx = 0L
    for (i in 0 until bnd) {
        val cur = mutableListOf<Train>()
        var curSum = 0L
        for (j in 0 until size) {
            if (one.shl(j).and(i) != 0) {
                cur.add(trains[j])
                curSum += trains[j].payment
            }
        }
        cur.sortBy { it.timeStart }
        val curSize = cur.size
        var ok = true
        for (k in 0 until curSize - 1) {
            if (cur[k].timeEnd > cur[k + 1].timeStart) {
                ok = false
                break
            }
        }
        if (ok) {
            mx = maxOf(mx, curSum)
        }
    }
    return mx
}

fun checkOutput(output: Pair<List<Int>, Long>, trains: List<Train>, value: Long = bruteForce(trains)) {
    assertEquals(output.second, value, "Expected: $value \n Actual: ${output.second}\n")
    var sum = 0L
    val idSet = output.first.toMutableSet()
    for (train in trains) {
        if (idSet.contains(train.id)) {
            sum += train.payment
            idSet.remove(train.id)
        }
    }
    assert(idSet.isEmpty()) { "Extra id found\n" }
    System.err.println("Expected: $value, \n Actual calculated: ${sum}")
    assertEquals(sum, value, "Expected: $value, \n Actual calculated: ${sum}\n")
}

fun addGenerator(
    trains: MutableList<Train>,
    startMin: Long,
    startMax: Long,
    endMin: Long,
    endMax: Long,
    id_start: Int,
    steps: Int
): Long { // max_payment
    var id = id_start
    val random = Random(42)
    var mx = 0L
    for (i in 0 until steps) {
        val payment = random.nextLong(1, 10000)
        mx = maxOf(mx, payment)
        trains.add(
            Train(
                id = id,
                timeStart = random.nextLong(startMin, startMax),
                timeEnd = random.nextLong(endMin, endMax),
                payment = payment
            )
        )
        ++id
    }
    System.err.println(mx)
    return mx
}

class TestSource() {
     @test
     fun empty() {
         val trains = mutableListOf<Train>()
         val output = solve(trains)
         checkOutput(output, trains)
     }

     @test
     fun simple() {
         val trains = mutableListOf(Train(21, getTimeSeconds("10:00"), getTimeSeconds("10:10"), 10))
         val output = solve(trains)
         checkOutput(output, trains)
     }

     @test
     fun consequent() {
         val trains = mutableListOf(
             Train(1, getTimeSeconds("10:00"), getTimeSeconds("11:10"), 10),
             Train(2, getTimeSeconds("11:10"), getTimeSeconds("12:20"), 10),
             Train(3, getTimeSeconds("12:20"), getTimeSeconds("13:30"), 10)
         )
         val output = solve(trains)
         checkOutput(output, trains)
     }

     @test
     fun consequentShuffle() {
         val trains = mutableListOf(
             Train(3, getTimeSeconds("12:20"), getTimeSeconds("13:30"), 10),
             Train(2, getTimeSeconds("11:10"), getTimeSeconds("12:20"), 10),
             Train(1, getTimeSeconds("10:00"), getTimeSeconds("11:10"), 10)
         )
         val output = solve(trains)
         checkOutput(output, trains)
     }

     @test
     fun overlapping() {
         val trains = mutableListOf(
             Train(1, getTimeSeconds("10:00"), getTimeSeconds("11:11"), 10),
             Train(2, getTimeSeconds("11:10"), getTimeSeconds("12:20"), 10),
             Train(3, getTimeSeconds("12:19"), getTimeSeconds("13:30"), 10)
         )
         val output = solve(trains)
         checkOutput(output, trains)
     }

     @test
     fun overlappingShuffle() {
         val trains = mutableListOf(
             Train(3, getTimeSeconds("12:19"), getTimeSeconds("13:30"), 10),
             Train(1, getTimeSeconds("10:00"), getTimeSeconds("11:11"), 10),
             Train(2, getTimeSeconds("11:10"), getTimeSeconds("12:20"), 10)
         )
         val output = solve(trains)
         checkOutput(output, trains)

     }

     @test
     fun overlappingMiddle() {
         val trains = mutableListOf(
             Train(1, getTimeSeconds("10:00"), getTimeSeconds("11:11"), 10),
             Train(2, getTimeSeconds("11:10"), getTimeSeconds("12:20"), 21),
             Train(3, getTimeSeconds("12:19"), getTimeSeconds("13:30"), 10)
         )
         val output = solve(trains)
         checkOutput(output, trains)
     }

     @test
     fun segmented() {
         val trains = mutableListOf(
             Train(1, getTimeSeconds("10:00"), getTimeSeconds("11:11"), 10),
             Train(2, getTimeSeconds("14:10"), getTimeSeconds("16:20"), 21),
             Train(3, getTimeSeconds("18:19"), getTimeSeconds("18:30"), 10),
             Train(4, getTimeSeconds("18:50"), getTimeSeconds("19:00"), 10),
             Train(5, getTimeSeconds("21:19"), getTimeSeconds("22:30"), 10)
         )
         val output = solve(trains)
         checkOutput(output, trains)
     }

     @test
     fun stressManyRepeating() {
         val trains = mutableListOf(
             Train(1, getTimeSeconds("10:00"), getTimeSeconds("11:11"), 10),
             Train(2, getTimeSeconds("14:10"), getTimeSeconds("16:20"), 21),
             Train(3, getTimeSeconds("18:19"), getTimeSeconds("18:30"), 10)
         )
         for (i in 0 until 1000000 step 3) {
             var cur = trains[0]
             cur.id = i
             trains.add(cur)

             cur = trains[1]
             cur.id = i + 1
             trains.add(cur)

             cur = trains[2]
             cur.id = i + 2
             trains.add(cur)
         }

         val output = solve(trains)
         checkOutput(output, trains, 41)
     }

     @test
     fun stressOneInAnswer() {
         val trains = mutableListOf<Train>()
         val id = 0
         val ans = addGenerator(
             trains,
             getTimeSeconds("00:00"),
             getTimeSeconds("12:00"),
             getTimeSeconds("12:01"),
             getTimeSeconds("23:59"),
             id,
             10000
         )
         val output = solve(trains)
         checkOutput(output, trains, ans)
     }

     @test
     fun stressTwoInAnswer() {
         val trains = mutableListOf<Train>()
         var id = 0
         var ans = addGenerator(
             trains,
             getTimeSeconds("00:00"),
             getTimeSeconds("06:00"),
             getTimeSeconds("06:01"),
             getTimeSeconds("12:00"),
             id,
             10000
         )
         id = 20000
         ans += addGenerator(
             trains,
             getTimeSeconds("12:01"),
             getTimeSeconds("18:00"),
             getTimeSeconds("18:01"),
             getTimeSeconds("23:59"),
             id,
             10000
         )
         val output = solve(trains)
         checkOutput(output, trains, ans)
     }

    @test
    fun stressThreeInAnswer() {
        val trains = mutableListOf<Train>()
        var id = 0
        val steps = 20000
        var ans = addGenerator(
            trains,
            getTimeSeconds("00:00"),
            getTimeSeconds("04:00"),
            getTimeSeconds("04:01"),
            getTimeSeconds("08:00"),
            id,
            steps
        )
        id = 20000
        ans += addGenerator(
            trains,
            getTimeSeconds("08:01"),
            getTimeSeconds("12:00"),
            getTimeSeconds("12:01"),
            getTimeSeconds("15:59"),
            id,
            steps
        )
        id = 40000
        ans += addGenerator(
            trains,
            getTimeSeconds("16:00"),
            getTimeSeconds("19:59"),
            getTimeSeconds("20:00"),
            getTimeSeconds("23:59"),
            id,
            steps
        )
        val output = solve(trains)
        checkOutput(output, trains, ans)
    }
}
