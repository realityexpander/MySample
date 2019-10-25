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
    open class C : B() {
        var c: Int = 3
    }

    class Box<T:A, out R:List<T>>(val e: T) { // drop-box only accepts type T or subtype
        private val _items = mutableListOf<T>(e)
        val items: R
            get() = _items as R
        val item
            get() = (_items[0] as T).a

        fun deposit(item: T) = _items.add(item)
        fun showItems() = _items.forEach(::println)

        fun insert(item: T) {
            println()
            deposit(item as T)
            showItems()
        }

    }

//    class Box2<T, out R:List<T>>(val e:T) {
//        private val _items = mutableListOf<T>(e)
//
//        val items: R
//            get() = _items as R
//    }

//    fun <T:A> readA(box: Box<T, List<T>>) {
    fun <T:A> readA(box: Box<T, List<T>>) {
        println("readA() list=${ box.items.joinToString{ "a=${it.a.toString()}" } }" )
    }

    fun <T:B> readB(box: Box<T, List<T>>) {
        println("readB() list=${ box.items.joinToString{ "b=${it.b.toString()}" } }" )
        readA(box as Box<A, List<A>>)
    }

    fun <T:C> readC(box: Box<T, List<T>>) {
        println("readC() list=${ box.items.joinToString{ "c=${it.c.toString()}" } }" )
        readB(box as Box<B, List<B>>)
    }

    val box2: Box<A, List<A>> = Box(A())
    val box3: Box<C, List<C>> = Box(C())
    box3.deposit(C())
    box3.deposit(C())
//    readA(box2)
//    readFirstB(box2)
//    readB(box3)

    fun <T:A> readDeep(box: Box<T, List<T>>) {
        readA(box)
    }
    fun <T:B> readDeep(box: Box<T, List<T>>) {
        readB(box)
    }
    fun <T:C> readDeep(box: Box<T, List<T>>) {
        readC(box)
    }

    readDeep(box3)




//    // using contravariance
//    val boxOfA: Box<A> = Box<A>()
//    val boxOfBPtrToBoxOfA: Box<B> = boxOfA
//    val boxOfB: Box<B> = Box<A>()
//    val boxOfC: Box<C> = Box<C>()
//
////    update(boxOfB, B())
////    update(boxOfA, A())
////    boxOfBPtrToBoxOfA.update(B())
//    boxOfA.deposit(B())
//    boxOfA.insert(B())
//
////    boxOfB.update(A()) // Compiler Error, "Type mismatch: inferred type is A but B was expected"
////    boxOfB.deposit(A())
//    boxOfB.insert(B())
//
////    boxOfC.deposit(B())
//    boxOfC.insert(C())

    println()

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
        //  use of lambda's allows us to override the signature, but keep the same lambda name
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

//    // We want AExecutor to work on B's, and BExecutor to work on A's
//    println()
//    println("With Super AExecutor -> B {Super to Sub}")
//    with (AExecutor()) {
////                          // Expected result                               // lambda     fun1          fun2
////      execute(A())        // Compiler Error, "Required B found A."
//        execute(B())        // AExecutor execute(b: B)                          AExec      AExec         AExec
////      execute((B() as A)) // Compiler Error, "Required B found A."
////      execute((A() as B)) // compiles, but get type cast error at runtime
//    }
//
//    println()
//    println("With BExecutor -> A {Sub to Super}")
//    with (BExecutor()) {
////                          // Expected result                               // lambda     fun1          fun2
//        execute(A())        // BExecutor execute(b: B)                          BExec      BExec         BExec
//        execute(B())        // BExecutor execute(b: B)                          BExec      AExec <-???-> AExec
//        execute((B() as A)) // BExecutor execute(b: B)                          BExec      BExec         BExec
////      execute((A() as B)) // compiles, but get type cast error at runtime
//    }




}