import java.util.*

// The first floor contains a strontium generator, a strontium-compatible microchip, a plutonium generator, and a plutonium-compatible microchip.
// The second floor contains a thulium generator, a ruthenium generator, a ruthenium-compatible microchip, a curium generator, and a curium-compatible microchip.
// The third floor contains a thulium-compatible microchip.
// The fourth floor contains nothing relevant.

val visited = TreeSet<State>( { s1, s2 ->
})

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

val NUM_FLOORS = 4

fun main(args: Array<String>) {
    // F4: .  .  .  .  .  .
    // F3: .  TM .  .  .  .
    // F2: .  TG RG RM CG CM
    // F1: E  SG SM PG PM .

    // Constraints: Måste vara tillsammans (M och G)

    val initFloors = ArrayList<MutableSet<Obj>>()
    for (i in 0..NUM_FLOORS - 1)
        initFloors.add(HashSet<Obj>())
    
    initFloors[0].add(ELEVATOR)
    initFloors[0].add(SG)
    initFloors[0].add(SM)
    initFloors[0].add(PG)
    initFloors[0].add(PM)

    initFloors[1].add(TG)
    initFloors[1].add(RG)
    initFloors[1].add(RM)
    initFloors[1].add(CG)
    initFloors[1].add(CM)

    initFloors[2].add(TM)

    val victoryFloors = ArrayList<MutableSet<Obj>>()
    for (i in 0..NUM_FLOORS - 1)
        victoryFloors.add(HashSet<Obj>())

    victoryFloors[3].add(ELEVATOR)
    victoryFloors[3].add(SG)
    victoryFloors[3].add(SM)
    victoryFloors[3].add(PG)
    victoryFloors[3].add(PM)
    victoryFloors[3].add(TG)
    victoryFloors[3].add(RG)
    victoryFloors[3].add(RM)
    victoryFloors[3].add(CG)
    victoryFloors[3].add(CM)
    victoryFloors[3].add(TM)

    val initState = State(initFloors)
    val victoryState = State(victoryFloors)

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

data class State(val floors: MutableList<MutableSet<Obj>>) : Comparable<State> {

    override fun compareTo(other: State): Int {
        pairs.hashCode() - s2.pairs().hashCode()
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val pairs: Set<Set<Int>>

    init {
        val map = HashMap<Char, Int>()
        pairs = HashSet<Set<Int>>()
        for (i in 0..floors.size - 1) {
            for (obj in floors[i]) {
                val v = map[obj.type]
                if (v != null)
                    pairs.add(setOf(v, i))
                else
                    map[obj.type] = i
            }
        }
    }

    fun repr() {

    }

    fun pairs(): Set<Set<Int>> {
        val map = HashMap<Char, Int>()
        val pairs = HashSet<Set<Int>>()
        for (i in 0..floors.size - 1) {
            for (obj in floors[i]) {
                val v = map[obj.type]
                if (v != null)
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

        for (obj1 in fromFloor) {
            val newState = newState.copy()

            newState.moveToFloor(obj1, from, to)
            states.add(newState)
            for (obj2 in fromFloor) {
                val items = setOf(obj1, obj2)
                if (obj1 == obj2 || added.contains(items))
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