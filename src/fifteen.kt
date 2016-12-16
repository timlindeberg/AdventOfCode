import java.io.File

val R = """Disc #\d has (\d+) positions; at time=0, it is at position (\d+).""".toRegex()

data class Disc(val numPos: Int, val startPos: Int)
fun main(args: Array<String>) {
    val discs = File("input/fifteen.txt").readLines().map {
        val m = R.matchEntire(it)!!
        Disc(m.groupValues[1].toInt(), m.groupValues[2].toInt())
    }

    val startTime = discs[0].numPos - discs[0].startPos
    val increment = discs[0].numPos
    val time = generateSequence(startTime) { it + increment }.find { matches(discs, it) }!!
    println("Time: ${time  - 1}")
}

fun matches(discs: List<Disc>, time: Int): Boolean {
    var t = time
    return discs
            .asSequence()
            .map { (it.startPos + t++) % it.numPos }
            .none { it != 0 }
}