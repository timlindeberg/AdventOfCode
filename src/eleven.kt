import java.util.*

// The first floor contains a strontium generator, a strontium-compatible microchip, a plutonium generator, and a plutonium-compatible microchip.
// The second floor contains a thulium generator, a ruthenium generator, a ruthenium-compatible microchip, a curium generator, and a curium-compatible microchip.
// The third floor contains a thulium-compatible microchip.
// The fourth floor contains nothing relevant.

val visited = HashSet<State>()

val ELEVATOR = Elevator()
val SG = Generator('S')
val SM = Microchip('S')
val PG = Generator('P')
val PM = Microchip('P')
val TG = Generator('T')
val TM = Microchip('T')
val RG = Generator('R')
val RM = Microchip('R')
val CG = Generator('C')
val CM = Microchip('C')

fun main(args: Array<String>) {
    // F4: .  .  .  .  .  .
    // F3: .  TM .  .  .  .
    // F2: .  TG RG RM CG CM
    // F1: E  SG SM PG PM .

    // Constraints: Måste vara tillsammans (M och G)

    val floors = ArrayList<MutableList<Obj>>()
    for (i in 0..3)
        floors.add(ArrayList<Obj>())
    val initState = State(floors)
    initState.floors[0].add(ELEVATOR)
    initState.floors[0].add(SG)
    initState.floors[0].add(SM)
    initState.floors[0].add(PG)
    initState.floors[0].add(PM)

    initState.floors[1].add(TG)
    initState.floors[1].add(RG)
    initState.floors[1].add(RM)
    initState.floors[1].add(CG)
    initState.floors[1].add(CM)

    initState.floors[2].add(TM)
}

interface Obj {
    val type: Char

    fun linkedWith(obj: Obj): Boolean
}
data class Microchip(override val type: Char) : Obj {
    override fun linkedWith(obj: Obj): Boolean {
        return obj is Generator && obj.type == type
    }

}
data class Generator(override val type: Char) : Obj {
    override fun linkedWith(obj: Obj): Boolean {
        return obj is Microchip && obj.type == type
    }
}
class Elevator : Obj {

    override val type: Char = 'E'

    override fun toString(): String {
        return "Elevator"
    }

    override fun equals(other: Any?): Boolean {
        return other != null && other is Elevator
    }

    override fun hashCode(): Int {
        return 31
    }
    override fun linkedWith(obj: Obj): Boolean {
        return false
    }
}


data class State(val floors: MutableList<MutableList<Obj>>) {
    val currentFloor: Int

    init {
        currentFloor = floors.indexOfFirst { it.any { it is Elevator } }
    }

    override fun toString(): String {
        return floors.withIndex().reversed().map {
            val (index, floor) = it
            "F$index: $floor"
        }.joinToString("\n")
    }


    fun remove(obj: Obj): Int {
        val floor = floors.indexOfFirst { it.contains(obj) }
        if(floor == -1)
            return  -1

        floors[floor].remove(obj)
        return floor
    }

    fun validMoves(): List<State> {
        val states = ArrayList<State>()

        // Go up:
        if(currentFloor != 3){
            val s = copy()
            val floor = s.floors[currentFloor]
            val nextFloor = s.floors[currentFloor + 1]
            floor.remove(ELEVATOR)
            nextFloor.add(ELEVATOR)
            val movableObjects = floor.filter { obj ->
                when(obj){
                    is Generator -> true
                    is Microchip -> true
                    else -> false
                }
            }

            for(obj1 in movableObjects){
                for(obj2 in movableObjects) {
                    if(obj1 == obj2)
                        continue
                }
            }
        }

        // Go down:
        return emptyList()
    }

    fun copy(): State {
        return State(floors.mapTo(ArrayList<MutableList<Obj>>()) { ArrayList<Obj>(it) })
    }


}