import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class LinkedListTest {

    var ll = LinkedList<String>()

    @org.junit.jupiter.api.BeforeEach
    fun setUp() {
        ll.append("John")
        ll.append("Carl")
        ll.append("Zack")
        ll.append("Tim")
        ll.append("Steve")
        ll.append("Peter")
        println(ll)
    }

    @org.junit.jupiter.api.AfterEach
    fun tearDown() {
        println("$ll")
        ll.removeAll()
    }

    @org.junit.jupiter.api.Test
    fun insertAfterMatch() {
        ll.insertAfterMatch("After Tim", "Tim")
        assert(ll.toString() == "[John, Carl, Zack, Tim, After Tim, Steve, Peter]")

        assertTrue(ll.insertAfterMatch("Wont be inserted", "CantFindThisString") == null,
                "failed to return null")
    }

    @org.junit.jupiter.api.Test
    fun insertBeforeMatch() {
        ll.insertBeforeMatch("Before Tim", "Tim")
        assert(ll.toString() == "[John, Carl, Zack, Before Tim, Tim, Steve, Peter]")

        assertTrue(ll.insertBeforeMatch("Wont be inserted", "CantFindThisString") == null,
                "failed to return null")
    }

    @org.junit.jupiter.api.Test
    fun insertBeforeIndex() {
        ll.insertBeforeIndex("Insert before index 1", 1)
        assert(ll.toString() == "[John, Insert before index 1, Carl, Zack, Tim, Steve, Peter]")

        assertTrue(ll.insertBeforeIndex("Replace Head", 0) != null)
        assert(ll.first()?.value == "Replace Head")
    }

    @org.junit.jupiter.api.Test
    fun insertAfterIndex() {
        ll.insertAfterIndex("inserted after index 2", 2)
        assertTrue(ll.toString() == "[John, Carl, Zack, inserted after index 2, Tim, Steve, Peter]", "this is messed up")

        ll.insertAfterIndex("abc", 2)
        println(ll)
        assertTrue(ll.toString() == "[John, Carl, Zack, abc, inserted after index 2, Tim, Steve, Peter]", "this is messed up")

        ll.insertAfterIndex("def", 2)
        println(ll)
        assertTrue(ll.toString() == "[John, Carl, Zack, def, abc, inserted after index 2, Tim, Steve, Peter]", "this is 2 messed up")

        assertTrue(ll.insertAfterIndex("wont be inserted", 20) == null,
                "Failed to return null")
    }

    @org.junit.jupiter.api.Test
    fun removeAtIndex() {
        ll.removeAtIndex(2)
        assert(ll.toString() == "[John, Carl, Tim, Steve, Peter]")

        assert(ll.removeAtIndex(20) == null)
    }

    @org.junit.jupiter.api.Test
    fun removeNode() {
        val startCount = ll.count()
        ll.nodeAtIndex(0)?.let { ll.removeNode(it) }
        assert(ll.count() == startCount - 1)
    }

    @org.junit.jupiter.api.Test
    fun count() {

        assert(ll.count() == 6)

        val startCount = ll.count()
        ll.append("Steve")
        assert(ll.count() == startCount + 1)

    }

    @org.junit.jupiter.api.Test
    fun first() {
        assert(ll.first()?.value == "John")
    }

    @Test
    fun insertNodeAfterPredicateCheck() {
        val pred : (Node<String>?, String)-> Boolean = {
            node, str -> node?.value == str
        }
        val startCount = ll.count()

        ll.insertNodeAfterPredicateCheck("New Element", "Zack")
            { node, str -> node?.value == str }
        println(ll)
        assert(ll.count() == startCount +1)

        ll.insertNodeAfterPredicateCheck("Shouldn't Insert element",
                "XXXX", pred )
        println(ll)
        assert(ll.count() == startCount + 1)

    }

    @Test
    fun contains() {
        assertTrue(ll.contains("Zack"), "Should contain Zack")
        assertFalse(ll.contains("Missing ELement"), "Should not contain Element")
    }

    @org.junit.jupiter.api.Test
    fun last() {
        assert(ll.last()?.value == "Peter")
    }

    @org.junit.jupiter.api.Test
    fun append() {
        ll.append("steve")
        assert(ll.count() == 7)
    }

    @Test
    fun removeAll() {
        ll.removeAll()
        assert(ll.count() == 0)
    }

    @Test
    fun nodeAtIndex() {
        assert(ll.nodeAtIndex(0)?.value == "John")
        assert(ll.nodeAtIndex(20) == null)
    }

    @Test
    fun removeLast() {
        val startCount = ll.count()
        ll.removeLast()
        assert(ll.count() == startCount - 1)
        assert(ll.last()?.value == "Steve")

        while(ll.removeLast() != null) {
            ll.removeLast()
        }
        assert(ll.removeLast() == null)
    }
}