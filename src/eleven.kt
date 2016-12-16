import java.util.*

// The first floor contains a strontium generator, a strontium-compatible microchip, a plutonium generator, and a plutonium-compatible microchip.
// The second floor contains a thulium generator, a ruthenium generator, a ruthenium-compatible microchip, a curium generator, and a curium-compatible microchip.
// The third floor contains a thulium-compatible microchip.
// The fourth floor contains nothing relevant.

val visited = TreeSet<State>()

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

    val initState = State()
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

    val victoryState = State()
    victoryState.floors[3].add(ELEVATOR)
    victoryState.floors[3].add(SG)
    victoryState.floors[3].add(SM)
    victoryState.floors[3].add(PG)
    victoryState.floors[3].add(PM)
    victoryState.floors[3].add(TG)
    victoryState.floors[3].add(RG)
    victoryState.floors[3].add(RM)
    victoryState.floors[3].add(CG)
    victoryState.floors[3].add(CM)
    victoryState.floors[3].add(TM)

    println(initState.pairs())
    println(victoryState.pairs())

    val Q = ArrayDeque<State>()
    val dist = HashMap<State, Int>()

    var d = 0
    Q.add(victoryState)
    dist[victoryState] = 0
    while (Q.isNotEmpty()) {
        val state = Q.pop()
        if (visited.contains(victoryState))
            continue

        if (state == initState)
            break

        if (dist[state] ?: 0 > d) {
            d = dist[state]!!
            println(d)
        }

        //println(state)

        state.validMoves()
                .filter { !visited.contains(state) }
                .forEach { newState ->
                    Q.add(newState)
                    dist[newState] = (dist[state] ?: 0) + 1
                }
    }
    println("Min distance: $dist")
}

interface Obj {
    val type: Char
}

data class Microchip(override val type: Char) : Obj {
    override fun toString(): String {
        return type + "M"
    }
}

data class Generator(override val type: Char) : Obj {
    override fun toString(): String {
        return type + "G"
    }
}
data class Elevator(override val type: Char = 'E') : Obj {
    override fun toString(): String {
        return "E"
    }
}

data class State(val floors: MutableList<MutableSet<Obj>> = ArrayList<MutableSet<Obj>>()) {
    init {
        if (floors.isEmpty())
            for (i in 0..3)
                floors.add(HashSet<Obj>())
    }

    fun pairs(): Set<Set<Int>>{
        val map = HashMap<Char, Int>()
        val pairs = HashSet<Set<Int>>()
        for(i in 0..floors.size - 1) {
            for(obj in floors[i]) {
                val v = map[obj.type]
                if(v != null)
                    pairs.add(setOf(v, i))
                else
                    map[obj.type] = i
            }
        }
        return pairs
    }

    override fun toString(): String {
        return floors.withIndex().reversed().map {
            val (index, floor) = it
            "F$index: $floor"
        }.joinToString("\n")
    }

    fun moveToFloor(obj: Obj, from: Int, to: Int) {
        floors[from].remove(obj)
        floors[to].add(obj)
    }

    fun isValid(): Boolean {
        return floors.none { floor ->
            val microchips = floor.filter { it is Microchip }
            microchips.any { microchip ->
                // Generator of same type
                if (floor.any { it is Generator && it.type == microchip.type })
                    return@any false

                // Any other generator
                floor.any { it is Generator }
            }
        }
    }

    fun addMoves(from: Int, to: Int, states: MutableList<State>) {
        val newState = copy()

        val fromFloor = newState.floors[from]
        val toFloor = newState.floors[to]

        fromFloor.remove(ELEVATOR)
        toFloor.add(ELEVATOR)

        states.add(newState)

        val added = HashSet<Set<Obj>>()

        for(obj1 in fromFloor){
            val newState = newState.copy()

            newState.moveToFloor(obj1, from, to)
            states.add(newState)
            for(obj2 in fromFloor){
                val items = setOf(obj1, obj2)
                if(obj1 == obj2 || added.contains(items))
                    continue

                added.add(items)

                val newState = newState.copy()
                newState.moveToFloor(obj2, from, to)
                states.add(newState)
            }
        }
        /*
        println("--------------- STATE ---------------")
        println(this)
        println("--------------- MOVES ---------------")
        println(states.joinToString("\n"))
        */
    }

    fun validMoves(): List<State> {
        val states = ArrayList<State>()

        val currentFloor = floors.indexOfFirst { it.contains(ELEVATOR) }

        // Up
        if (currentFloor != 3)
            addMoves(currentFloor, currentFloor + 1, states)

        // Down
        if (currentFloor != 0)
            addMoves(currentFloor, currentFloor - 1, states)

        return states.filter(State::isValid)
    }

    fun copy(): State {
        return State(floors.mapTo(ArrayList<MutableSet<Obj>>()) { HashSet<Obj>(it) })
    }


}