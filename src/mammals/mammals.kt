package mammals

//from https://medium.com/kotlin-thursdays/introduction-to-kotlin-generics-part-2-9428963bb96b

import kotlin.random.*

sealed class Mammal(val name: String) {
    open fun eat() {}
    open fun sleep() {}
    open fun swim(){
        println("${name.toUpperCase()} CAN SWIM")
    }
    open fun relief() {}
    open fun action() { eat(); swim (); sleep() }
    var weight = 10
}


class Sloth(val slothName: String,
            val isTwoFingered: Boolean,
            var slothWeight: Int
            ): Mammal(slothName) {

    override fun relief() {
        val oldWeight = slothWeight
        weight = slothWeight
        val weightShed = Random.nextInt(0, slothWeight/3)
        val newWeight = slothWeight - weightShed
        println("${slothName.toUpperCase()} FINALLY WENT THIS WEEK")
        println("\tOld weight: $oldWeight \t|\t New weight: $newWeight")
    }

    fun numFingers() {
        println("fingers=${(if (this.isTwoFingered) 2 else 3)}")
    }

}

data class Manatee(val manateeName: String): Mammal(manateeName) {
    override fun swim() {
        super.swim()
        println("$manateeName's more of a floater")
    }
}

data class Panda(val pandaName: String) : Mammal(pandaName) {
    override fun eat() {
        super.eat()
        println("pandaName = $pandaName eats shoots")
    }
}

data class Tiger(val tigerName: String) : Mammal(tigerName) {
    override fun eat() {
        super.eat()
        println("tigerName = $tigerName eats shoots")
    }
}

fun slothActivity(sloth: Sloth, action: Unit) {
    sloth.run {
        action
    }
}

fun mammalActivity(mammal: Mammal, action: Unit) {
    mammal.run {
        action
    }
}

fun Mammal.vertebraeCount(i:Int): Int {
    return when (this) {
        is Manatee -> 6
        is Sloth -> 10
        else -> 7
    }
}

fun Mammal.knownSpeciesCount(i:Int): Int {
    return when (this) {
        is Panda -> 2
        is Manatee -> 3
        is Sloth -> 6
        is Tiger -> 2000
//        else -> 100 // bc Mammal is a sealed class
    }
}

fun Mammal.isEndangered(): Boolean {
    return when (this) {
        is Panda -> true
        is Manatee -> false // upgraded to "threatened"
        is Sloth -> true
        else -> false
    }
}

//fun mammalFactCheck(mammal: Mammal, factCheck: KFunction2<Mammal, Int, Int>): Int {
fun mammalFactCheck(mammal: Mammal, factCheck: (Mammal, Int)->Int ): Int {
    println("mammal = [${mammal.javaClass.simpleName}.${mammal.name}], factCheck = [${factCheck(mammal,5)}]")
    return factCheck(mammal, 5)
}

inline fun <reified T: Mammal> printAnimalResultFiltered(
        list: List<Mammal>,
        factCheck: Mammal.(Int) -> Int ) {
    if (list.isNotEmpty()) {
        list.filterIsInstance<T>()
                .forEach {
                    println("${it.javaClass.name} - ${it.factCheck(5)}")
                }
    }
}

interface XY {
    var a:Int
    var b:Int

    companion object {
        var c: Int = 10
    }


}
class XYImpl(): XY {
    override var a: Int = 20
    override var b: Int = 10


    companion object {
        var c: Int = 20
    }
}

fun mainMammals() {
    val mammalCrew = listOf(
            Sloth("Jerry", false, 15),
            Sloth("Bae", true, 12),
            Sloth("Alex", false, 20),
            Panda("Tegan"),
            Manatee("Manny"),
            Tiger("Tigger"),
            Tiger("Jerome"),
            Panda("Sandy"),
            Manatee("SeaCow")
    )

    mammalCrew.forEach { it ->
        mammalActivity(it, it.eat() )
        mammalActivity(it, it.swim() )
        mammalActivity(it, it.relief() )
    }

    mammalCrew.forEach {
        mammalFactCheck(it, Mammal::vertebraeCount)
        mammalFactCheck(it, Mammal::knownSpeciesCount)
    }

    println("mammalCrew.filterIsInstance<Tiger>() = ${mammalCrew.filterIsInstance<Tiger>().joinToString{it.name}}")

    var mc = (mammalCrew as MutableList<Mammal>)
            .sortedBy { it.name }
            .associateTo(mutableMapOf<String, Int>(), {it -> Pair(it.name, it.weight) }  )
    println("mc.map: it.value = ${mc.map{ "name:${it.key}=weight:${it.value}" }.joinToString() }")



    printAnimalResultFiltered<Tiger>(mammalCrew, Mammal::knownSpeciesCount)

    // nullable experiments
    var al:List<Int?>? = listOf(null, 100,300)
    getX(al)

    var x: Int? = null
    var xNN = nullableIntToNonNullableInt(x)
    var y  = nonNullIntToNullableInt(xNN)

    var xy = XYImpl.c
//    x= null
//    println(x as Tiger)


    // Spread operator
    foo(strings = *arrayOf("a", "b", "c"))
    val a = arrayOf("a", "b", "c")
    val b = arrayOf("z", "x", *a, "o")
    foo(*b)


//    PassByReference()




}

private fun PassByReference() {
    class Logger() {
        var hours: Int = 0
    }

    class Simulate(val loggerRef: Logger) {
        fun doSomething() {
            loggerRef.hours += 1
        }
    }

    val log1 = Logger()
    val log2 = Logger()
    val sim1 = Simulate(log1)
    val sim2 = Simulate(log2)
    sim1.doSomething()
    sim2.doSomething()
    sim2.doSomething()
    println(log1.hours) //This should be 1
    println(log2.hours) //This should be 2
}


fun foo(vararg strings: String) = println("strings = ${strings.joinToString()}")



fun nullableIntToNonNullableInt(x: Int?): Int {
//    var result = x?.let{ x + 1 }

    var result:Int = x ?: 0
    return result
}

fun nonNullIntToNullableInt( x: Int): Int? {
    var result = (x as Int?)
    return result
}

fun getX( x: List<Int?>?): Int {
    var y = -1
    y = x?.get(0) ?: 0

    return y
}



