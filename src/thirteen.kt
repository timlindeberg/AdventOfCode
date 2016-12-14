import java.io.File
import java.util.*

data class Pos(val x: Int, val y: Int)

fun main(args: Array<String>) {
    val key = File("input/thirteen.txt").readText().toInt()

    val dist = HashMap<Pos, Int>()
    val queue = ArrayDeque<Pos>()
    val visited = HashSet<Pos>()
    val start = Pos(1, 1)
    val goal = Pos(31, 39)

    dist[start] = 0
    queue.add(start)

    while (queue.isNotEmpty()) {
        val u = queue.pop()

        if(dist[u]!! > 50)
            break

        if(visited.contains(u))
            continue

        visited.add(u)

        val (x, y) = u

        arrayOf(Pos(x + 1, y), Pos(x, y + 1), Pos(x - 1, y), Pos(x, y - 1))
                .filter{ v -> !isWall(v, key) && !visited.contains(v) }
                .forEach { v ->
                    val prevDist = dist[u] ?: 0
                    dist[v] = prevDist + 1
                    queue.add(v)
                }
    }

    println(dist.filterValues { it <= 50 }.size)
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
