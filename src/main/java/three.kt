data class Triangle(val a: Int, val b: Int, val c: Int)
fun three(input: String) {
    val r = """(\d+)\s*(\d+)\s*(\d+)""".toRegex()
    val t = input.split("\n").map {
        val groups = r.matchEntire(it.trim())!!.groups
        Triangle(groups[1]!!.value.toInt(), groups[2]!!.value.toInt(), groups[3]!!.value.toInt())
    }

    val triangles = (0..t.size step 3).flatMap { i ->
        if (i >= t.size)
            return@flatMap emptyList<Triangle>()
        val t1 = t[i]
        val t2 = t[i + 1]
        val t3 = t[i + 2]
        listOf(Triangle(t1.a, t2.a, t3.a), Triangle(t1.b, t2.b, t3.b), Triangle(t1.c, t2.c, t3.c))
    }

    val numValid = triangles.filter {
        val (a, b, c) = it
        a + b > c && a + c > b && b + c > a
    }.size

    println(numValid)
}