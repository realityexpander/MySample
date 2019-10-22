// https://stackoverflow.com/questions/36155108/kotlin-generics-in-kfunction1
import kotlin.Exception

open class Base
open class SubBase: Base()
open class SubSubBase: SubBase()
open class SubSubSubBase: SubSubBase()


// *****
// * <T> = Simple Generics
class Invariant<T>  {
    fun f(i: Int): T { throw Exception() } // OK
    fun f(t: T): Int { throw Exception() } // OK
}
val i0: Invariant<Base>    = Invariant<Base>()    // OK     Base    -> Base
val i1: Invariant<SubBase> = Invariant<SubBase>() // OK     SubBase -> SubBase
val i2: Invariant<Base>    = Invariant<SubBase>() // Error  Cant mismatch types
val i3: Invariant<SubBase> = Invariant<Base>()    // Error  Cant mismatch types

// **************
// * <T: SubBase> = Simple Generics w/ restriction bounds
class Invariant2<T: SubBase>  {
    fun f(i: Int): T { throw Exception() } // OK
    fun f(t: T): Int { throw Exception() } // OK
}
val i4: Invariant2<Base>    = Invariant2<Base>()    // OK     Base    -> Base
val i5: Invariant2<SubBase> = Invariant2<SubBase>() // OK     SubBase -> SubBase
val i6: Invariant2<Base>    = Invariant2<SubBase>() // Error  SubBase -> Base
val i7: Invariant2<SubBase> = Invariant2<Base>()    // Error  Base    -> SubBase

// ********
// * <IN T> == Contravariant, can accept any generic class/type, or Subclass of it
// 'in' must be a Subtype of generic param
class Contravariant<in T> {
    //    fun f(i: Int): T { throw Exception() } // error, T cannot be returned, its an 'in' type only
    fun f(t: T): Int { throw Exception() } // OK, T is parameter type
}
//                    Tt=to                          Tf=from
val n1: Contravariant<Base>          = Contravariant<Base>()       // OK    Equal classes always work
val n1: Contravariant<SubBase>       = Contravariant<Base>()       // OK    as Tt is subclass of Tf
val n3: Contravariant<Base>          = Contravariant<SubBase>()    // Error bc Tt is not subclass of Tf
val n3: Contravariant<SubBase>       = Contravariant<SubBase>()    // Ok    Equal classes always work

val n2: Contravariant<SubSubBase>    = Contravariant<Base>()       // OK    as Tt is subclass of Tf
val n4: Contravariant<SubSubBase>    = Contravariant<SubBase>()    // OK    as Tt is subclass of Tf
val n4: Contravariant<SubBase>       = Contravariant<SubBase>()    // OK    Equal classes always work
val n6: Contravariant<SubSubSubBase> = Contravariant<SubBase>()    // OK    as Tt is subclass of Tf
val n5: Contravariant<SubSubSubBase> = Contravariant<SubSubBase>() // OK    as Tt is subclass of Tf


// *****************
// * <IN T: SubBase> == Contravariant, restricted bounds to specific subclasses/subtypes
// <in T: allowTypeOrSubtypeOfT>
// 'in' must be a Subtype of generic param, and restricted to class and subclasses of SubBase
class Contravariant2<in T: SubBase> {
    fun f(t: T) : Int { throw Exception() }
}
//                     Tt=to                           Tf=from
val o2: Contravariant2<SubBase>       = Contravariant2<SubBase>()     // OK     Equal classes always work
val o4: Contravariant2<SubSubBase>    = Contravariant2<SubBase>()     // OK     as Tt is subclass of Tf
val o4: Contravariant2<SubBase>       = Contravariant2<SubSubBase>()  // Error  as Tt is not subclass of Tf
val o4: Contravariant2<SubBase>       = Contravariant2<SubBase>()     // OK     Equal classes always work

val o5: Contravariant2<SubSubSubBase> = Contravariant2<SubBase>()     // OK     as Tt is subclass of Tf
val o6: Contravariant2<SubSubSubBase> = Contravariant2<SubSubBase>()  // OK     as Tt is subclass of Tf
val o7: Contravariant2<Base>          = Contravariant2<SubBase>()     // Error  bc Tt is not in bounds of <T: SubBase>
val o8: Contravariant2<SubBase>       = Contravariant2<Base>()        // Error  bc Tf is not in bounds of <T: SubBase>
val o9: Contravariant2<SubBase>       = Contravariant2<SubSubBase>()  // Error  bc Tt is not a subclass of Tf


