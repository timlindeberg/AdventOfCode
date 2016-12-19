import org.magicwerk.brownies.collections.GapList
import java.util.*

/**
 * Created by Tim Lindeberg on 12/19/2016.
 */

fun main(args: Array<String>) {
    val elves = LinkedList<Int>()
    elves += 1..3014387

    var it = elves.listIterator()
    var remove = false
    while (elves.size > 1) {
        if (!it.hasNext()) {
            it = elves.listIterator()
            continue
        }

        it.next()
        if (remove) it.remove()

        remove = !remove
    }
    println(elves.first)
}