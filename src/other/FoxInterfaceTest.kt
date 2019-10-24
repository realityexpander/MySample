package other

//// Saving state in interfaces - Not Approved
//// https://blog.kotlin-academy.com/abstract-class-vs-interface-in-kotlin-5ab8697c3a14
//interface AnimalInterface {
//    var name: String
//        get() = names[this] ?: "Default name"
//        other.set(value) { names[this] = value
//            refs++
//        }
//
//    var type: String
//        get() = types[this] ?: "Default type"
//        other.set(value) { types[this] = value
//            refs++
//        }
//
//    val fullName: String
//        get() = "$name of type $type, refs=$refs"
//
//    companion object { // Global for all AnimalInterface users
//        // Data is saved in this static companion object as a mutable list of class objects
//        // based on the enclosing objects' scope.
//        private var refs = 0
//        private val names = mutableMapOf<Any, String>()
//        private val types = mutableMapOf<Any, String>()
//    }
//}
//
//class Fox: AnimalInterface
//class other.Dog: AnimalInterface
//
//fun main0(args: Array<String>) {
//    val fox = Fox()
//    fox.name = "Sox"
//    fox.type = "Tibetan sand fox"
//
//    println(fox.fullName) // Sox of type Tibetan sand fox
//
//    val dog = other.Dog()
//    dog.name = "Billy"
//    dog.type = "other.Dog"
//
//    println(dog.fullName) // Billy of type other.Dog
//
//    println()
//}
//
//
//abstract class AnimalAbstract(
//    var name: String = "Default name"
//) {
//    var type: String = "Default type"
//
//    companion object { // Global for all users of AnimalAbtract
//        private var refs: Int = 0
//    }
//
//    val fullName: String
//        get() {
//            refs++
//            return "$name of type $type, refs=$refs"
//        }
//}
//
//class FoxViaAbstract(name: String): AnimalAbstract(name)
//class DogViaAbstract: AnimalAbstract()
//
//fun main2(args: Array<String>) {
//    val fox = FoxViaAbstract("Sox")
////    fox.name = "Sox"
//    fox.type = "Tibetan sand fox"
//
//    val dog = DogViaAbstract()
//    dog.name = "Billy"
//    dog.type = "other.Dog"
//
//    println(fox.fullName) // Sox of type Tibetan sand fox
//    println(dog.fullName) // Billy of type other.Dog
//}