// *********
// * <OUT R> == Covariant (OK with same class or Superclass), can return any generic class/type, or Superclass/type of it
// R must be same type or Supertype of generic param
class Covariant<out R> {
    fun f(i: Int): R { throw Exception() } // OK, R is returned
//  fun f(t: Int): Int { throw Exception() } // error, R cannot be parameter type, 'out' type only
}
//                Rt=to                   Rf=from
val v1: Covariant<Base>       = Covariant<Base>()       // OK     Equal classes always work
val v2: Covariant<SubBase>    = Covariant<Base>()       // Error  bc Rt is not superclass of Rf
val v2: Covariant<Base>       = Covariant<SubBase>()    // OK     as Rt is superclass of Rf
val v2: Covariant<SubBase>    = Covariant<SubBase>()    // OK     Equal classes always work

val v3: Covariant<SubBase>    = Covariant<SubSubBase>() // OK     as Rt is superclass of Rf
val v4: Covariant<SubBase>    = Covariant<Base>()       // Error  bc Rt is not superclass of Rf
val v5: Covariant<SubSubBase> = Covariant<Base>()       // Error  bc Rt is not superclass of Rf


// ******************
// * <OUT R: SubBase>
// <out R: allowTypeOrSuperTypeOfR>
// 'out' must be a Supertype of generic param, restricted bounds to class and superclass/type of SubBase
class Covariant2<out R: SubBase> {
    fun f(t: Int) : R { throw Exception() }
}
//                 Rt=to                    Rf=from
val z1: Covariant2<SubBase>    = Covariant2<SubBase>()       // OK     Equal classes always work
val z2: Covariant2<SubBase>    = Covariant2<SubSubBase>()    // OK     as Rt is superclass of Rf
val z4: Covariant2<SubBase>    = Covariant2<SubSubSubBase>() // OK     as Rt is superclass of Rf

val z5: Covariant2<SubSubBase> = Covariant2<SubSubBase>()    // OK     Equal classes always work
val z6: Covariant2<SubSubBase> = Covariant2<SubSubSubBase>() // OK     as Rt is superclass of Rf
val z7: Covariant2<SubSubBase> = Covariant2<SubBase>()       // Error  as Rt is not superclass of Rf
val z8: Covariant2<Base>       = Covariant2<SubBase>()       // Error  bc Rt is not a subclass of SubBase


// ***************
// * <IN T, OUT R>
class Crossvariant3<in T, out R> {
    fun f(t: T): R { throw Exception() }
}
//                    Tt=to     Rt=to                    Tf=from  Rf=from
val r1: Crossvariant3<Base,     Base>    = Crossvariant3<Base,    SubBase>() // OK    Rt is superclass of Rf
val r2: Crossvariant3<Base,     SubBase> = Crossvariant3<Base,    SubBase>() // OK    Equal classes always work
val r3: Crossvariant3<SubBase,  Base>    = Crossvariant3<Base,    SubBase>() // OK    Tt is subclass of Tf, Rt is superclass of Rf
val r4: Crossvariant3<SubBase,  SubBase> = Crossvariant3<Base,    SubBase>() // OK    Tt is subclass of Tf

val r9: Crossvariant3<Base,     Base>    = Crossvariant3<SubBase, Base>()    // Error Tt not subclass of Tf
val s1: Crossvariant3<Base,     SubBase> = Crossvariant3<SubBase, Base>()    // Error Tt not subclass of Tf
val s2: Crossvariant3<SubBase,  Base>    = Crossvariant3<SubBase, Base>()    // OK    Equal classes always work
val s3: Crossvariant3<SubBase,  SubBase> = Crossvariant3<SubBase, Base>()    // OK    Rf is not superclass of Rt

val s5: Crossvariant3<Base,     Base>    = Crossvariant3<Base,    SubBase>() // OK    Tt same class of Tf, Rt superclass of Rf
val s6: Crossvariant3<Base,     SubBase> = Crossvariant3<Base,    SubBase>() // OK    Equal classes always work
val s7: Crossvariant3<SubBase,  Base>    = Crossvariant3<Base,    SubBase>() // OK    Tt subclass of Tf, Rt superclass of Rf
val s8: Crossvariant3<SubBase,  SubBase> = Crossvariant3<Base,    SubBase>() // OK    Tt subclass of Tf, Rt same class of Rf

