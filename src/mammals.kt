////from https://medium.com/kotlin-thursdays/introduction-to-kotlin-generics-part-2-9428963bb96b
//
//import kotlin.random.*
//import kotlin.reflect.*
//
//sealed class Mammal(val name: String) {
//    fun eat() {}
//    fun sleep() {}
//    fun swim(){
//        println("${name.toUpperCase()} CAN SWIM")
//    }
//    open fun relief() {}
//}
//
//
//data class Sloth(val slothName: String,
//                 val isTwoFingered: Boolean,
//                 var slothWeight: Int): Mammal(slothName) {
//    override fun relief() {
//        val oldWeight = slothWeight
//        val weightShed = Random.nextInt(0, slothWeight/3)
//        val newWeight = slothWeight - weightShed
//        println("${slothName.toUpperCase()} FINALLY WENT THIS WEEK")
//        println("\tOld weight: $oldWeight \t|\t New weight: $newWeight")
//    }
//
//    fun numFingers() {
//        println("fingers=${(if (this.isTwoFingered) 2 else 3)}")
//    }
//
//}
//
//data class Manatee(val manateeName: String): Mammal(manateeName)
//
//data class Panda(val pandaName: String) : Mammal(pandaName)
//
//// Unit means we're passing in Kotlin a block function w/ no inputs/outputs
//fun slothActivity(sloth: Sloth, action: Unit) {
//    sloth.run {
//        action
//    }
//}
//
//fun Mammal.vertebraeCount(): Int {
//    return when (this) {
//        is Manatee -> 6
//        is Sloth -> 10
//        else -> 7
//    }
//}
//
//fun Mammal.knownSpeciesCount(): Int {
//    return when (this) {
//        is Panda -> 2
//        is Manatee -> 3
//        is Sloth -> 6
//        else -> 100
//    }
//}
//
//fun Mammal.isEndangered(): Boolean {
//    return when (this) {
//        is Panda -> true
//        is Manatee -> false // upgraded to "threatened"
//        is Sloth -> true
//        else -> false
//    }
//}
//
//fun mammalFactCheck(mammal: Mammal, factCheck: KFunction1<Mammal, Int>): Int {
//    println("$mammal = ${factCheck(mammal)}")
//    return factCheck(mammal)
//}
//
//fun mainMammals() {
//    val slothCrew = listOf(
//            Sloth("Jerry", false, 15),
//            Sloth("Bae", true, 12),
//            Sloth("Alex", false, 20),
//            Panda("Tegan"),
//            Manatee("Manny")
//    )
//
////    slothCrew.forEach { it ->
////        slothActivity(it, it.swim() ) /// it.swim())
////        slothActivity(it, it.relief())
//
//    slothCrew.forEach {
//        mammalFactCheck(it, Mammal::vertebraeCount)
//        mammalFactCheck(it, Mammal::knownSpeciesCount)
//    }
//}