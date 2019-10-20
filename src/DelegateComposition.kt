class CountingList<T>(
        private val newThis: MutableSet<T> = mutableSetOf<T>()
): MutableSet<T> by newThis {
    //    val set = HashSet<T>()
    var addCount = 0
        private set

    override fun add(element: T): Boolean {
        addCount += 1
        return newThis.add(element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        addCount += elements.size
        return newThis.addAll(elements)
    }
}

class CountingLinkedList<T>(
        // innerObject becomes the new "this", so all members of class are accessed thru it
        private val innerObject: LinkedList<T> = LinkedList<T>()
): ILinkedList<T> by innerObject {
    var addCount = 0
        private set

    override fun add(value: T) {
        addCount += 1
        innerObject.add(value)
    }

    override fun removeAll() {
        addCount -= innerObject.count()
        innerObject.removeAll()
    }

    override fun toString(): String {
        return innerObject.toString()
    }
}


fun main4( args: Array<String>) {
//    val list = CountingList<String>()
    val list = CountingLinkedList<String>()
//    val list = LinkedList<String>()

    list.add("hello")
    list.add("Jimmy")
    list.add("George")
    println("${list.addCount}=$list")
    list.removeAll()
    println("${list.addCount}=$list")

}