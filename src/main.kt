fun main(args: Array<String>) {
    //var bev = readLine() // wait for keybd input
    var bev = "chris athanas"
    val swords : Int? = 2
    bev.let {
        bev = it.split(" ")
                .asSequence()
                .map{ s -> s.capitalize()}
                .joinToString(" ")
    } //?: println("i cant do that without crashing")


    try {
        swords ?: throw UnskilledSwordJUgglerException()
    } catch(e:Exception){
        println(e)
    }

    println(bev)

    val player = Player()

    player.name = "Jimbo"
    player.castFireball(3)

    val s = Sword("Abcdefg")
    println(s.name)

    val list = addToList()
    println(list)


    // Linked list implementation
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

    ll.removeAtIndex(2)
    println(" 'remove 3rd item': ${ll.nodeAtIndex(2)?.value} items: $ll")
    println("the list has ${ll.count()} items: $ll")
    ll.nodeAtIndex(0)?.let { ll.removeNode(it) }
    println(" 'removing node 0', the list has ${ll.count()} items: $ll")

    ll.insertAfterMatch("This is after Tim", "Tim")
    println("\n 'insert after Tim': $ll")

    ll.insertAfterMatch( "This is after Peter", "Peter")
    println(" 'insert after Peter': $ll")

    ll.insertBeforeMatch("Carl", "Before Carl")
    println("\n 'insert before Carl' on Head: $ll")

    ll.insertBeforeMatch("Tim" , "Before Tim")
    println(" 'insert before Tim' on middle of list: $ll")
    
    ll.insertAfterIndex("inserted after 3rd", 2)
    println("\n insert after index 2: $ll")

    ll.insertBeforeIndex("inserted before 2nd", 1)
    println("\n insert before index 1: $ll")
}


class UnskilledSwordJUgglerException :
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