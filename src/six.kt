/**
 * Created by tlin on 06/12/16.
 */
fun main(args: Array<String>) {
    val lines = Files("input/six.txt").readLines()
    val charCount = Array(lines[0].length, { Array('z' - 'a' + 1, { 0 }) })

    for (line in lines)
        for ((i, char) in line.withIndex())
            charCount[i][char - 'a']++

    charCount
            .map { count -> count.indices.maxBy { count[it] }!! } // index of letter with largest count
            .map { (it + 'a'.toInt()).toChar() } // from index to actual character, e.g 0 -> a, 1 -> b
            .forEach(::print)
}