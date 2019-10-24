package other

// https://stackoverflow.com/questions/36155108/kotlin-generics-in-kfunction1
import kotlin.Exception

open class Base1(var a: Int = 0) {
    override fun toString(): String {
        return super.toString() + ",a=${a}"
    }

    // ==
    override fun equals(other: Any?): Boolean {
        return if(other is Base1)
            a == other.a //&& super.equals(other)
        else
            return false
    }

//    fun equals(other: other.Base1): Boolean { // Will use more specific Base type if other type is known
//        return a == other.a // top of chain, so no super.equals()
//    }
}
open class Base2(a: Int = 0, var b: Int = 1) : Base1(a) {
    override fun toString(): String {
        return super.toString() + ",b=${b}"
    }

    // ==
    override fun equals(other: Any?): Boolean {
        return if(other is Base2)
            b == other.b && super.equals(other)
        else
            super.equals(other)
    }

//    fun equals(other: other.Base2): Boolean {
//        return b == other.b && super.equals(other)
//    }
}
open class Base3(a: Int = 0, b: Int = 1, var c: Int = 2) : Base2(a, b) {
    override fun toString(): String {
        return super.toString() + ",c=${c}"
    }

    // ==
    override fun equals(other: Any?): Boolean {
        return if(other is Base3)
            c == other.c && super.equals(other)
        else
            super.equals(other)
    }

//    fun equals(other: other.Base3): Boolean {
//        return c == other.c && super.equals(other)
//    }
}
open class Base4(a: Int = 0, b: Int = 1, c: Int = 2, var d: Int = 3) : Base3(a, b, c) {
    override fun toString(): String {
        return super.toString() + ",d=${d}"
    }

    // ==
    override fun equals(other: Any?): Boolean {
        return if(other is Base4)
            d == other.d && super.equals(other)
        else
            super.equals(other)
    }

//    fun equals(other: other.Base4): Boolean {
//        return d == other.d && super.equals(other)
//    }
}
open class Base5(a: Int = 0, b: Int = 1, c: Int = 2, d: Int = 3, var e: Int = 4) : Base4(a, b, c, d) {
    override fun toString(): String {
        return super.toString() + ",e=${e}"
    }

    // == operator
    override fun equals(other: Any?): Boolean {
        return if(other is Base5)
            e == other.e && super.equals(other)
        else
            super.equals(other)
    }

//    fun equals(other: other.Base5): Boolean {
//        return e == other.e && super.equals(other)
//    }
}

