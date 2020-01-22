package other

import LinkedList

class LRUCache( var cache_size:Int) {

    var cache = LinkedList<Int>()
    var map = mutableMapOf<Int, String>()

    fun put(k:Int, v:String) {
        map[k] = v
        cache.push(k)

        if(cache.count() > cache_size ) {
            map.remove(cache.last()?.value)
            cache.removeLast()
        }
    }

    fun get(k:Int):String? {
        if (cache.contains(k)) {
            // move the item to the front
            cache.remove(k)
            cache.push(k)
            return map[k]
        }
        // if no cache hit, return null
        return null
    }

    fun cacheItemCount(): Int{
        return cache.count()
    }

    override fun toString(): String {
        return cache.toString()
    }

}

fun lruCacheTest() {
    var v = LRUCache(5)
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

    println("cache_count=${v.cacheItemCount()}")
    if(v.get(2) == null) println("get 1 cache miss") else println("Get 2 cache hit")

    println(v.toString())
}