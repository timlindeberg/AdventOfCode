import java.io.File
import java.util.*

val registers = Array<Int>('z'.toInt(), { i -> if(i == 'c'.toInt()) 1 else 0 })
var PC = 0

val num = "\\d+".toRegex()
val operations = mapOf(
        "cpy" to ::cpy,
        "jnz" to ::jnz,
        "inc" to ::inc,
        "dec" to ::dec
)

fun main(a: Array<String>) {
    val program = File("input/twelve.txt").readLines().map {
        val s = it.split(" ")
        val command = operations[s[0]]!!
        val args = s.drop(1)
        return@map { command(args) }
    }

    while(PC >= 0 && PC < program.size)
        program[PC++]()

    println(registers['a'.toInt()])
}

fun cpy(args: List<String>) {
    val from = args[0]
    val to = args[1]
    if (num.matches(from)) {
        registers[to[0].toInt()] = from.toInt()
        return
    }
    registers[to[0].toInt()] = registers[from[0].toInt()]
}

fun jnz(args: List<String>) {
    val x = args[0]
    val v = if (num.matches(x)) x.toInt() else registers[x[0].toInt()]
    if (v == 0)
        return

    val y = args[1]
    PC += y.toInt() - 1
}

fun inc(args: List<String>) {
    val x = args[0]
    registers[x[0].toInt()]++
}

fun dec(args: List<String>) {
    val x = args[0]
    registers[x[0].toInt()]--
}