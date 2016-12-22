import java.io.File
import java.util.*

fun main(args: Array<String>) {
    val one = File("input/twentyone.txt").readLines()
            .map(::parseCommand)
            .fold("abcdefgh") { s, op -> op(s) }
    println("one: $one")

    val two = File("input/twentyone.txt").readLines()
            .map(::parseCommand)
            .reversed()
            .fold("fbgdceah") { s, op -> op.revert(s) }
    println("one: $two")


}

private val PARSERS = arrayOf(
        """(swap) (position|letter) (.+) with (?:position|letter) (.+)""".toRegex(),
        """(rotate) (left|right|based on position of letter) (.+?)(?: steps?)?""".toRegex(),
        """(reverse) positions (\d+) through (\d+)""".toRegex(),
        """(move) position (\d+) to position (\d+)""".toRegex()
)

fun parseCommand(line: String): StringOperation {
    val parser = PARSERS.find { it.matches(line) }!!

    val values = parser.find(line)!!.groupValues
    when (values[1]) {
        "swap" -> {
            val x = values[3]
            val y = values[4]
            if (values[2] == "position")
                return SwapPos(x.toInt(), y.toInt())
            return SwapChars(x[0], y[0])
        }
        "rotate" -> {
            val x = values[3]
            if (values[2].startsWith("based"))
                return RotateChar(x[0])

            val dir = if(values[2] == "right") 1 else -1
            return Rotate(dir * x.toInt())
        }
        "reverse" -> {
            return Reverse(values[2].toInt(), values[3].toInt())
        }
        "move" -> {
            return Move(values[2].toInt(), values[3].toInt())
        }
        else -> throw Exception()
    }
}

interface StringOperation {
    operator fun invoke(s: String): String
    fun revert(s: String): String
}

class SwapPos(val i: Int, val j: Int) : StringOperation {

    override fun invoke(s: String): String {
        val cp = s.toCharArray()
        val tmp = cp[i]
        cp[i] = cp[j]
        cp[j] = tmp
        return String(cp)
    }

    override fun revert(s: String): String {
        return SwapPos(j, i)(s)
    }

}

class SwapChars(val a: Char, val b: Char) : StringOperation {

    override fun revert(s: String): String {
        return SwapChars(b, a)(s)
    }

    override fun invoke(s: String): String {
        return s.map {
            when (it) {
                a -> b
                b -> a
                else -> it
            }
        }.joinToString("")
    }
}

class Rotate(val num: Int) : StringOperation {
    override fun revert(s: String): String {
        return Rotate(-num)(s)
    }

    override fun invoke(s: String): String {
        return s.indices.map { s[mod(it - num, s.length)] }.joinToString("")
    }
}

class RotateChar(val c: Char) : StringOperation {

    // Hack since I can't do math but we know the length of the string
    var reversedPos = mapOf(
            1 to 0,
            3 to 1,
            5 to 2,
            7 to 3,
            2 to 4,
            4 to 5,
            6 to 6,
            0 to 7
    )

    override fun revert(s: String): String {
        val index = s.indexOf(c)
        val startIndex = reversedPos[index]!!
        val times = startIndex - index
        return Rotate(times)(s)
    }

    override fun invoke(s: String): String {
        val index = s.indexOf(c)
        var times = 1 + index
        if (index >= 4)
            times++
        return Rotate(times)(s)
    }
}

class Reverse(val i: Int, val j: Int) : StringOperation {

    override fun revert(s: String): String {
        return invoke(s)
    }

    override fun invoke(s: String): String {
        val j = j + 1
        return s.substring(0, i) + s.substring(i, j).reversed() + s.substring(j, s.length)
    }
}

class Move(val i: Int, val j: Int) : StringOperation {

    override fun revert(s: String): String {
        return Move(j, i).invoke(s)
    }

    override fun invoke(s: String): String {
        val res = ArrayList<Char>(s.length)
        for (c in s) res.add(c)
        val c = res.removeAt(i)
        val index = if (j > res.size) res.size - 1 else j
        res.add(index, c)
        return res.joinToString("")
    }
}