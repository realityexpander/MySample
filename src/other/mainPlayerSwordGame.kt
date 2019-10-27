package other


class UnskilledSwordJugglerDescriptionException : RuntimeException("other.Player cannot juggle swords")
typealias UnskilledSwordJugglerException = kotlin.RuntimeException
//        IllegalStateException("other.Player cannot juggle swords")

class Sword(_name: String) {
    var name = _name
        get() = "The legendary $field"
        set(value) {
            field = value.toLowerCase().reversed().capitalize()
        }

    init {
        name = _name
    }
}


fun mainPlayerSwordGame() {

//    var bev = readLine() // wait for keybd input
    var bev = "chris athanas"
    val swords: Int? = 2
    bev.let {
        bev = it.split(" ")
                .asSequence()
                .map { s -> s.capitalize() }
                .joinToString(" ")
    } //?: println("i cant do that without crashing")


    try {
//        swords ?: throw UnskilledSwordJugglerException()
        swords ?: throw UnskilledSwordJugglerDescriptionException()
    } catch (e: Exception) {
        println(e)
    }

    println(bev)

    val player = Player()
    player.name = "Jimbo"
    player.castFireball(3)

    val s = Sword("Abcdefg")
    println(s.name)
}