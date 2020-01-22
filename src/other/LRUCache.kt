package other

import LinkedList

class LRUCache<K, T>(var cache_size:Int ) {

    var cache = LinkedList<K>()
    var map = mutableMapOf<K, T>()

    fun put(key:K, value:T) {
        map[key] = value
        cache.push(key)

        if(cache.count() > cache_size ) {
            map.remove(cache.last()?.value)
            cache.removeLast()
        }
    }

    fun get(key:K):T? {
        if (cache.contains(key)) {
            // move the item to the front
            cache.remove(key)
            cache.push(key)
            return map[key]
        }
        // if no cache hit, return null
        return null
    }

    fun count(): Int{
        return cache.count()
    }

    override fun toString(): String {
        return cache.toString()
    }

}

fun lruCacheTest() {
    var v = LRUCache<Int, String>(7)
    v.put(7, "Chris")
    v.put(8, "Jimbo")

    println(v.get(7))
    println(v.get(8))
    if(v.get(30) == null) println("correct cache miss")

    v.put(1, "first")
    v.put(2, "second")
    v.put(3, "third")
    v.put(4, "fourth")
    v.put(5, "fifth")
    v.put(6, "sixth")

//    println(v.get(6))

    println("cache_count=${v.count()}")
    if(v.get(2) == null) println("get 2 cache miss") else println("Get 2 cache hit")

    println(v.toString())
}