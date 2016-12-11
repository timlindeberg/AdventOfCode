import java.util.*
import kotlin.comparisons.compareBy
import kotlin.comparisons.compareByDescending

data class Room(val name: String, val id: Int, val checksum: String)
data class CharData(val count: Int, val char: Char)

fun four(input: String) {
    val r = """([a-z\-]+)-(\d+)\[([a-z]+)\]""".toRegex()
    val rooms = input.split("\n").map {
        val groups = r.matchEntire(it.trim())!!.groups
        Room(groups[1]!!.value, groups[2]!!.value.toInt(), groups[3]!!.value)
    }

    println(rooms
            .filter { calculateChecksum(it) == it.checksum }
            .map { room ->
                val roomId = room.id
                val rotatedName = room.name
                        .map { transformLetter(it, room.id) }
                        .joinToString("")
                "$roomId: $rotatedName"
            }
            .joinToString("\n")
    )
}

fun calculateChecksum(room: Room): String {
    return room.name.replace("-", "")
            .groupBy { it }
            .map { CharData(it.value.size, it.key) }
            .sortedWith(Comparator { a, b ->
                if (a.count != b.count)
                    return@Comparator b.count - a.count
                a.char.compareTo(b.char)
            })
            .take(5)
            .map { it.char }
            .joinToString("")
}

fun transformLetter(char: Char, rotation: Int): Char {
    if (char == '-')
        return ' '

    val a = 'a'.toInt()
    val z = 'z'.toInt()
    return (a + ((char.toInt() + rotation - a) % (z - a + 1))).toChar()
}
