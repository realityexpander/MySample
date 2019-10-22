// https://stackoverflow.com/questions/36155108/kotlin-generics-in-kfunction1
import kotlin.Exception

open class Base1 {var a: Int = 0}
open class Base2: Base1() {var b: Int = 1}
open class Base3: Base2() {var c: Int = 2}
open class Base4: Base3() {var d: Int = 3}
open class Base5: Base4() {var e: Int = 4}

fun mainGenerics() {

    // *****
    // * <T> = Simple Generics
    class Invariant<T>  {
        fun f(i: Int): T { throw Exception() } // OK
        fun f(t: T): Int { throw Exception() } // OK
    }
    val i0: Invariant<Base1>   = Invariant<Base1>() // OK     Base1  -> Base1
    val i1: Invariant<Base2>   = Invariant<Base2>() // OK     Base2 -> Base2
//  val i2: Invariant<Base1>   = Invariant<Base2>() // Error  Cant mismatch types
//  val i3: Invariant<Base2>   = Invariant<Base>()  // Error  Cant mismatch types

    // **************
    // * <T: Base2> = Simple Generics w/ restriction bounds
    class Invariant2<T: Base2>  {
        fun f(i: Int): T { throw Exception() } // OK
        fun f(t: T): Int { throw Exception() } // OK
    }
//  val i4: Invariant2<Base1>   = Invariant2<Base1>()   // Error  Base1 is not in bounds
    val i5: Invariant2<Base2>   = Invariant2<Base2>()   // OK     Base2 -> Base2
//  val i6: Invariant2<Base>    = Invariant2<Base2>()   // Error  Base is not in bounds
//  val i7: Invariant2<Base2>   = Invariant2<Base>()    // Error  Base is not in bounds

    // ********
    // * <IN T> == Contravariant, input only, can accept any generic class/type, or Subclass of it
    // 'in' values must be a Subtype of generic param, like java <? extends T>
    class Contravariant<in T> {
    //  fun f(i: Int): T { throw Exception() } // error, T cannot be returned, its an only for parameters
        fun f(t: T): Int { throw Exception() } // OK, T is parameter type
    }
    //                    Tt=to                     Tf=from
    val n0: Contravariant<Base1>    = Contravariant<Base1>() // OK    Equal classes always work
    val n1: Contravariant<Base2>    = Contravariant<Base1>() // OK    as Tt is subclass of Tf
//  val n3: Contravariant<Base>     = Contravariant<Base2>() // Error bc Tt is not subclass of Tf
    val n3: Contravariant<Base2>    = Contravariant<Base2>() // Ok    Equal classes always work

    val n2: Contravariant<Base3>    = Contravariant<Base1>() // OK    as Tt is subclass of Tf
    val n5: Contravariant<Base3>    = Contravariant<Base2>() // OK    as Tt is subclass of Tf
    val n7: Contravariant<Base2>    = Contravariant<Base2>() // OK    Equal classes always work
    val n6: Contravariant<Base4>    = Contravariant<Base2>() // OK    as Tt is subclass of Tf
    val n8: Contravariant<Base4>    = Contravariant<Base3>() // OK    as Tt is subclass of Tf


    // *****************
    // * <IN T: Base2> == Contravariant, input only, restricted bounds to specific subclasses/subtypes
    // <in T: allowTypeOrSubtypeOfT>
    // 'in' must be a Subtype of generic param, and restricted to class and subclasses of Base2, like java <? extends T>
    class Contravariant2<in T: Base2> {
        fun f(t: T) : Int { throw Exception() }
    }
    //                     Tt=to                           Tf=from
    val o2: Contravariant2<Base2>    = Contravariant2<Base2>()  // OK     Equal classes always work
    val p4: Contravariant2<Base3>    = Contravariant2<Base2>()  // OK     as Tt is subclass of Tf
//  val o4: Contravariant2<Base2>    = Contravariant2<Base3>()  // Error  as Tt is not subclass of Tf
    val o4: Contravariant2<Base2>    = Contravariant2<Base2>()  // OK     Equal classes always work

    val o5: Contravariant2<Base4>    = Contravariant2<Base2>()  // OK     as Tt is subclass of Tf
    val o6: Contravariant2<Base4>    = Contravariant2<Base3>()  // OK     as Tt is subclass of Tf
//  val o7: Contravariant2<Base>     = Contravariant2<Base2>()  // Error  bc Tt is not in bounds of <T: Base2>
//  val o8: Contravariant2<Base2>    = Contravariant2<Base>()   // Error  bc Tf is not in bounds of <T: Base2>
//  val o9: Contravariant2<Base2>    = Contravariant2<Base3>()  // Error  bc Tt is not a subclass of Tf


    // *********
    // * <OUT R> == Covariant (OK with same class or Superclass), generic class/type, or Superclass/type of it
    // R must be same type or Supertype of generic param, is return only, and like java <? super R>
    class Covariant<out R> {
        fun f(i: Int): R { throw Exception() } // OK, R is returned
    //  fun f(t: Int): Int { throw Exception() } // error, R cannot be parameter type, 'out' type only
    }
    //                Rt=to                   Rf=from
    val v1: Covariant<Base1>    = Covariant<Base1>()  // OK     Equal classes always work
//  val v2: Covariant<Base2>    = Covariant<Base>()   // Error  bc Rt is not superclass of Rf
    val v6: Covariant<Base1>    = Covariant<Base2>()  // OK     as Rt is superclass of Rf
    val v2: Covariant<Base2>    = Covariant<Base2>()  // OK     Equal classes always work

    val v3: Covariant<Base2>    = Covariant<Base3>()  // OK     as Rt is superclass of Rf
//  val v4: Covariant<Base2>    = Covariant<Base>()   // Error  bc Rt is not superclass of Rf
//  val v5: Covariant<Base3>    = Covariant<Base>()   // Error  bc Rt is not superclass of Rf


//    class Covary<in T:Base3, R>(var x: R) {
//        fun f(i: Int): R {return x as R}
//
//        fun f(e: T) {x = e as R}
//    }

    class Covary<in T:Base2, R>(var y: R) {
        fun get(i: Int): R { println("get() ${this.toString()}"); return y }
        fun set(e:T) { println("set() ${this.toString()}"); y=e as R }
        inline fun <reified R> get(e: T): R {
            return if(e is R) e as R else y as R
        }

        override fun toString(): String {
            return (y as T).run {
                "a=${a}, b=${b}, " +
                /*c=${c},*/ "${this.javaClass.simpleName}, " + super.toString()}
        }
    }
    val aa = Covary<Base2, Base2>(Base2())
//    aa.f(Base2())
    var x = aa.get(0)
    aa.set(Base3()) // Set a new Base2
    println(aa.get(0).toString())


    // ******************
    // * <OUT R: Base2>
    // <out R: requiredTypeOrSuperTypeOfR>
    // 'out' must be a Supertype of generic param, restricted bounds to class and superclass/type of Base2
    // like java <? super R>
    class Covariant2<out R: Base2> {
        fun f(t: Int) : R { throw Exception() }
    }
    //                 Rt=to                    Rf=from
    val z1: Covariant2<Base2>    = Covariant2<Base2>() // OK     Equal classes always work
    val z2: Covariant2<Base2>    = Covariant2<Base3>() // OK     as Rt is superclass of Rf
    val z4: Covariant2<Base2>    = Covariant2<Base4>() // OK     as Rt is superclass of Rf

    val z5: Covariant2<Base3>    = Covariant2<Base3>() // OK     Equal classes always work
    val z6: Covariant2<Base3>    = Covariant2<Base4>() // OK     as Rt is superclass of Rf
//  val z7: Covariant2<Base3>    = Covariant2<Base2>() // Error  as Rt is not superclass of Rf
//  val z8: Covariant2<Base1>    = Covariant2<Base2>() // Error  bc Rt is not a subclass of Base2


    // ***************
    // * <IN T, OUT R>
    class Crossvariant3<in T, out R> {
        fun f(t: T): R { throw Exception() }
    }
    // Tt >= Tf, Rt <= Rf
    //                    Tt=to   Rt=to                  Tf=from Rf=from
    val r1: Crossvariant3<Base1,  Base1> = Crossvariant3<Base1,  Base2>() // OK    Rt is superclass of Rf
    val r2: Crossvariant3<Base1,  Base2> = Crossvariant3<Base1,  Base2>() // OK    Equal classes always work
    val r3: Crossvariant3<Base2,  Base1> = Crossvariant3<Base1,  Base2>() // OK    Tt is subclass of Tf, Rt is superclass of Rf
    val r4: Crossvariant3<Base2,  Base2> = Crossvariant3<Base1,  Base2>() // OK    Tt is subclass of Tf

//  val r9: Crossvariant3<Base1,  Base1> = Crossvariant3<Base2,  Base1>() // Error Tt not subclass of Tf
//  val s1: Crossvariant3<Base1,  Base2> = Crossvariant3<Base2,  Base1>() // Error Tt not subclass of Tf
    val s2: Crossvariant3<Base2,  Base1> = Crossvariant3<Base2,  Base1>() // OK    Equal classes always work
//  val s3: Crossvariant3<Base2,  Base2> = Crossvariant3<Base2,  Base1>() // OK    Rf is not superclass of Rt

    val s5: Crossvariant3<Base1,  Base1> = Crossvariant3<Base1,  Base2>() // OK    Tt same class of Tf, Rt superclass of Rf
    val s6: Crossvariant3<Base1,  Base2> = Crossvariant3<Base1,  Base2>() // OK    Equal classes always work
    val s7: Crossvariant3<Base2,  Base1> = Crossvariant3<Base1,  Base2>() // OK    Tt subclass of Tf, Rt superclass of Rf
    val s8: Crossvariant3<Base2,  Base2> = Crossvariant3<Base1,  Base2>() // OK    Tt subclass of Tf, Rt same class of Rf

//  val r6: Crossvariant3<Base1,  Base2> = Crossvariant3<Base2,  Base2>() // Error Tt not subclass of Tf
//  val r5: Crossvariant3<Base1,  Base1> = Crossvariant3<Base2,  Base2>() // Error Tt not subclass of Tf
    val r7: Crossvariant3<Base2,  Base1> = Crossvariant3<Base2,  Base2>() // OK    Tt same class Tf, Rt is superclass of Rf
    val r8: Crossvariant3<Base3,  Base1> = Crossvariant3<Base2,  Base2>() // OK    Tt subclass Tf, Rt is superclass of Rf


    // ************************
    // * <IN T, OUT R: Base2>
    // 'in' are input parameter types, 'out' are for return types
    // <in T, out R : onlyTypeOrSubtypeOfR>  // note: T alone means onlyTypeOrSupertype of T
    class Crossvariant<in T, out R: Base2> {
        fun f(t: T): R { throw Exception() } // OK, as R is returned
    //    fun f(t: R): T { throw Exception() } // error, bc R is in 'in' position, T is in 'out' position
    }
    //  in T, out R : Base2  // input T is generic, output R is required to be a superclass of 'Base2'
    //                   Tt=to  Rt=to                    Tf=from  Rf=from
//  val w1: Crossvariant<Base2, Base2>    = Crossvariant<Base1,   Base1>() // Error  Rf Base1 is out of bounds
    val w2: Crossvariant<Base2, Base2>    = Crossvariant<Base1,   Base2>() // OK
//  val w3: Crossvariant<Base2, Base1>    = Crossvariant<Base1,   Base2>() // Error  Rt Base1 is out of bounds
//  val w4: Crossvariant<Base1, Base1>    = Crossvariant<Base1,   Base2>() // Error  Rt Base1 is out of bounds

    val w5: Crossvariant<Base1, Base2>    = Crossvariant<Base1,   Base2>() // OK     Equal classes always work
    val w6: Crossvariant<Base2, Base2>    = Crossvariant<Base1,   Base3>() // OK
    val w7: Crossvariant<Base3, Base2>    = Crossvariant<Base1,   Base3>() // OK
//  val w8: Crossvariant<Base1, Base3>    = Crossvariant<Base1,   Base2>() // Error  Rt not a superclass of Rf

    val w9: Crossvariant<Base1, Base2>    = Crossvariant<Base1,   Base2>() // OK     Equal classes always work
    val e1: Crossvariant<Base1, Base3>    = Crossvariant<Base1,   Base3>() // OK     Equal classes always work
    val e2: Crossvariant<Base1, Base3>    = Crossvariant<Base1,   Base4>() // OK
    val e3: Crossvariant<Base2, Base3>    = Crossvariant<Base1,   Base4>() // OK

//  val e3: Crossvariant<Base2, Base4>    = Crossvariant<Base1,   Base3>() // Error  Rt is not superclass of Rf
    val e4: Crossvariant<Base3, Base2>    = Crossvariant<Base2,   Base3>() // OK
    val e5: Crossvariant<Base2, Base2>    = Crossvariant<Base2,   Base3>() // OK
    val e6: Crossvariant<Base2, Base3>    = Crossvariant<Base2,   Base4>() // OK
//  val e7: Crossvariant<Base2, Base2>    = Crossvariant<Base2,   Base1>() // Error  Rf Base1 is out of bounds


    // ************************
    // * <IN T: Base2, OUT R>
    // <in T: onlyTypeOrSubtypeOfT, out R:onlyTypeOrSupertypeOfR>
    class Crossvariant2<in T: Base2, out R: Base2 > {
        fun f(t: T): R { throw Exception() } // OK, as R is returned
    //    fun f(t: R): T { throw Exception() } // error, bc R is in 'in/params' position, T is in 'out/return' position
    }
    // <in T: Base2, out R>  // input T required to be a subclass of 'Base2'
    //                    Tt=to  Rt=to                  Tf=from  Rf=from
//  val v7: Crossvariant2<Base2, Base2> = Crossvariant2<Base1,   Base3>() // Error  Tf Base1 is out of bounds
    val v8: Crossvariant2<Base2, Base2> = Crossvariant2<Base2,   Base3>() // OK     Rt is superclass of Rf
    val q1: Crossvariant2<Base3, Base2> = Crossvariant2<Base2,   Base3>() // OK     Tt is subclass of Tf
    val q0: Crossvariant2<Base3, Base1> = Crossvariant2<Base2,   Base3>() // Error  Rt Base1 is out of bounds
//  val q2: Crossvariant2<Base4, Base1> = Crossvariant2<Base3,   Base1>() // Error  Rf Base1 is out of bounds
    val q2: Crossvariant2<Base4, Base2> = Crossvariant2<Base3,   Base3>() // OK     Tt is subclass of Tf, Rt is superclass of Rf

}
