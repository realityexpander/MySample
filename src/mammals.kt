
sealed class Mammal(val name: String) {
    fun eat() {}
    fun sleep() {}
    fun swim(){
        println("${name.toUpperCase()} CAN SWIM")
    }
    open fun relief() {}
}


data class Sloths(val slothName: String,
                  val isTwoFingered: Boolean,
                  var slothWeight: Int): Mammal(slothName) {
    override fun relief() {
        val oldWeight = slothWeight
        val weightShed = 3 //Random.nextInt(0, slothWeight/3)
        val newWeight = slothWeight.minus(weightShed)
        println("${slothName.toUpperCase()} FINALLY WENT THIS WEEK")
        println("\tOld weight: $oldWeight \t|\t New weight: $newWeight")
    }
}

// Unit means we're passing in Kotlin a block function w/ no inputs/outputs
fun slothActivity(sloth: Sloths, action: Unit) {
    sloth.run { action }
}

fun mainMammals() {
    val slothCrew = listOf(
            Sloths("Jerry", false, 15),
            Sloths("Bae", true, 12),
            Sloths("Alex", false, 20)
    )

    slothCrew.forEach {
        slothActivity(it, it.swim())
        slothActivity(it, it.relief())
    }
}