package mammals

//from https://medium.com/kotlin-thursdays/introduction-to-kotlin-generics-part-2-9428963bb96b

import kotlin.random.*
import kotlin.reflect.*

sealed class Mammal(val name: String) {
    open fun eat() {}
    open fun sleep() {}
    open fun swim(){
        println("${name.toUpperCase()} CAN SWIM")
    }
    open fun relief() {}
    open fun action() { eat(); swim (); sleep() }
}


class Sloth(val slothName: String,
            val isTwoFingered: Boolean,
            var slothWeight: Int
            ): Mammal(slothName) {

    override fun relief() {
        val oldWeight = slothWeight
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
        println("pandaName = ${pandaName} eats shoots")
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
        else -> 100
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

fun mainMammals() {
    val mammalCrew = listOf(
            Sloth("Jerry", false, 15),
            Sloth("Bae", true, 12),
            Sloth("Alex", false, 20),
            Panda("Tegan"),
            Manatee("Manny")
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
}