val r6: Crossvariant3<Base,     SubBase> = Crossvariant3<SubBase, SubBase>() // Error Tt not subclass of Tf
val r5: Crossvariant3<Base,     Base>    = Crossvariant3<SubBase, SubBase>() // Error Tt not subclass of Tf
val r7: Crossvariant3<SubBase,  Base>    = Crossvariant3<SubBase, SubBase>() // OK    Tt same class Tf, Rt is superclass of Rf
val r8: Crossvariant3<SubBase,  SubBase> = Crossvariant3<SubBase, SubBase>() // OK    equal classes always work


// ************************
// * <IN T, OUT R: SubBase>
// 'in' are input parameter types, 'out' are for return types
// <in T, out R : onlyTypeOrSubtypeOfR>  // note: T alone means onlyTypeOrSupertype of T
class Crossvariant<in T, out R: SubBase> {
    fun f(t: T): R { throw Exception() } // OK, as R is returned
//    fun f(t: R): T { throw Exception() } // error, bc R is in 'in' position, T is in 'out' position
}
//  in T, out R : SubBase  // input T is generic, output R is required to be a superclass of 'SubBase'
//                   Tt=to       Rt=to                      Tf=from  Rf=from
val w1: Crossvariant<SubBase,    SubBase>    = Crossvariant<Base,    Base>()          // Error  Rf Base is out of bounds
val w2: Crossvariant<SubBase,    SubBase>    = Crossvariant<Base,    SubBase>()       // OK
val w3: Crossvariant<SubBase,    Base>       = Crossvariant<Base,    SubBase>()       // Error  Rt Base is out of bounds
val w4: Crossvariant<Base,       Base>       = Crossvariant<Base,    SubBase>()       // Error  Rt Base is out of bounds

val w5: Crossvariant<Base,       SubBase>    = Crossvariant<Base,    SubBase>()       // OK     Equal classes always work
val w6: Crossvariant<SubBase,    SubBase>    = Crossvariant<Base,    SubSubBase>()    // OK
val w7: Crossvariant<SubSubBase, SubBase>    = Crossvariant<Base,    SubSubBase>()    // OK
val w8: Crossvariant<Base,       SubSubBase> = Crossvariant<Base,    SubBase>()       // Error  Rt not a superclass of Rf
val w9: Crossvariant<Base,       SubBase>    = Crossvariant<Base,    SubBase>()       // OK     Equal classes always work

val e1: Crossvariant<Base,       SubSubBase> = Crossvariant<Base,    SubSubBase>()    // OK     Equal classes always work
val e2: Crossvariant<Base,       SubSubBase> = Crossvariant<Base,    SubSubSubBase>() // OK
val e3: Crossvariant<SubBase,    SubSubBase> = Crossvariant<Base,    SubSubSubBase>() // OK
val e3: Crossvariant<SubBase,    SubSubSubBase> = Crossvariant<Base, SubSubBase>()    // Error  Rt is not superclass of Rf

val e4: Crossvariant<SubSubBase, SubBase>    = Crossvariant<SubBase, SubSubBase>()    // OK
val e5: Crossvariant<SubBase,    SubBase>    = Crossvariant<SubBase, SubSubBase>()    // OK
val e6: Crossvariant<SubBase,    SubSubBase> = Crossvariant<SubBase, SubSubSubBase>() // OK
val e7: Crossvariant<SubBase,    SubBase>    = Crossvariant<SubBase, Base>()          // Error  Rf Base is out of bounds


// ************************
// * <IN T: SubBase, OUT R>
// <in T: onlyTypeOrSubtypeOfT, out R>
class Crossvariant2<in T: SubBase, out R > {
    fun f(t: T): R { throw Exception() } // OK, as R is returned
//    fun f(t: R): T { throw Exception() } // error, bc R is in 'in/params' position, T is in 'out/return' position
}
// <in T: SubBase, out R>  // input T required to be a subclass of 'SubBase'
//                    Tt=to       Rt=to                    Tf=from  Rf=from
val v7: Crossvariant2<SubBase,    SubBase> = Crossvariant2<Base,    SubSubBase>() // Error, Tf Base is out of bounds
val v8: Crossvariant2<SubBase,    SubBase> = Crossvariant2<SubBase, SubSubBase>() // OK, 'in' subtype is SubBase or sub type
val v9: Crossvariant2<SubSubBase, SubBase> = Crossvariant2<SubBase, SubSubBase>() // OK


