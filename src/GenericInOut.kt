// https://stackoverflow.com/questions/36155108/kotlin-generics-in-kfunction1
import kotlin.Exception

class Invariant<T>  {
    fun f(i: Int): T { throw Exception() } // OK
    fun f(t: T): Int { throw Exception() } // OK
}

class Contravariant<in T> {
//    fun f(i: Int): T { throw Exception() } // error, T cannot be returned, its an 'in' type only
    fun f(t: T): Int { throw Exception() } // OK, T is parameter type
}

class Covariant<out T> {
    fun f(i: Int): T { throw Exception() } // OK, T is returned
//    fun f(t: T): Int { throw Exception() } // error, T cannot be parameter type, 'out' type only
}

// 'in' are input parameter types, 'out' are for return types
// <in T, out R : onlyTypeOrSubtypeOfR>  // note: T alone means onlyTypeOrSupertype of T
class Crossvariant<in T, out R : SubBase> {
    fun f(t: T): R { throw Exception() } // OK, R is returned
//    fun f(t: R): T { throw Exception() } // error, R is in 'in' position, T is in 'out' position
}

// in T : onlyTypeOrSubtype, out R
class Crossvariant2<in T : SubBase, out R > {
    fun f(t: T): R { throw Exception() } // OK, R is returned
//    fun f(t: R): T { throw Exception() } // error, R is in 'in' position, T is in 'out' position
}

// in T : onlyTypeOrSubtype, out R
class Crossvariant3<in T : Base, out R > {
    fun f(t: T): R { throw Exception() }
}

open class Base
open class SubBase: Base()
open class SubSubBase: SubBase()
open class SubSubSubBase: SubSubBase()

val i0: Invariant<Base>    = Invariant<Base>()    // OK     Base    -> Base
val i3: Invariant<SubBase> = Invariant<SubBase>() // OK     SubBase -> SubBase
val i1: Invariant<Base>    = Invariant<SubBase>() // error  SubBase -> Base
val i2: Invariant<SubBase> = Invariant<Base>()    // error  Base    -> SubBase

// * IN
// Keyword 'in' must be a Supertype of generic type
val n2: Contravariant<SubBase>    = Contravariant<Base>()    // OK     in Base    -> in SubBase
val n3: Contravariant<SubSubBase> = Contravariant<Base>()    // OK     in Base    -> in SubSubBase
val n1: Contravariant<Base>       = Contravariant<SubBase>() // error  in SubBase -> in Base

// * OUT
// Keyword 'out' must be a Subtype of generic type
val v1: Covariant<Base>       = Covariant<SubBase>() // OK     out SubBase    -> out Base
val v2: Covariant<SubBase>    = Covariant<Base>()    // error  out Base       -> out SubBase
val v3: Covariant<SubSubBase> = Covariant<Base>()    // error  out Base       -> out SubSubBase

//  in T, out R : SubBase  // output R is a subclass of 'SubBase'
val v4: Crossvariant<SubBase,    SubBase>    = Crossvariant<Base,    SubBase>()    // OK
val v4: Crossvariant<SubBase,    SubBase>    = Crossvariant<Base,    Base>()       // Error, SubBase is minimum class for R
val a1: Crossvariant<SubBase,    SubBase>    = Crossvariant<Base,    SubSubBase>() // OK
val a1: Crossvariant<SubSubBase, SubBase>    = Crossvariant<Base,    SubSubBase>() // OK
val a1: Crossvariant<SubBase,    SubBase>    = Crossvariant<SubBase, SubSubBase>() // OK
val v5: Crossvariant<SubSubBase, SubBase>    = Crossvariant<SubBase, SubSubBase>() // OK
val v6: Crossvariant<Base,       SubSubBase> = Crossvariant<Base, SubSubBase>()    // OK
val v6: Crossvariant<Base,       SubBase>    = Crossvariant<Base, SubBase>()       // OK
val v6: Crossvariant<Base,       SubSubBase> = Crossvariant<Base, SubBase>()       // Error, R SubBase > R SubSubBase (upper bound reached)
val v6: Crossvariant<Base,       SubSubBase> = Crossvariant<Base, SubSubSubBase>() // OK
val v6: Crossvariant<SubBase,    SubSubBase> = Crossvariant<Base, SubSubSubBase>() // OK
val v6: Crossvariant<SubBase,    SubSubBase> = Crossvariant<SubBase, SubSubSubBase>() // OK
val v5: Crossvariant<SubBase,    SubBase>    = Crossvariant<SubBase, Base>()       // Error, R SubBase < R Base (out of bounds)

// in T : SubBase, out R  // input T is a subclass of 'SubBase'
val v7: Crossvariant2<SubBase,    SubBase> = Crossvariant2<Base,    SubSubBase>() // error, 'Base' not below sub bounds
val v8: Crossvariant2<SubBase,    SubBase> = Crossvariant2<SubBase, SubSubBase>() // OK, 'in' subtype is SubBase or sub type
val v9: Crossvariant2<SubSubBase, SubBase> = Crossvariant2<SubBase, SubSubBase>() //