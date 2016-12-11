import java.util.*

// The first floor contains a strontium generator, a strontium-compatible microchip, a plutonium generator, and a plutonium-compatible microchip.
// The second floor contains a thulium generator, a ruthenium generator, a ruthenium-compatible microchip, a curium generator, and a curium-compatible microchip.
// The third floor contains a thulium-compatible microchip.
// The fourth floor contains nothing relevant.

val visited = HashSet<State>()


fun main(args: Array<String>) {
    // F4: .  .  .  .  .  .
    // F3: .  TM .  .  .  .
    // F2: .  TG RG RM CG CM
    // F1: E  SG SM PG PM .

    // Constraints: Måste vara tillsammans (M och G)

    val initState = State()
    initState.floors[0].add(Elevator())
    initState.floors[0].add(Generator('S'))
    initState.floors[0].add(Microchip('S'))
    initState.floors[0].add(Generator('P'))
    initState.floors[0].add(Microchip('P'))

    initState.floors[1].add(Generator('T'))
    initState.floors[1].add(Generator('R'))
    initState.floors[1].add(Microchip('R'))
    initState.floors[1].add(Generator('C'))
    initState.floors[1].add(Microchip('C'))

    initState.floors[2].add(Microchip('T'))

    println(initState)


}

interface Obj
data class Microchip(val type: Char) : Obj
data class Generator(val type: Char) : Obj
class Elevator : Obj {
    override fun toString(): String {
        return "Elevator"
    }
}


data class State(val floors: MutableList<MutableList<Obj>> = ArrayList<MutableList<Obj>>()) {
    init {
        for (i in 0..3)
            floors.add(ArrayList<Obj>())
    }

    override fun toString(): String {
        return floors.withIndex().reversed().map {
            val (index, floor) = it
            "F$index: $floor"
        }.joinToString("\n")
    }

    val currentFloor: Int
        get() = floors.indexOfFirst { it.any { it is Elevator } }

    fun validMoves(): List<State> {
        val states = ArrayList<State>()

        // Go up:
        if(currentFloor != 3){
        }

        // Go down:
    }

}