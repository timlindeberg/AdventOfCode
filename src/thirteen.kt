import java.io.File
import java.util.*

data class Dijkstra(val distance: Int, val pos: Pos) : Comparable<Dijkstra> {
    override fun compareTo(other: Dijkstra): Int {
        return distance - other.distance
    }

}

data class Pos(val x: Int, val y: Int)

fun distance(distances: HashMap<Pos, Int>, pos: Pos): Int {
    return distances[pos] ?: Int.MAX_VALUE
}

fun main(args: Array<String>) {

    val key = File("input/thirteen.txt").readText().toInt()

    val dist = HashMap<Pos, Int>()
    val queue = TreeSet<Dijkstra>()
    val parents = HashMap<Pos, Pos>()

    val start = Pos(1, 1)
    val goal = Pos(31, 39)
    val INF = 10000000

    dist[start] = 0
    queue.add(Dijkstra(0, start))
    var i = 0
    while (queue.isNotEmpty() && i < 10000) {
        val k = queue.first()
        queue.remove(k)
        val u = k.pos
        val (x, y) = u

        println("$x, $y")

        val neighbours = arrayOf(Pos(x + 1, y), Pos(x, y + 1), Pos(x - 1, y), Pos(x, y - 1))
                .filter { !isWall(it, key) }

        for (v in neighbours) {
            val alt = (dist[u] ?: INF) + 1
            val distv = dist[v] ?: INF
            if (alt < distv) {
                parents[v] = u
                dist[v] = alt
                queue.remove(Dijkstra(distv, v))
                queue.add(Dijkstra(alt, v))
            }
        }
        i++
    }

    var pos = goal
    var d = 0
    while (pos != start) {
        println(pos)
        pos = parents[pos]!!
        d++
    }
    println(d)
}

fun isWall(pos: Pos, key: Int): Boolean {
    val (x, y) = pos
    if (x < 0 || y < 0)
        return true

    val a = (x * x) + (3 * x) + (2 * x * y) + y + (y * y) + key
    return numSetBits(a) % 2 != 0
}


fun numSetBits(i: Int): Int {
    var i = i
    i = i - (i ushr 1 and 0x55555555)
    i = (i and 0x33333333) + (i ushr 2 and 0x33333333)
    return (i + (i ushr 4) and 0x0F0F0F0F) * 0x01010101 ushr 24
}
