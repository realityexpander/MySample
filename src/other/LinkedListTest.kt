package other

import LinkedList
import Node
import org.junit.jupiter.api.*
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class LinkedListTest {

    private var ll = LinkedList<String>()

    @BeforeEach
    fun setUp() {
        ll.append("John")
        ll.append("Carl")
        ll.append("Zack")
        ll.append("Tim")
        ll.append("Steve")
        ll.append("Peter")
        println(ll)
    }

    @AfterEach
    fun tearDown() {
        println("$ll")
        ll.removeAll()
    }

    @Test
    fun insertAfterMatch() {
        ll.insertAfterMatch("Insert After Tim", "Tim")
        assert(ll.toString() == "[John, Carl, Zack, Tim, Insert After Tim, Steve, Peter]")

        assertTrue(ll.insertAfterMatch("Wont be inserted", "CantFindThisString") == null,
                "failed to not insert item and return null")
    }

    @Test
    fun insertBeforeMatch() {
        ll.insertBeforeMatch("Insert Before Tim", "Tim")
        assert(ll.toString() == "[John, Carl, Zack, Insert Before Tim, Tim, Steve, Peter]")

        assertTrue(ll.insertBeforeMatch("Wont be inserted", "CantFindThisString") == null,
                "failed to not insert item and return null")
    }

    @Test
    fun insertBeforeIndex() {
        ll.insertBeforeIndex("Insert before index 1", 1)
        assert(ll.toString() == "[John, Insert before index 1, Carl, Zack, Tim, Steve, Peter]")

        assertTrue(ll.insertBeforeIndex("Replace Head", 0) != null)
        assert(ll.first()?.value == "Replace Head")
    }

    @Test
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

    @Test
    fun removeAtIndex() {
        ll.removeAtIndex(2)
        assert(ll.toString() == "[John, Carl, Tim, Steve, Peter]")

        assert(ll.removeAtIndex(20) == null)
    }

    @Test
    fun removeNode() {
        val startCount = ll.count()
        ll.nodeAtIndex(0)?.let { ll.removeNode(it) }
        assert(ll.count() == startCount - 1)
    }

    @Test
    fun count() {

        assert(ll.count() == 6)

        val startCount = ll.count()
        ll.append("Steve")
        assert(ll.count() == startCount + 1)

    }

    @Test
    fun first() {
        assert(ll.first()?.value == "John")
    }

    @Test
    fun insertNodeAfterUsingPredicate() {

        val pred : (Node<String>?) -> Boolean = {
            node-> node?.value == "unfindable element"
        }
        val startCount = ll.count()

        ll.insertNodeAfterUsingPredicate("New Element")
            { node-> node?.value == "Zack" }
        println(ll)
        assert(ll.count() == startCount +1)

        // should fail
        ll.insertNodeAfterUsingPredicate("Shouldn't Insert element",
                pred)
        println(ll)
        assert(ll.count() == startCount + 1)

    }

    @Test
    fun insertNodeBeforeUsingPredicateCheck() {
        val pred : (Node<String>?)-> Boolean = {
            node -> node?.value == "unfindable element"
        }
        val startCount = ll.count()

        ll.insertNodeBeforeUsingPredicate("New Element")
            { node -> node?.value == "Zack" }
        println(ll)
        assert(ll.count() == startCount +1)
        assert(ll.nodeAtIndex(2)?.value == "New Element")

        // should fail
        ll.insertNodeBeforeUsingPredicate("Shouldn't Insert element", pred )
        println(ll)
        assert(ll.count() == startCount + 1)
    }

    @Test
    fun contains() {
        assertTrue(ll.contains("Zack"), "Should contain Zack")
        assertFalse(ll.contains("Missing Element"), "Should not contain Element")
    }

    @Test
    fun last() {
        assert(ll.last()?.value == "Peter")
    }

    @Test
    fun append() {
        ll.append("steve")
        assert(ll.count() == 7)
    }

    @Test
    fun removeAll() {
        ll.removeAll()
        assert(ll.count() == 0)

        assert(ll.isEmpty)
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