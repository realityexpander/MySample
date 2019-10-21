// https://stackoverflow.com/questions/36155108/kotlin-generics-in-kfunction1
import kotlin.Exception

open class Base
open class SubBase: Base()
open class SubSubBase: SubBase()
open class SubSubSubBase: SubSubBase()

// Simple Generics
class Invariant<T>  {
    fun f(i: Int): T { throw Exception() } // OK
    fun f(t: T): Int { throw Exception() } // OK
}
val i0: Invariant<Base>    = Invariant<Base>()    // OK     Base    -> Base
val i3: Invariant<SubBase> = Invariant<SubBase>() // OK     SubBase -> SubBase
val i1: Invariant<Base>    = Invariant<SubBase>() // Error  SubBase -> Base
val i2: Invariant<SubBase> = Invariant<Base>()    // Error  Base    -> SubBase


// 'in' are input parameter types, 'out' are for return types
// <in T, out R : onlyTypeOrSubtypeOfR>  // note: T alone means onlyTypeOrSupertype of T
class Crossvariant<in T, out R : SubBase> {
    fun f(t: T): R { throw Exception() } // OK, as R is returned
//    fun f(t: R): T { throw Exception() } // error, bc R is in 'in' position, T is in 'out' position
}

// in T : onlyTypeOrSubtype, out R
class Crossvariant2<in T : SubBase, out R > {
    fun f(t: T): R { throw Exception() } // OK, as R is returned
//    fun f(t: R): T { throw Exception() } // error, bc R is in 'in' position, T is in 'out' position
}

// in T : onlyTypeOrSubtype, out R
class Crossvariant3<in T : Base, out R > {
    fun f(t: T): R { throw Exception() }
}


// * <IN T> == Contravariant, can accept any generic class/type
// 'in' must be a Subtype of generic param
class Contravariant<in T> {
    //    fun f(i: Int): T { throw Exception() } // error, T cannot be returned, its an 'in' type only
    fun f(t: T): Int { throw Exception() } // OK, T is parameter type
}
//  Tf = from, Tt = to
val n1: Contravariant<SubBase>       = Contravariant<Base>()       // OK     Tf Base       -> Tt SubBase,  as Tt is subclass of Tf
val n2: Contravariant<SubSubBase>    = Contravariant<Base>()       // OK     Tf Base       -> Tt SubSubBase, as Tt is subclass of Tf
val n3: Contravariant<Base>          = Contravariant<SubBase>()    // Error  Tf SubBase    -> Tt Base, bc Tt Base is not subclass of Tf SubBase
val n4: Contravariant<SubSubBase>    = Contravariant<SubBase>()    // OK     Tf SubBase    -> Tt SubSubBase, as Tt is subclass of Tf
val n5: Contravariant<SubSubSubBase> = Contravariant<SubSubBase>() // OK     Tf SubSubBase -> Tt SubSubSubBase, as Tt is subclass of Tf
val n6: Contravariant<SubSubSubBase> = Contravariant<SubBase>()    // OK     Tf SubBase    -> Tt SubSubSubBase, as Tt is subclass of Tf

// * <IN T: SubBase> == Contravariant, restricted to certain classes/types
// 'in' must be a Subtype of generic param, and restricted to class and subclasses of SubBase
class Contravariant2<in T: SubBase> {
    fun f(t: T) : Int { throw Exception() }
}
val o1: Contravariant2<Base>       = Contravariant2<SubBase>()        // Error, Tf SubBase is not in bounds of  <T: SubBase>
val o2: Contravariant2<SubBase>    = Contravariant2<SubBase>()        // OK,    Equal classes always work
val o3: Contravariant2<SubBase>    = Contravariant2<SubSubBase>()     // Error, Tf SubSubBase    -> Tt SubBase, bc Tt is not a subclass of Tf
val o4: Contravariant2<SubSubBase> = Contravariant2<SubBase>()        // OK,    Tf SubSubBase    -> Tt SubSubBase, as Tt is subclass of Tf
val o5: Contravariant2<SubSubSubBase> = Contravariant2<SubSubBase>()  // OK,    Tf SubSubSubBase -> Tt SubSubSubBase, as Tt is subclass of Tf
val o6: Contravariant2<SubBase>    = Contravariant2<Base>()           // Error, Tf SubBase is not in bounds of <T: SubBase>


