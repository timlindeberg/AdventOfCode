import java.io.File

/**
 * Created by tlin on 16/12/16.
 */

//Disc #6 has 17 positions; at time=0, it is at position 5.

val R = """Disc #\d has (\d+) positions; at time=0, it is at position (\d+).""".toRegex()

data class Disc(val numPos: Int, val startPos: Int)
fun main(args: Array<String>) {
    val discs = File("input/fifteen.txt").readLines().map {
        val m = R.matchEntire(it)!!
        Disc(m.groupValues[1].toInt(), m.groupValues[2].toInt())
    }
    val d1= discs[0]

    println(discs)
    var time = d1.numPos - d1.startPos
    while(true){
        if(matches(discs, time))
            break
        time += d1.numPos
    }
    println("Time: ${time  - 1}")
}

fun matches(discs: List<Disc>, time: Int): Boolean {
    var time = time
    return discs
            .asSequence()
            .map { (it.startPos + time++) % it.numPos }
            .none { it != 0 }
}