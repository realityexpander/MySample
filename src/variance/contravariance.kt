package variance

import kotlin.test.assertEquals

// from: https://typealias.com/concepts/contravariance/

fun mainContravariance() {


    open class A() {
        var a: Int = 1

        constructor(a: Int) : this() {
            this.a = a
        }

//        fun copy(a: Int = this.a) = A(a)

        // Copy whole objects
        fun copy(o: A): A = A(o.a)
    }
    open class B() : A() {
        var b: Int = 2

        constructor(a: Int = 2, b: Int = 3) : this() {
            this.a = a
            this.b = b
        }

//        // .copy(...) As a method doesn't work bc inheritance selects for most specific declaration.
//        //   so doing .copy(b=12) on a C type will run the B copy and return a new B object, not a C object.
//        fun copy(a: Int = this.a, b: Int = this.b) = B(a, b)

        // Copy whole objects
        fun copy(o: B): B = B(o.a, o.b)
    }
    open class C() : B() {
        var c: Int = 3

        constructor(a: Int = 4, b: Int = 5, c: Int = 6) : this() {
            this.a = a
            this.b = b
            this.c = c
        }

//        // .copy(...) as a lambda (doesnt work bc we cant use named params, all params must be given from caller)
//        open val copy: (Int, Int, Int) -> C = { a, b, c -> C(a+1, b, c) }

//        // .copy(...) As a method doesn't work bc inheritance selects for most specific declaration.
//        //   so doing .copy(b=12) on a C type will run the B .copy method and return a new B object, not a C object.
//        fun copy(a: Int = this.a, b: Int = this.b) = B(a, b)

        // Copy whole objects
        fun copy(o: C): C = C(o.a, o.b, o.c)
    }

    // Must do .copy() as extension function in order to use default params (getting original values with {this})
    // and to get proper inheritance.
    fun A.copy(a: Int = this.a): A  = A(a)
    fun B.copy(a: Int = this.a, b: Int = this.b): B = B(a, b)
    fun C.copy(a: Int = this.a, b: Int = this.b, c: Int = this.c): C = C(a, b, c)


//    // Copy whole objects
    var b01 = B(10,20)
    var a7 = b01.copy(C(a=55,c=77))
    var a6 = C().copy(A(33))
    var a9 = a6.copy()
    assertEquals(a6.a, a9.a, "a6.a should equal a9.a")


    var c02 = C(89, 99, 200)
    var c01 = c02.copy(1, c=2)
    c02 = c01.copy(C(c01.a, b=c01.b, c=c01.c))
    var a8 = b01.copy(a=55,b=77)
    assertEquals(true, c01.b==99, "Messed up")


    // *******************
    // Covariant with inheritance
    class Box<T:A, out R:List<T>>(val e: T) {
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

    // using contravariance with inheritance
//    val boxOfA: Box<A, List<A>> = Box(C())
//    val boxOfBPtrToBoxOfA: Box<B, List<B>> = boxOfA
//    val boxOfB: Box<B> = Box<A>()
//    val boxOfC: Box<C> = Box<C>()

//    update(boxOfB, B())
//    update(boxOfA, A())
//    boxOfBPtrToBoxOfA.update(B())
//    boxOfA.deposit(B())
//    boxOfA.insert(B())
//
//    boxOfB.update(A()) // Compiler Error, "Type mismatch: inferred type is A but B was expected"
//    boxOfB.deposit(A())
//    boxOfB.insert(B())
//
//    boxOfC.deposit(B())
//    boxOfC.insert(C())

//    class Box2<T, out R:List<T>>(val e:T) {
//        private val _items = mutableListOf<T>(e)
//
//        val items: R
//            get() = _items as R
//    }


        // ***********************
        // Contravariant strict
        // Only allow type or subtype, and only allow as input for T
        class Box3<in T : A> {
            private val items = mutableListOf<T>()
            fun showItems() {
                println("types= ${items.joinToString {
                    it.javaClass.simpleName.split("$")[1] +
                            "->a=" +
                            "${(it as T).a}"
                }}" // cast is OK due to T:A
                )
            }

            fun insert(elem: T) {
                items.add(elem)
            }

            fun getItem0(): A {
                return items[0]
            }
        }

        val a = Box3<A>()
        a.insert(A())
        a.insert(B().copy(a = 200))
        a.insert(C())
        a.showItems()

        val b: Box3<B> = a
//  b.insert(A()) // compiler error, type mismatch Required B, found A.
        b.insert(B().copy(a = 99))
        b.insert(C().copy(a = 98))
        b.showItems()
        println("b.getItem0=${b.getItem0().a}")

        val c: Box3<C> = a
//    c.insert(A()) // compiler error, type mismatch Required C, found A.
//    c.insert(B()) // compiler error, type mismatch Required C, found B.
        c.insert(C().copy(a = 97))
        c.showItems()


        // ************************
        // Experiments with variance, call chaining
//    fun <T:A> readA(box: Box<T, List<T>>) {
        fun <T : A> readA(box: Box<T, List<T>>) {
            println("readA() list=${box.items.joinToString { "a=${it.a.toString()}" }}")
        }

        fun <T : B> readB(box: Box<T, List<T>>) {
            println("readB() list=${box.items.joinToString { "b=${it.b.toString()}" }}")
            readA(box as Box<A, List<A>>)
        }

        fun <T : C> readC(box: Box<T, List<T>>) {
            println("readC() list=${box.items.joinToString { "c=${it.c.toString()}" }}")
            readB(box as Box<B, List<B>>)
        }

        val box2: Box<B, List<B>> = Box(B())
        var box3: Box<C, List<C>> = Box(C())
        box3.deposit(C())
        box3.deposit(C())

        readA(box2)
        println()
        readB(box3)

        // **************
        // Experiments with inheritance
        fun <T : A> readDeep(box: Box<T, List<T>>) {
            readA(box)
        }

        fun <T : B> readDeep(box: Box<T, List<T>>) {
            readB(box)
        }

        fun <T : C> readDeep(box: Box<T, List<T>>) {
            readC(box)
        }
//    readDeep(box3)


        // ***********
        // Experiments with default params and inheritance using .copy
        var a1 = A()
        var a2 = a1.copy(99)
        var b1 = B()
        var b2 = b1.copy(b = 23)
//    var c2 = c1.copy(a=988, b=989, c=999)
        var c1 = C()
        var c2 = c1.copy(a = 105)
        c2 = c2.copy(c = 98)
//    c2 = c2.copy(b=99)
//    println("c2=${c2.a}, ${c2.b}, ${c2.c}")


        println()

        // Experiments with Contravariance and call chaining, using lambdas as properties
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
                if (it is B) {
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
        with(AExecutor()) {
            //                          // Expected result                               // lambda     fun1          fun2
//      execute(A())        // Compiler Error, "Required B found A."
            execute(B())        // AExecutor execute(b: B)                          AExec      AExec         AExec
//      execute((B() as A)) // Compiler Error, "Required B found A."
//      execute((A() as B)) // compiles, but get type cast error at runtime
        }

        println()
        println("With BExecutor -> A {Sub to Super}")
        with(BExecutor()) {
            //                          // Expected result                               // lambda     fun1          fun2
            execute(A())        // BExecutor execute(b: B)                          BExec      BExec         BExec
            execute(B())        // BExecutor execute(b: B)                          BExec      AExec <-???-> AExec
            execute((B() as A)) // BExecutor execute(b: B)                          BExec      BExec         BExec
//      execute((A() as B)) // compiles, but get type cast error at runtime
        }


}