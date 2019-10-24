package variance
// from: https://typealias.com/concepts/contravariance/

fun mainContravariance() {

    open class A
    open class B : A()

    class Box<in T> { // drop-box only accepts type T or subtype
        private val items = mutableListOf<T>()
        fun deposit(item: T) = items.add(item)
        fun showItems() = items.forEach(::println)

        fun update( item: T) {
            println()
            deposit(item)
            showItems()
        }
    }

//    fun <T> update(box: other.Box<T>, item: T){
//        box.deposit(item)
//        box.showItems()
//    }

    // using contravariance
    val boxOfA: Box<A> = Box<A>()
    val boxOfB: Box<B> = Box<A>()
    val boxOfBPtrToBoxOfA: Box<B> = boxOfA

    open class Super {
        //open fun execute(arg: B) {}
        open val execute: (B) -> Unit = {
            println("Super execute() (B) -> Unit")
        }

//        fun execute(a: A) {
//            println("Super execute(a: A)")
//        }
    }

    class Sub : Super() {
        //override fun execute(arg: A) {}
        override val execute: (A) -> Unit = {
            println("Sub execute() (A) -> Unit")
        }

//        fun execute(b: B) {
//            println("Sub execute(b: B)")
//        }
    }

    println("With Sub()")
    with (Sub()) {
        execute(A())        // "Sub execute() (A) -> Unit"
        execute(B())        // "Sub execute() (A) -> Unit"
        execute((B() as A)) // "Sub execute() (A) -> Unit"
//      execute((A() as B)) // compiles, but get type cast error at runtime
    }

    println()
    println("With Super()")
    with (Super()) {
//      execute(A())        // Compiler Error, "Required B found A."
        execute(B())        // "Super execute() (B) -> Unit"
//      execute((B() as A)) // Compiler Error, "Required B found A."
//      execute((A() as B)) // compiles, but get type cast error at runtime
    }

    println()
    println()

//    update(boxOfB, B())
//    update(boxOfA, A())
    boxOfBPtrToBoxOfA.update(B())
    boxOfA.update(A())
//    boxOfB.update(A()) // Compiler Error, "Type mismatch: inferred type is A but B was expected"
    boxOfB.update(B())

}