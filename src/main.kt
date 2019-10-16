fun main(args: Array<String>) {
    //var bev = readLine() // wait for keybd input
    var bev = "Chris Athanas"
    var swords : Int? = null
    bev?.let {
        bev = it.capitalize()
    } ?: println("i cant do that without crashing")

    try {
        swords ?: throw UnskilledSwordJUgglerException()
    } catch(e:Exception){
        println(e)
    }

    println(bev)

    var player = Player()

    player.name = "Jimbo"
    player.castFireball(3)

    val s = Sword("Abcdefg")
    println(s.name)

    var cls = Aaa(10)

    var list = addToList()
    println(list)


    // Linked list implementation
    var ll = LinkedList<String>()
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
    println("After remove 3rd item: ${ll.nodeAtIndex(2)?.value} items: $ll")
    println("the list has ${ll.count()} items: $ll")
    ll.nodeAtIndex(0)?.let { ll.removeNode(it) }
    println("After removing node 0, the list has ${ll.count()} items: $ll")

    ll.insertAfterMatch("Tim", "This is after Tim")
    println("After insert after Tim: $ll")

    ll.insertAfterMatch("Peter", "This is after Peter")
    println("After insert after Peter: $ll")
}

class Aaa(var superClass: Int)

class UnskilledSwordJUgglerException() :
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