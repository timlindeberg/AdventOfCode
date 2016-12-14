import java.security.MessageDigest
import java.util.*

val md5 = MessageDigest.getInstance("MD5")


fun main(args: Array<String>) {
    val num = 100000
    val salt = "abc"
    val threes = ArrayList<Pair<Int, Char>>()
    val fives = ArrayList<Pair<Int, Char>>()

    for(i in 0..num){
        val s = MD5(salt + i)
        val (t, f) = charsRepeating(s, i)
        if(t != null)
            threes.add(t)
        if(f != null)
            fives.add(f)
    }

    println("Found threes and fives")
    val keys = ArrayList<Int>()

    for((indexThree, charThree) in threes){
        for((indexFive, charFive) in fives){
            if(indexFive < indexThree)
                continue

            if(indexFive > indexThree + 1000)
                break

            if(charThree == charFive){
                //println(charThree)
                //println("$indexThree: ${MD5(salt + indexThree)}")
                //println("$indexFive: ${MD5(salt + indexFive)}")
                keys.add(indexThree)
                println("Key: $indexThree")
                break
            }
        }
    }
    println(keys.withIndex().map{"${it.index + 1} -> ${it.value}"}.joinToString("\n"))
    println("Key 64: ${keys[63]}")
}

fun MD5(s: String): String {
    val b = md5.digest(s.toByteArray())
    return toHexString(b)
}

data class Repeats(val three: Pair<Int, Char>?, val five: Pair<Int, Char>?)
fun charsRepeating(s: String, index: Int): Repeats {
    var i = 0
    while (i < s.length) {
        var j = 0
        while (i + j + 1 < s.length && s[i] == s[i + j++ + 1]) { }

        if (j == 3) return Repeats(Pair(index, s[i]), null)
        if (j == 5) return Repeats(null, Pair(index, s[i]))

        i += Math.max(j, 1)
    }
    return Repeats(null, null)
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