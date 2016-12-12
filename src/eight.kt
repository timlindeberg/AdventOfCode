package seven

import java.io.File

val WIDTH = 50
val HEIGHT = 6

val RECT = """rect (\d+)x(\d+)""".toRegex()
val ROTATE_COL = """rotate column x=(\d+) by (\d+)""".toRegex()
val ROTATE_ROW = """rotate row y=(\d+) by (\d+)""".toRegex()
val OPERATIONS = arrayOf(Operation(RECT, ::rect), Operation(ROTATE_COL, ::rotateCol), Operation(ROTATE_ROW, ::rotateRow))

data class Operation(val regex: Regex, val func: (Array<Array<Char>>, Int, Int) -> Array<Array<Char>>)
fun main(args: Array<String>) {
    val startDisplay = Array(HEIGHT, { Array(WIDTH, { '.' }) })

    val finalDisplay = File("input/seven.txt").readLines()
            .map { line ->
                val (regex, op) = OPERATIONS.find { it.regex.matches(line) }!!
                val m = regex.find(line)!!
                { display: Array<Array<Char>> -> op(display, m.groupValues[1].toInt(), m.groupValues[2].toInt()) }
            }
            .fold(startDisplay) { display, operation ->
                printDisplay(display)
                operation(display)
            }

    val pixels = finalDisplay.map { it.count { it == '#' } }.sum()
    println("Pixels: $pixels")
}

fun rect(display: Array<Array<Char>>, a: Int, b: Int): Array<Array<Char>> {
    val rotated = copy(display)
    for (x in 0..a - 1)
        for (y in 0..b - 1)
            rotated[y][x] = '#'
    return rotated
}

fun rotateCol(display: Array<Array<Char>>, col: Int, amount: Int): Array<Array<Char>> {
    val rotated = copy(display)
    for (y in 0..HEIGHT - 1)
        rotated[y][col] = display[mod((y - amount), HEIGHT)][col]
    return rotated
}

fun rotateRow(display: Array<Array<Char>>, row: Int, amount: Int): Array<Array<Char>> {
    val rotated = copy(display)
    for (x in 0..WIDTH - 1)
        rotated[row][x] = display[row][mod((x - amount), WIDTH)]
    return rotated
}

fun mod(x: Int, N: Int): Int {
    return if (x < 0) ((x % N) + N) % N else x % N
}

fun printDisplay(display: Array<Array<Char>>) {
    for (y in 0..HEIGHT - 1) {
        for (x in 0..WIDTH - 1)
            print(display[y][x])
        println()
    }
    println()
}

fun copy(display: Array<Array<Char>>): Array<Array<Char>> {
    return Array(HEIGHT, { i -> Array(WIDTH, { j -> display[i][j] }) })
}