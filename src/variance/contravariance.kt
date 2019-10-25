package variance
// from: https://typealias.com/concepts/contravariance/

fun mainContravariance() {

    open class A() {
        var a: Int = 1

        constructor(a: Int) : this() {
            this.a = 99
        }
    }
    open class B : A() {
        var b: Int = 2
    }

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
    val boxOfBPtrToBoxOfA: Box<B> = boxOfA
    val boxOfB: Box<B> = Box<A>()

    open class AExecutor {
        //open fun execute(arg: B) {}

//        // This will *not* accept A // lambda
//        open val execute: (B) -> Unit = {
//            println("AExecutor execute (B) -> Unit")
//            println("b=${it.b}")
//        }

//        // Only accept type: B  // fun1
//        fun execute(b: B) {
//            println("AExecutor execute(b: B)")
//            println("b=${b.b}")
//        }

        open fun <T : B> execute(b: T) { // fun2
            println("AExecutor execute(b: <T:B>) b=${b.b}")
        }
    }

    class BExecutor : AExecutor() {
        //override fun execute(arg: A) {}

//        // This will also accept B, bc B a subclass of A // lambda
//        override val execute: (A) -> Unit = {
//            println("BExecutor execute (A) -> Unit")
//            println("a=${it.a}")
//
//            if(it is B) {
//                println("(A) is really type B, b=${it.b}")
//                super.execute(it as B)
//            }
//        }

        // Only Accept type A // fun1
        fun execute(a: A) {
            println("BExecutor execute(a: A) a=${a.a}")
        }

        // Only accept type B or subtype
        override fun <T : B> execute(b: T) {  // fun2
//        fun execute(b: B) {  // fun2
            println("* BExecutor B -> AExecutor super.execute ")
            super.execute(b)
        }
    }

    println()
    println("With Super AExecutor -> B")
    with (AExecutor()) {
//                          // Expected result                               // lambda     fun1          fun2
//      execute(A())        // Compiler Error, "Required B found A."         // lambda     fun1          fun2
//        execute(B())        // AExecutor execute(b: B)                          AExec      AExec         AExec
//      execute((B() as A)) // Compiler Error, "Required B found A."
//      execute((A() as B)) // compiles, but get type cast error at runtime
    }

    println()

    println("With Sub BExecutor -> A")
    with (BExecutor()) {
//                          // Expected result                               // lambda     fun1          fun2
//        execute(A())        // BExecutor execute(b: B)                          BExec      BExec         BExec
        execute(B())        // BExecutor execute(b: B)                          BExec      AExec <-???-> AExec
//        execute((B() as A)) // BExecutor execute(b: B)                          BExec      BExec         BExec
//      execute((A() as B)) // compiles, but get type cast error at runtime
    }


//    println()
//    println()

////    update(boxOfB, B())
////    update(boxOfA, A())
//    boxOfBPtrToBoxOfA.update(B())
//    boxOfA.update(A())
////    boxOfB.update(A()) // Compiler Error, "Type mismatch: inferred type is A but B was expected"
//    boxOfB.update(B())

}