import org.magicwerk.brownies.collections.GapList
import java.io.File

fun main(args: Array<String>) {
    val numElves = File("input/nineteen.txt").readText().toInt()
    val elves = GapList<Int>()
    elves += 1 .. numElves

    while (elves.size > 1) {
        var i = 0
        while(i < elves.size){
            val j = (i + elves.size / 2) % elves.size
            elves.removeAt(j)
            if(j > i)
                i++
        }
    }
    println(elves)
}