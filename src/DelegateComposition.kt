class CountingList<T>(
        private val newThis: MutableSet<T> = mutableSetOf<T>()
): MutableSet<T> by newThis {
    //    val set = HashSet<T>()
    var addCount = 0
        private set

    override fun add(o: T): Boolean {
        addCount += 1
        return newThis.add(o)
    }

    override fun addAll(collection: Collection<T>): Boolean {
        addCount += collection.size
        return newThis.addAll(collection)
    }
}

class CountingLinkedList<T>(
        // innerObject becomes the new "this", so all members of class are accessed thru it
        private val innerObject: LinkedList<T> = LinkedList<T>()
): ILinkedList<T> by innerObject {
    var addCount = 0
        private set

    override fun add(o: T) {
        addCount += 1
        innerObject.add(o)
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
//    val list2 = LinkedList<String>()

    list.add("hello")
    list.add("Jimmy")
    list.add("George")
    println("${list.addCount}=$list, $list2")
    list.removeAll(); list2.removeAll()
    println("${list.addCount}=$list, $list2")

}