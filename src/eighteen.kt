import java.io.File

val SAFE = '.'
val TRAP = '^'

fun main(args: Array<String>) {
    var line = File("input/eighteen.txt").readText()
    val lines = 400000

    var safeTiles = 0
    for (i in 1..lines) {
        safeTiles += line.count { it == SAFE }
        line = nextLine(line)
    }
    println(safeTiles)
}

fun nextLine(line: String): String {
    val sb = StringBuilder()
    for (i in 0..line.length - 1) {
        val left = if (i == 0) SAFE else line[i - 1]
        val center = line[i]
        val right = if (i == line.length - 1) SAFE else line[i + 1]

        sb.append(if(isTrap(left, center, right)) TRAP else SAFE)
    }
    return sb.toString()
}

fun isTrap(left: Char, center: Char, right: Char): Boolean {
    return (left == TRAP && center == TRAP && right == SAFE) ||
           (left == SAFE && center == TRAP && right == TRAP) ||
           (left == TRAP && center == SAFE && right == SAFE) ||
           (left == SAFE && center == SAFE && right == TRAP)
}