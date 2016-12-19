import java.io.File

fun main(args: Array<String>) {
    val seed = File("input/sixteen.txt").readText()
    val diskSize = 35651584
    val sb = StringBuilder(diskSize)
    sb.append(seed)

    while(sb.length < diskSize) {
        val b = sb.reversed().map { if(it == '0') '1' else '0'}.joinToString("")
        sb.append('0' + b)
    }

    val data = sb.substring(0, diskSize)

    var checksum = StringBuilder(data)
    do {
        val sb = StringBuilder(checksum.length / 2)
        var i = 0
        while(i < checksum.length) {
            val c = if(checksum[i] == checksum[i + 1]) '1' else '0'
            sb.append(c)
            i += 2
        }
        checksum = sb
    } while(checksum.length % 2 == 0)
    println(checksum)
}