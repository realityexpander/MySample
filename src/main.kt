

public var foo: String = "hello"

fun main(args: Array<String>) {

    mainBattleGame()

    println()

//    //var bev = readLine() // wait for keybd input
//    var bev = "chris athanas"
//    val swords : Int? = 2
//    bev.let {
//        bev = it.split(" ")
//                .asSequence()
//                .map{ s -> s.capitalize()}
//                .joinToString(" ")
//    } //?: println("i cant do that without crashing")
//
//
//    try {
//        swords ?: throw UnskilledSwordJugglerException()
//    } catch(e:Exception){
//        println(e)
//    }
//
//    println(bev)
//
//    val player = Player()
//    player.name = "Jimbo"
//    player.castFireball(3)
//
//    val s = Sword("Abcdefg")
//    println(s.name)
//
//    val list = addToList()
//    println(list)


//    // Linked list implementation
//    runLinkedListTesting()



    val intArray = intArrayOf(4, 2, 3, 1)
    // before sorting
    println(intArray.joinToString()) //joinToString()) // 4, 3, 2, 1
    intArray.sortDescending()
    // after sorting
    println(intArray.joinToString()) // 1, 2, 3, 4

    println("\n\n")

    //doTeacherStudent()
}

private fun runLinkedListTesting() {
    val ll = LinkedList<String>()
    ll.append("John")
    println(ll)
    ll.append("Carl")
    println(ll)
    ll.append("Zack")
    println(ll)
    ll.append("Tim")
    println(ll)
    ll.append("Steve")
    println(ll)
    ll.append("Peter")
    println(ll)
    print("\n\n")
    println("first item: ${ll.first()?.value}")
    println("last item: ${ll.last()?.value}")
    println("second item: ${ll.first()?.next?.value}")
    println("penultimate item: ${ll.last()?.previous?.value}")
    println("4th item: ${ll.nodeAtIndex(3)?.value}")
    println("\nthe list has ${ll.count()} items: $ll")
}


class UnskilledSwordJugglerException :
        IllegalStateException("Player cannot juggle swords")

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
