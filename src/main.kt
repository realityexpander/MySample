

public var foo: String = "hello"

fun main(args: Array<String>) {

//    mainBattleGame()
//    println()

//    mainPlayerSwordGame()
//    println()

//    mainTeacherStudentParent()
//    println()

//    // Linked list implementation
//    runLinkedListTesting()

//    val list = addToList()
//    println(list)

//    runSort()

    mainMammals()
    println()

    println("\n\n")

}

private fun runSort() {
    val intArray = intArrayOf(4, 2, 3, 1)
    // before sorting
    println(intArray.joinToString()) //joinToString()) // 4, 3, 2, 1
    intArray.sortDescending()
    // after sorting
    println(intArray.joinToString()) // 1, 2, 3, 4
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

class UnskilledSwordJugglerDescriptionException : RuntimeException("Player cannot juggle swords")
typealias UnskilledSwordJugglerException = kotlin.RuntimeException
//        IllegalStateException("Player cannot juggle swords")

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
