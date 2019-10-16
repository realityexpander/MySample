class Node<T>(value: T) {
    var value: T = value
    var next: Node<T>? = null
    var previous: Node<T>? = null
}

enum class NodeInsertionMode { INSERT_BEFORE_NODE, INSERT_AFTER_NODE }

class LinkedList<T> {

    private var head: Node<T>? = null

    var isEmpty: Boolean = head == null

    fun first(): Node<T>? = head

    fun last(): Node<T>? {
        var node = head
        return if (node != null) {
            while (node?.next != null) {
                node = node.next
            }
            node
        } else {
            null
        }
    }

    fun count(): Int {
        var node = head
        return if (node != null) {
            var counter = 1
            while (node?.next != null) {
                node = node.next
                counter += 1
            }
            counter
        } else {
            0
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

    fun append(value: T) {
        val newNode = Node(value)
        val lastNode = this.last()
        if (lastNode != null) {
            newNode.previous = lastNode
            lastNode.next = newNode
        } else {
            head = newNode
        }
    }

    fun removeAll() {
        head = null
    }

    fun removeNode(node: Node<T>): T { // why remove T here to remove the return stmnt?
        val prev = node.previous
        val next = node.next
        if (prev != null) {
            prev.next = next
        } else {
            head = next
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

    fun removeAtIndex(index: Int): T? {
        val node = nodeAtIndex(index)
        return if (node != null) {
            removeNode(node)
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

    fun insertAfterMatch(value: T, match: T): T? {
        var node = head
        while (node != null) {
            if (node.value == match) {
                insertAfterThisNode(value, node)
                return node.value
            }
            node = node.next
        }

        return null
    }

    fun insertBeforeMatch(value: T, match: T): T? {
        var node = head
        var prevNode = head
        while (node != null) {
            if (node.value == match) {
                insertBeforeThisNode(value, node, prevNode)
                return value
            }
            prevNode = node
            node = node.next
        }
        return null
    }

    private fun insertAtIndex(value: T, index: Int, NodeInsertMode: NodeInsertionMode): T? {
        var node = head
        var prevNode = head
        var i = 0
        while (node != null && i <= index) {
            if (i == index) {
                when (NodeInsertMode) {
                    NodeInsertionMode.INSERT_BEFORE_NODE ->
                        insertBeforeThisNode(value, node, prevNode)

                    NodeInsertionMode.INSERT_AFTER_NODE ->
                        insertAfterThisNode(value, node)
                }
                return node.value
            }

            i++
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

    private fun insertBeforeThisNode(value: T, thisNode: Node<T>?, prevNode: Node<T>?) {
        if (thisNode == head) { // special case for head
            head = Node(value)
            head?.next = thisNode
            head?.next?.previous = head
        } else {
            prevNode?.next = Node(value)
            prevNode?.next?.next = thisNode
            prevNode?.next?.previous = prevNode
            thisNode?.previous = prevNode?.next
        }
    }

    private fun insertAfterThisNode(value: T, thisNode: Node<T>?) {
        val tmp = thisNode?.next
        thisNode?.next = Node(value)
        thisNode?.next?.next = tmp
    }
}