// * <OUT R> == Covariant, can return any generic class/type
// Keyword 'out' must be a Supertype of generic param
class Covariant<out R> {
    fun f(i: Int): R { throw Exception() } // OK, R is returned
//  fun f(t: Int): Int { throw Exception() } // error, R cannot be parameter type, 'out' type only
}
// Rf = from, Rt = to
val v1: Covariant<Base>       = Covariant<Base>()       // OK     Equal classes always work
val v1: Covariant<Base>       = Covariant<SubBase>()    // OK     Rf SubBase    -> Rt Base, as Rt is superclass of Rf
val v2: Covariant<SubBase>    = Covariant<Base>()       // error  Rf Base       -> Rt SubBase, bc Rt is not superclass of Rf
val v4: Covariant<SubSubBase> = Covariant<Base>()       // error  Rf Base       -> Rt SubSubBase, bc Rt is not superclass of Rf
val v5: Covariant<SubBase>    = Covariant<SubSubBase>() // OK     Rf SubSubBase -> Rt SubBase, bc Rt is a superclass of Rf

// * <OUT R: SubBase>
// 'out' must be a Supertype of generic param, restricted to class and subclasses of SubBase
class Covariant2<out R: SubBase> {
    fun f(t: Int) : R { throw Exception() }
}




//  in T, out R : SubBase  // output R is a subclass of 'SubBase'
val w1: Crossvariant<SubBase,    SubBase>    = Crossvariant<Base,    SubBase>()    // OK
val w2: Crossvariant<SubBase,    SubBase>    = Crossvariant<Base,    Base>()       // Error, SubBase is minimum class for R
val w3: Crossvariant<SubBase,    SubBase>    = Crossvariant<Base,    SubSubBase>() // OK
val w4: Crossvariant<SubSubBase, SubBase>    = Crossvariant<Base,    SubSubBase>() // OK
val w5: Crossvariant<SubBase,    SubBase>    = Crossvariant<SubBase, SubSubBase>() // OK
val w6: Crossvariant<SubSubBase, SubBase>    = Crossvariant<SubBase, SubSubBase>() // OK
val w7: Crossvariant<Base,       SubSubBase> = Crossvariant<Base, SubSubBase>()    // OK, Equal classes always work
val w8: Crossvariant<Base,       SubBase>    = Crossvariant<Base, SubBase>()       // OK, Equal classes always work
val w9: Crossvariant<Base,       SubSubBase> = Crossvariant<Base, SubBase>()       // Error, R SubBase > R SubSubBase (upper bound reached)
val x6: Crossvariant<Base,       SubSubBase> = Crossvariant<Base, SubSubSubBase>() // OK
val w6: Crossvariant<SubBase,    SubSubBase> = Crossvariant<Base, SubSubSubBase>() // OK
val w6: Crossvariant<SubBase,    SubSubBase> = Crossvariant<SubBase, SubSubSubBase>() // OK
val w5: Crossvariant<SubBase,    SubBase>    = Crossvariant<SubBase, Base>()       // Error, R SubBase < R Base (out of bounds)

// in T : SubBase, out R  // input T is a subclass of 'SubBase'
val v7: Crossvariant2<SubBase,    SubBase> = Crossvariant2<Base,    SubSubBase>() // error, 'Base' not below sub bounds
val v8: Crossvariant2<SubBase,    SubBase> = Crossvariant2<SubBase, SubSubBase>() // OK, 'in' subtype is SubBase or sub type
val v9: Crossvariant2<SubSubBase, SubBase> = Crossvariant2<SubBase, SubSubBase>() //