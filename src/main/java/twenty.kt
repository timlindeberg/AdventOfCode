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

    var max = 0L
    var numValid = 0L
    for ((first, last) in ranges) {
        if (first > max)
            numValid += (first - max) - 1
        max = Math.max(max, last)
    }
    println("Num: $numValid")
}