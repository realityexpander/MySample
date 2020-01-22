class Node<T>(var value: T,
              var next: Node<T>? = null,
              var previous: Node<T>? = null)


enum class NodeInsertionMode { INSERT_BEFORE_NODE, INSERT_AFTER_NODE }

interface ILinkedList<T> {
    fun add(value: T)
    fun removeAll()
}

open class LinkedList<T> : ILinkedList<T> {

    private var head: Node<T>? = null

    var isEmpty: Boolean = head == null

    fun first(): Node<T>? = head

    fun last(): Node<T>? {
        var node = head
        if (node != null) {
            while (node?.next != null) {
                node = node.next
            }
            return node
        } else {
            return null
        }
    }

    fun count(): Int {
        var node = head
        if (node != null) {
            var counter = 1
            while (node?.next != null) {
                node = node.next
                counter += 1
            }
            return counter
        } else {
            return 0
        }
    }

    fun nodeAtIndex(index: Int): Node<T>? {
        if (index >= 0) {
            var node = head
            var i = index
            while (node != null) {
                if (i == 0) return node
                i -= 1
                node = node.next
            }
        }
        return null
    }

    override fun add(value: T) {
        append(value)
    }

    fun append(value: T) {
        val newNode = Node(value)
        val lastNode = this.last()
        if (lastNode != null) {
            newNode.previous = lastNode
            lastNode.next = newNode
        } else {
            head = newNode
        }// ‍

    }

    fun push(value:T) {
        val newNode = Node(value)
        val headNode = head

        if (headNode == null) {
            head = newNode
        } else {
            newNode.next = headNode
            headNode.previous = newNode
            head = newNode
        }
    }

    override fun removeAll() {
        head = null
    }

    fun removeNode(node: Node<T>): T? {
        val prev = node.previous
        val next = node.next
        if (prev == null) {
            head = next
        } else {
            prev.next = next
        }
        next?.previous = prev

        node.previous = null
        node.next = null

        return node.value
    }

    fun removeLast(): T? {
        val last = this.last()
        return if (last != null) {
            removeNode(last)
        } else {
            null
        }
    }

    fun remove(item: T) {
        var node = head
        while (node != null) {
            if (node.value == item)
                removeNode(node)
            node = node.next
        }
    }


    fun removeAtIndex(index: Int): T? {
        val node = nodeAtIndex(index)
        return if (node != null) {
            return removeNode(node)
        } else {
            null
        }
    }

    override fun toString(): String {
        var s = "["
        var node = head
        while (node != null) {
            s += "${node.value}"
            node = node.next
            if (node != null) {
                s += ", "
            }
        }
        return "$s]"
    }

    fun insertNodeBeforeUsingPredicate(value: T, predicate: (Node<T>?) -> Boolean): T? {
        return insertNodeWithPredicate(value, NodeInsertionMode.INSERT_BEFORE_NODE, predicate)
    }

    fun insertNodeAfterUsingPredicate(value: T, predicate: (Node<T>?) -> Boolean): T? {
        return insertNodeWithPredicate(value, NodeInsertionMode.INSERT_AFTER_NODE, predicate)
    }

    private fun insertNodeWithPredicate(value: T,
                                        nodeInsertMode: NodeInsertionMode,
                                        predicate: (Node<T>?) -> Boolean): T? {
        var node = head
        var prevNode = head
        while (node != null) {
            if (predicate(node)) {
                when (nodeInsertMode) {
                    NodeInsertionMode.INSERT_BEFORE_NODE -> insertBeforeCurNode(value, node, prevNode)
                    NodeInsertionMode.INSERT_AFTER_NODE -> insertAfterCurNode(value, node)
                }
                return node.value
            }
            prevNode = node
            node = node.next
        }
        return null
    }

    fun contains(match: T): Boolean {
        var node = head
        while (node != null) {
            if (node.value == match)
                return true
            node = node.next
        }
        return false
    }

    fun insertBeforeMatch(value: T, match: T): T? {
        return insertMatch(value, match, NodeInsertionMode.INSERT_BEFORE_NODE)
    }

    fun insertAfterMatch(value: T, match: T): T? {
        return insertMatch(value, match, NodeInsertionMode.INSERT_AFTER_NODE)
    }

    private fun insertMatch(value: T, match: T, nodeInsertMode: NodeInsertionMode): T? {
        var node = head
        var prevNode = head
        while (node != null) {
            if (node.value == match) {
                when (nodeInsertMode) {
                    NodeInsertionMode.INSERT_BEFORE_NODE ->
                        insertBeforeCurNode(value, node, prevNode)
                    NodeInsertionMode.INSERT_AFTER_NODE ->
                        insertAfterCurNode(value, node)
                }
                return value
            }
            prevNode = node
            node = node.next
        }
        return null
    }

    fun insertBeforeIndex(value: T, index: Int): T? {
        return insertAtIndex(value, index, NodeInsertionMode.INSERT_BEFORE_NODE)
    }

    fun insertAfterIndex(value: T, index: Int): T? {
        return insertAtIndex(value, index, NodeInsertionMode.INSERT_AFTER_NODE)
    }

    private fun insertAtIndex(value: T, index: Int, NodeInsertMode: NodeInsertionMode): T? {
        var node = head
        var prevNode = head
        var i = 0
        while (node != null && i <= index) {
            if (i == index) {
                when (NodeInsertMode) {
                    NodeInsertionMode.INSERT_BEFORE_NODE ->
                        insertBeforeCurNode(value, node, prevNode)

                    NodeInsertionMode.INSERT_AFTER_NODE ->
                        insertAfterCurNode(value, node)
                }
                return node.value
            }

            i++
            prevNode = node
            node = node.next
        }

        return null
    }

    private fun insertBeforeCurNode(value: T, curNode: Node<T>?, prevNode: Node<T>?) {
        if (curNode == head) { // special case for head
            head = Node(value)
            head?.next = curNode
            head?.next?.previous = head
        } else {
            prevNode?.next = Node(value)
            prevNode?.next?.next = curNode
            prevNode?.next?.previous = prevNode
            curNode?.previous = prevNode?.next
        }
    }

    private fun insertAfterCurNode(value: T, curNode: Node<T>?) {
        val tmp = curNode?.next
        curNode?.next = Node(value)
        curNode?.next?.next = tmp
    }



}