import java.io.File
import java.security.MessageDigest
import java.util.*
import java.util.regex.Pattern

val md5 = MessageDigest.getInstance("MD5")!!

val THREE = Pattern.compile("(\\w)\\1+\\1+")!!
val FIVE = Pattern.compile("(\\w)\\1+\\1+\\1+\\1+")!!

fun main(args: Array<String>) {
    val num = 30000
    val salt = File("input/fourteen.txt").readText()
    val threes = ArrayList<Pair<Int, Char>>()
    val fives = ArrayList<Pair<Int, Char>>()

    for(i in 0..num){
        val s = stretchedHash(salt + i)
        val three = charsRepeating(s, THREE)
        if(three != '.')
            threes.add(Pair(i,three))
        val five = charsRepeating(s, FIVE)
        if(five != '.')
            fives.add(Pair(i,five))
    }

    println("Found threes and fives")
    val keys = ArrayList<Int>()

    for((indexThree, charThree) in threes){
        for((indexFive, charFive) in fives){
            if(indexFive <= indexThree)
                continue

            if(indexFive > indexThree + 1000)
                break

            if(charThree == charFive){
                keys.add(indexThree)
                break
            }
        }
    }
    println(keys.withIndex().map{"${it.index + 1} -> ${it.value}"}.joinToString("\n"))
    println("Key 64: ${keys[63]}")
}

fun stretchedHash(s: String): String{
    return (1..2017).fold(s) { old, i -> MD5(old) }
}

fun MD5(s: String): String {
    val b = md5.digest(s.toByteArray())
    return toHexString(b)
}

fun charsRepeating(s: String, p: Pattern): Char {
    val m = p.matcher(s)
    return if (m.find()) return m.group(1)[0] else '.'
}

val hexArray = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
fun toHexString(bytes: ByteArray): String {
    val hexChars = CharArray(bytes.size * 2)
    var v: Int
    for (j in bytes.indices) {
        v = bytes[j].toInt() and 0xFF
        hexChars[j * 2] = hexArray[v / 16]
        hexChars[j * 2 + 1] = hexArray[v % 16]
    }
    return String(hexChars)
}