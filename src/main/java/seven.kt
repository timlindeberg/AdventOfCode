import java.io.File
import java.util.*

fun main(args: Array<String>) {
    val r = """(.+?)\[(.+?)\]|(.*)""".toPattern()
    val num = File("src/six/input.txt").readLines()
            .map {
                val supernet = ArrayList<String>()
                val hypernet = ArrayList<String>()
                val matcher = r.matcher(it)
                while (matcher.find()) {
                    val m1 = matcher.group(1)
                    if(m1.isNullOrEmpty()){
                        // Only one match - (.*)
                        supernet.add(matcher.group(0))
                    }else{
                        // Two matches - (.+?)\[(.+?)\]
                        supernet.add(m1)
                        hypernet.add(matcher.group(2))
                    }
                }
                Pair(supernet, hypernet)
            }
            .filter {
                val (supernet, hypernet) = it
                val babas = supernet.flatMap(::findABAs).map { aba -> "" + aba[1] + aba[0] + aba[1] }
                hypernet.any {
                    babas.any { baba -> it.contains(baba) }
                }
            }.size
    println(num)
}

fun findABAs(s: String): List<String> {
    return s.indices.filter { i ->
        i + 2 < s.length &&
                s[i] == s[i + 2] &&
                s[i] != s[i + 1]
    }.map { s.substring(it, it + 3) }
}

fun isABBA(s: String): Boolean {
    return s.indices.any { i ->
        i + 3 < s.length &&
                s[i] == s[i + 3] &&
                s[i + 1] == s[i + 2] &&
                s[i] != s[i + 1]
    }
}