fun mainGenerics() {

    // *****
    // * <T> = Simple Generics
    class Invariant<T>  {
        fun f(i: Int): T { throw Exception() } // OK
        fun f(t: T): Int { throw Exception() } // OK
    }
    val i0: Invariant<Base1>   = Invariant<Base1>() // OK     other.Base1 -> other.Base1
    val i1: Invariant<Base2>   = Invariant<Base2>() // OK     other.Base2 -> other.Base2
//  val i2: Invariant<other.Base1>   = Invariant<other.Base2>() // Error  Cant mismatch types
//  val i3: Invariant<other.Base2>   = Invariant<Base>()  // Error  Cant mismatch types

    // ************
    // * <T: other.Base2> = Simple Generics w/ restriction bounds
    class Invariant2<T: Base2>  {
        fun f(i: Int): T { throw Exception() } // OK
        fun f(t: T): Int { throw Exception() } // OK
    }
//  val i4: Invariant2<other.Base1>   = Invariant2<other.Base1>()   // Error  other.Base1 is not in bounds
    val i5: Invariant2<Base2>   = Invariant2<Base2>()   // OK     other.Base2 -> other.Base2
    val j5: Invariant2<Base3>   = Invariant2<Base3>()   // OK     other.Base3 -> other.Base3
//  val i6: Invariant2<other.Base1>   = Invariant2<other.Base2>()   // Error  Type mismatch
//  val i7: Invariant2<other.Base2>   = Invariant2<other.Base1>()   // Error  Type mismatch

    // ********
    // * <IN T> == Contravariant, input only, can accept any generic class/type, or other.Subclass of it
    // 'in' values must be a Subtype of generic param, like java <? extends T>
    class Contravariant<in T> {
    //  fun f(i: Int): T { throw Exception() } // error, T cannot be returned, its an only for parameters
        fun f(t: T): Int { throw Exception() } // OK, T is parameter type
    }
    //                    Tt=to                     Tf=from
    val n0: Contravariant<Base1>    = Contravariant<Base1>() // OK    Equal classes always work
    val n1: Contravariant<Base2>    = Contravariant<Base1>() // OK    as Tt is subclass of Tf
//  val n3: Contravariant<other.Base1>    = Contravariant<other.Base2>() // Error bc Tt is not subclass of Tf
    val n3: Contravariant<Base2>    = Contravariant<Base2>() // Ok    Equal classes always work

    val n2: Contravariant<Base3>    = Contravariant<Base1>() // OK    as Tt is subclass of Tf
    val n5: Contravariant<Base3>    = Contravariant<Base2>() // OK    as Tt is subclass of Tf
    val n7: Contravariant<Base2>    = Contravariant<Base2>() // OK    Equal classes always work
    val n6: Contravariant<Base4>    = Contravariant<Base2>() // OK    as Tt is subclass of Tf
    val n8: Contravariant<Base4>    = Contravariant<Base3>() // OK    as Tt is subclass of Tf


    // ***************
    // * <IN T: other.Base2> == Contravariant, input only, restricted bounds to specific subclasses/subtypes
    // <in T: allowTypeOrSubtypeOfT>
    // 'in' must be a Subtype of generic param, and restricted to class and subclasses of other.Base2, like java <? extends T>
    class Contravariant2<in T: Base2> {
        fun f(t: T) : Int { throw Exception() }
    }
    //                     Tt=to                      Tf=from
    val o2: Contravariant2<Base2>    = Contravariant2<Base2>()  // OK     Equal classes always work
    val p4: Contravariant2<Base3>    = Contravariant2<Base2>()  // OK     as Tt is subclass of Tf
//  val o4: Contravariant2<other.Base2>    = Contravariant2<other.Base3>()  // Error  as Tt is not subclass of Tf
    val o4: Contravariant2<Base2>    = Contravariant2<Base2>()  // OK     Equal classes always work

    val o5: Contravariant2<Base4>    = Contravariant2<Base2>()  // OK     as Tt is subclass of Tf
    val o6: Contravariant2<Base4>    = Contravariant2<Base3>()  // OK     as Tt is subclass of Tf
//  val o7: Contravariant2<other.Base1>    = Contravariant2<other.Base2>()  // Error  bc Tt is not in bounds of <T: other.Base2>
//  val o8: Contravariant2<other.Base2>    = Contravariant2<other.Base1>()  // Error  bc Tf is not in bounds of <T: other.Base2>
//  val o9: Contravariant2<other.Base2>    = Contravariant2<other.Base3>()  // Error  bc Tt is not a subclass of Tf


    // *********
    // * <OUT R> == Covariant (OK with same class or Superclass), generic class/type, or Superclass/type of it
    // R must be same type or Supertype of generic param, is return only, and like java <? super R>
    class Covariant<out R> {
        fun f(i: Int): R { throw Exception() } // OK, R is returned
    //  fun f(t: Int): Int { throw Exception() } // error, R cannot be parameter type, 'out' type only
    }
    //                Rt=to                 Rf=from
    val v1: Covariant<Base1>    = Covariant<Base1>()  // OK     Equal classes always work
//  val v2: Covariant<other.Base2>    = Covariant<other.Base1>()  // Error  bc Rt is not superclass of Rf
    val v6: Covariant<Base1>    = Covariant<Base2>()  // OK     as Rt is superclass of Rf
    val v2: Covariant<Base2>    = Covariant<Base2>()  // OK     Equal classes always work

    val v3: Covariant<Base2>    = Covariant<Base3>()  // OK     as Rt is superclass of Rf
//  val v4: Covariant<other.Base2>    = Covariant<other.Base1>()  // Error  bc Rt is not superclass of Rf
//  val v5: Covariant<other.Base3>    = Covariant<other.Base1>()  // Error  bc Rt is not superclass of Rf



//                                   xx345+       x2345+   x2345+
    class CrossvariantWithInternal<T: Base3, out R: Base2, I: Base2>() {

        lateinit var internalStore: T // Must match T Type

        fun set(e: T): R {
            internalStore = e as T
            println("other.set() ${internalStore.toString()}")
            return e as R
        }
        fun getterI(i: Int): I {
            println("getterI() ${internalStore.toString()}")
            return internalStore as I
        }
        inline fun <reified J> getterRi(): J {
            println("getRi() ${internalStore.toString()}")
            return internalStore as J
        }
        fun getterR(): R {
            println("getterR() ${internalStore.toString()}")
//            return other.Base2(internalStore.a, internalStore.b) as R
            return internalStore as R
        }
        fun getterRUp(): R {
            println("getterRUp() ${internalStore.toString()}")
            // Promotes/Up-casts the return class from I to R, 'BaseN(...)' below *must* match R to Upcast properly
            internalStore.run {
                return when (this) {
                    is Base5 -> internalStore as R
                    is Base4 -> Base5(a, (this as Base4).b, (this as Base4).c, (this as Base4).d) as R
                    is Base3 -> Base4(a, (this as Base3).b, (this as Base3).c) as R
                    is Base2 -> Base3(a, (this as Base2).b) as R
                    is Base1 -> Base2(a) as R
                    else -> internalStore as R
                }
            }
        }

        override fun toString(): String {
            return "CovaryInternal internal2=${internalStore.toString()}, " + super.toString()
        }
    }

    val aa = CrossvariantWithInternal<Base3, Base2, Base2>( )
//    aa.other.set(other.Base1())
//    aa.other.set(other.Base2())
//    aa.other.set(other.Base3())
//    aa.other.set(other.Base4())
    aa.set(Base3(99, 99, 55))
    var base1: Base1 = Base1(99)
    var base2: Base2 = Base2(99, 99)
    var base3: Base3 = Base3(99, 99)
    var base4: Base4 = Base5(99, 50, 99)
    var base5: Base5 = Base5(99, 99, 99)

//    println(base2.equals(base5))
//    println(base3.equals(base5))
//    println(base4.equals(base5))
    println("base5 == base4 => ${base5 == base4}")
//    println(aa.getterRi<other.Base5>())


    // ****************
    // * <OUT R: other.Base2>
    // <out R: requiredTypeOrSuperTypeOfR>
    // 'out' must be a Supertype of generic param, restricted bounds to class and superclass/type of other.Base2
    // like java <? super R>
    class Covariant2<out R: Base2> {
        fun f(t: Int) : R { throw Exception() }
    }
    //                 Rt=to                  Rf=from
    val z1: Covariant2<Base2>    = Covariant2<Base2>() // OK     Equal classes always work
    val z2: Covariant2<Base2>    = Covariant2<Base3>() // OK     as Rt is superclass of Rf
    val z4: Covariant2<Base2>    = Covariant2<Base4>() // OK     as Rt is superclass of Rf

    val z5: Covariant2<Base3>    = Covariant2<Base3>() // OK     Equal classes always work
    val z6: Covariant2<Base3>    = Covariant2<Base4>() // OK     as Rt is superclass of Rf
//  val z7: Covariant2<other.Base3>    = Covariant2<other.Base2>() // Error  as Rt is not superclass of Rf
//  val z8: Covariant2<other.Base1>    = Covariant2<other.Base2>() // Error  bc Rt is not a subclass of other.Base2


    // ***************
    // * <IN T, OUT R>
    class Crossvariant3<in T, out R> {
        fun f(t: T): R { throw Exception() }
    }
    // Tt >= Tf, Rt <= Rf
    //                    Tt=to   Rt=to                  Tf=from Rf=from
    val r1: Crossvariant3<Base1, Base1> = Crossvariant3<Base1, Base2>() // OK    Rt is superclass of Rf
    val r2: Crossvariant3<Base1, Base2> = Crossvariant3<Base1, Base2>() // OK    Equal classes always work
    val r3: Crossvariant3<Base2, Base1> = Crossvariant3<Base1, Base2>() // OK    Tt is subclass of Tf, Rt is superclass of Rf
    val r4: Crossvariant3<Base2, Base2> = Crossvariant3<Base1, Base2>() // OK    Tt is subclass of Tf

//  val r9: Crossvariant3<other.Base1,  other.Base1> = Crossvariant3<other.Base2,  other.Base1>() // Error Tt not subclass of Tf
//  val s1: Crossvariant3<other.Base1,  other.Base2> = Crossvariant3<other.Base2,  other.Base1>() // Error Tt not subclass of Tf
    val s2: Crossvariant3<Base2, Base1> = Crossvariant3<Base2, Base1>() // OK    Equal classes always work
//  val s3: Crossvariant3<other.Base2,  other.Base2> = Crossvariant3<other.Base2,  other.Base1>() // OK    Rf is not superclass of Rt

    val s5: Crossvariant3<Base1, Base1> = Crossvariant3<Base1, Base2>() // OK    Tt same class of Tf, Rt superclass of Rf
    val s6: Crossvariant3<Base1, Base2> = Crossvariant3<Base1, Base2>() // OK    Equal classes always work
    val s7: Crossvariant3<Base2, Base1> = Crossvariant3<Base1, Base2>() // OK    Tt subclass of Tf, Rt superclass of Rf
    val s8: Crossvariant3<Base2, Base2> = Crossvariant3<Base1, Base2>() // OK    Tt subclass of Tf, Rt same class of Rf

//  val r6: Crossvariant3<other.Base1,  other.Base2> = Crossvariant3<other.Base2,  other.Base2>() // Error Tt not subclass of Tf
//  val r5: Crossvariant3<other.Base1,  other.Base1> = Crossvariant3<other.Base2,  other.Base2>() // Error Tt not subclass of Tf
    val r7: Crossvariant3<Base2, Base1> = Crossvariant3<Base2, Base2>() // OK    Tt same class Tf, Rt is superclass of Rf
    val r8: Crossvariant3<Base3, Base1> = Crossvariant3<Base2, Base2>() // OK    Tt subclass Tf, Rt is superclass of Rf


    // **********************
    // * <IN T, OUT R: other.Base2>
    // 'in' are input parameter types, 'out' are for return types
    // <in T, out R : onlyTypeOrSubtypeOfR>  // note: T alone means onlyTypeOrSupertype of T
    class Crossvariant<in T, out R: Base2> {
        fun f(t: T): R { throw Exception() } // OK, as R is returned
    //    fun f(t: R): T { throw Exception() } // error, bc R is in 'in' position, T is in 'out' position
    }
    //  Tt >= Tf, Rt <= Rf: other.Base2
    //  <in T, out R : other.Base2>  // input T is generic, output R is required to be a superclass of 'other.Base2'
    //                   Tt=to  Rt=to                    Tf=from  Rf=from
//  val w1: Crossvariant<other.Base2, other.Base2>    = Crossvariant<other.Base1,   other.Base1>() // Error  Rf other.Base1 is out of bounds
    val w2: Crossvariant<Base2, Base2>    = Crossvariant<Base1, Base2>() // OK
//  val w3: Crossvariant<other.Base2, other.Base1>    = Crossvariant<other.Base1,   other.Base2>() // Error  Rt other.Base1 is out of bounds
//  val w4: Crossvariant<other.Base1, other.Base1>    = Crossvariant<other.Base1,   other.Base2>() // Error  Rt other.Base1 is out of bounds

    val w5: Crossvariant<Base1, Base2>    = Crossvariant<Base1, Base2>() // OK     Equal classes always work
    val w6: Crossvariant<Base2, Base2>    = Crossvariant<Base1, Base3>() // OK
    val w7: Crossvariant<Base3, Base2>    = Crossvariant<Base1, Base3>() // OK
//  val w8: Crossvariant<other.Base1, other.Base3>    = Crossvariant<other.Base1,   other.Base2>() // Error  Rt not a superclass of Rf

    val w9: Crossvariant<Base1, Base2>    = Crossvariant<Base1, Base2>() // OK     Equal classes always work
    val e1: Crossvariant<Base1, Base3>    = Crossvariant<Base1, Base3>() // OK     Equal classes always work
    val e2: Crossvariant<Base1, Base3>    = Crossvariant<Base1, Base4>() // OK
    val e3: Crossvariant<Base2, Base3>    = Crossvariant<Base1, Base4>() // OK

//  val e3: Crossvariant<other.Base2, other.Base4>    = Crossvariant<other.Base1,   other.Base3>() // Error  Rt is not superclass of Rf
    val e4: Crossvariant<Base3, Base2>    = Crossvariant<Base2, Base3>() // OK
    val e5: Crossvariant<Base2, Base2>    = Crossvariant<Base2, Base3>() // OK
    val e6: Crossvariant<Base2, Base3>    = Crossvariant<Base2, Base4>() // OK
//  val e7: Crossvariant<other.Base2, other.Base2>    = Crossvariant<other.Base2,   other.Base1>() // Error  Rf other.Base1 is out of bounds


    // **********************
    // * <IN T: other.Base2, OUT R>
    // <in T: onlyTypeOrSubtypeOfT, out R:onlyTypeOrSupertypeOfR>
    class Crossvariant2<in T: Base2, out R: Base2> {
        fun f(t: T): R { throw Exception() } // OK, as R is returned
    //    fun f(t: R): T { throw Exception() } // error, bc R is in 'in/params' position, T is in 'out/return' position
    }
    //  Tt >= Tf: other.Base2, Rt <= Rf
    // <in T: other.Base2, out R>  // input T required to be a subclass of 'other.Base2'
    //                    Tt=to  Rt=to                  Tf=from  Rf=from
//  val v7: Crossvariant2<other.Base2, other.Base2> = Crossvariant2<other.Base1,   other.Base3>() // Error  Tf other.Base1 is out of bounds
    val v8: Crossvariant2<Base2, Base2> = Crossvariant2<Base2, Base3>() // OK     Rt is superclass of Rf
    val q1: Crossvariant2<Base3, Base2> = Crossvariant2<Base2, Base3>() // OK     Tt is subclass of Tf
//  val q0: Crossvariant2<other.Base3, other.Base1> = Crossvariant2<other.Base2,   other.Base3>() // Error  Rt other.Base1 is out of bounds
//  val q2: Crossvariant2<other.Base4, other.Base1> = Crossvariant2<other.Base3,   other.Base1>() // Error  Rf other.Base1 is out of bounds
    val q2: Crossvariant2<Base4, Base2> = Crossvariant2<Base3, Base3>() // OK     Tt is subclass of Tf, Rt is superclass of Rf

}
