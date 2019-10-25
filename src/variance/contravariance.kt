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


    open class AExecutor {
//        open fun execute(b: B) {
//            println("AExecutor execute(b: B) b=${b.b}")
//        }

        // lambda property (variable in an object is called a property)
        // This will *not* accept A
        open val execute: (B) -> Unit = {
            println("AExecutor execute (B) -> Unit b=${it.b}")
        }

//        // fun1
//        // Only accept type: B
//        fun execute(b: B) {
//            println("AExecutor execute(b: B)")
//            println("b=${b.b}")
//        }

//        // fun2
//        open fun <T : B> execute(b: T) {
//            println("AExecutor execute(b: <T:B>) b=${b.b}")
//        }
    }

    class BExecutor : AExecutor() {
//        fun execute(a: A) {
//            println("BExecutor execute(a: A) a=${a.a}")
//        }

        // lambda
        // This will also accept B, bc B passed in is a subclass of A
        override val execute: (A) -> Unit = {
            println("BExecutor execute (A) -> Unit a=${it.a}")

            // possible to process just A type info, but can also process for type B too.
            if(it is B) {
                println("  (A) is really type B tho, b=${it.b}")
//                super.execute(it as B) // alt behavior, process with DRY in super
            }
        }

//        // fun1
//        // Only Accept type of A
//        fun execute(a: A) {
//            println("BExecutor execute(a: A) a=${a.a}")
//        }

//        // fun2
//        // Only accept type B or subtype
//        override fun <T : B> execute(b: T) {  // fun2
//            println("* BExecutor B -> AExecutor super.execute ")
//            super.execute(b) // just call the parent fn
//        }
    }

    // We want AExecutor to work on B's, and BExecutor to work on A's
    println()
    println("With Super AExecutor -> B {Super to Sub}")
    with (AExecutor()) {
//                          // Expected result                               // lambda     fun1          fun2
//      execute(A())        // Compiler Error, "Required B found A."
        execute(B())        // AExecutor execute(b: B)                          AExec      AExec         AExec
//      execute((B() as A)) // Compiler Error, "Required B found A."
//      execute((A() as B)) // compiles, but get type cast error at runtime
    }

    println()
    println("With BExecutor -> A {Sub to Super}")
    with (BExecutor()) {
//                          // Expected result                               // lambda     fun1          fun2
        execute(A())        // BExecutor execute(b: B)                          BExec      BExec         BExec
        execute(B())        // BExecutor execute(b: B)                          BExec      AExec <-???-> AExec
        execute((B() as A)) // BExecutor execute(b: B)                          BExec      BExec         BExec
//      execute((A() as B)) // compiles, but get type cast error at runtime
    }


//    println()
//    println()

    // using contravariance
    val boxOfA: Box<A> = Box<A>()
    val boxOfBPtrToBoxOfA: Box<B> = boxOfA
    val boxOfB: Box<B> = Box<A>()

////    update(boxOfB, B())
////    update(boxOfA, A())
//    boxOfBPtrToBoxOfA.update(B())
//    boxOfA.update(A())
////    boxOfB.update(A()) // Compiler Error, "Type mismatch: inferred type is A but B was expected"
//    boxOfB.update(B())

}