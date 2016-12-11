import java.io.File
import java.util.*

var bots = ArrayList<Bot>()
var outputs = ArrayList<MutableList<Int>>()

fun main(args: Array<String>) {
    val input = File("input/ten.txt").readText()
    parseData(input)

    var bot = bots.find { it.microchips.size >= 2 }
    while(bot != null){
        bot.microchips.sort()
        if(bot.microchips.contains(61) && bot.microchips.contains(17))
            println("Bot: " + bots.indexOf(bot))
        giveTo(bot.microchips.removeAt(0), bot.lower!!)
        giveTo(bot.microchips.removeAt(0), bot.higher!!)
        bot = bots.find { it.microchips.size >= 2 }
    }
    println(outputs[0][0] * outputs[1][0]  * outputs[2][0] )
}

fun parseData(input: String){
    val numBots = """bot (\d+)""".toRegex().findAll(input).map { it.groupValues[1].toInt() }.max()!!
    val numOutputs = """output (\d+)""".toRegex().findAll(input).map { it.groupValues[1].toInt() }.max()!!

    bots = ArrayList<Bot>()
    for (i in 0..numBots)
        bots.add(Bot())

    outputs = ArrayList<MutableList<Int>>()
    for (i in 0..numBots - 1)
        outputs.add(ArrayList<Int>())

    val giveInstructions = """bot (\d+) gives low to (bot|output) (\d+) and high to (bot|output) (\d+)""".toRegex()
    val initInstruction = """value (\d+) goes to bot (\d+)""".toRegex()

    for(line in input.split("\n")){
        var m = giveInstructions.find(line)
        if(m != null){
            val g = m.groupValues
            val from = g[1].toInt()
            val lowType = g[2]
            val lowNum = g[3].toInt()
            val highType = g[4]
            val highNum = g[5].toInt()

            val fromBot = bots[from]
            fromBot.higher = Pair(highType, highNum)
            fromBot.lower = Pair(lowType, lowNum)
            continue
        }

        m = initInstruction.find(line)
        if(m != null){
            val g = m.groupValues
            val v = g[1].toInt()
            val to = g[2].toInt()
            bots[to].microchips.add(v)
        }
    }
}

fun giveTo(microchip: Int, to: Pair<String, Int>){
    val index = to.second
    if(to.first == "bot")
        bots[index].microchips.add(microchip)
    else
        outputs[index].add(microchip)
}

data class Bot(val microchips: MutableList<Int> = ArrayList(), var higher: Pair<String, Int>? = null, var lower: Pair<String, Int>? = null)
