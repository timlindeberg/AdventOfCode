import java.io.File

fun main(args: Array<String>) {
    val line = File("input/eighteen.txt").readText()
    val safeTiles = generateSequence(line, ::nextLine).take(400000).map { it.count { it == '.' } }.sum()
    println(safeTiles)
}

fun nextLine(line: String): String {
    val e = ".$line."
    return (1.. e.length - 2).map { if (e[it - 1] != e[it + 1]) '^' else '.' }.joinToString("")
}