import java.io.File
import java.util.*

/**
 * Created by Tim Lindeberg on 12/20/2016.
 */

fun main(args: Array<String>) {
    val ranges = File("input/twenty.txt").readLines().map {
        val vals = it.split('-')
        Pair(vals[0].toLong(), vals[1].toLong())
    }.sortedBy { it.first }

    println("Num: ${numValid(ranges)}")
}

fun numValid(ranges: List<Pair<Long, Long>>): Long {
    var max = 0L
    var num = 0L
    for ((first, last) in ranges) {
        if (first > max)
            num += (first - max) - 1
        max = Math.max(max, last)
    }
    return num
}