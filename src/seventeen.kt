import java.io.File
import java.util.*

data class MazeState(val pos: Pos, val path: String)

fun main(args: Array<String>) {
    val input = File("input/seventeen.txt").readText()

    val start = Pos(0, 0)
    val end = Pos(3, 3)

    val Q = ArrayDeque<MazeState>()
    val paths = HashMap<Pos, MazeState>()

    Q.push(MazeState(start, ""))
    while (Q.isNotEmpty()) {
        val (u, path) = Q.pop()
        for (v in getNeighbours(u, input, path)) {
            val next = MazeState(v, path + direction(u, v))
            paths[v] = next

            if (v == end)
                continue

            Q.add(next)
        }
    }
    println(paths[end]?.path?.length)
}

fun getNeighbours(pos: Pos, input: String, path: String): List<Pos> {
    val (x, y) = pos
    val candidates = listOf(Pos(x, y - 1), Pos(x, y + 1), Pos(x - 1, y), Pos(x + 1, y))

    val hash = MD5(input + path)
    return candidates.indices
            .filter { i ->
                val (x, y) = candidates[i]
                x in 0..3 && y in 0..3 && hash[i] in 'b'..'f'
            }
            .map { candidates[it] }
}

fun direction(from: Pos, to: Pos): Char {
    return when {
        from.x > to.x -> 'L'
        from.x < to.x -> 'R'
        from.y > to.y -> 'U'
        from.y < to.y -> 'D'
        else -> throw Exception()
    }
}