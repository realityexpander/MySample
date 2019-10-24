
class Box<T>(var item: T)

open class Animal( var sex: String = "Species Default" ) {

//    fun <T : Animal> describe(item: T) {
//        println("  describe() <Animal>=${item.sex}")
//    }


    open fun describe() {
        println("  describe() <Animal>=${this.sex}")
    }

}

open class Mammal( var species: String = "Mammal Default") : Animal("Sex Unknown") {

    override fun describe() {
        println("  describe() <Mammal>=${this.species}")
    }
}

open class Dog( var kind: String = "Kind Default") : Mammal("Canine") {
//    fun <T : Dog> describe(item: T) {
//        println("  describe() <Dog>=${item.kind}")
//    }

    override fun describe() {
        println("  describe() <Dog>=${this.kind}")
    }
}
open class Cat( var collar: String = "Collar Default") : Mammal("Feline") {
//    fun <T : Cat> describe(item: T) {
//        println("  describe() <Cat>=${item.collar}")
//    }

    override fun describe() {
        println("  describe() <Cat>=${this.collar}")
    }
}

class Kitten( var toy: String = "Toy Default") : Cat("Collar Diamonds") {
    fun <T : Kitten> describe(item: T) {
        println("  describe() <Kitten>=${item.toy}")
    }

    override fun describe() {
        println("  describe() <Kitten>=${this.toy}")
    }
}

// Forces to only most general type (Animal), returns specific description based on type
fun examine(boxed: Box<out Animal>) { // accepts Animal or any subtype of Animal
    val animal: Animal = boxed.item

    println("examine($boxed) animal.species=${animal.sex}")

    when (animal) { // reverse hierarchical order (deepest subclass first)
        is Kitten -> animal.describe()
        is Dog    -> animal.describe()
        is Cat    -> animal.describe()
        is Mammal -> animal.describe()
        else      -> animal.describe()
    }

//     boxed.item = Animal() //not allowed, "Setter removed by type projection"
}

fun set(boxed: Box<in Animal>, item: Animal) {
    boxed.item = item
    println("<in Animal> set(${item.sex})")
}

fun <T : Mammal> set(boxed: Box<in T>, item: T) {
    boxed.item = item
    println("<T : Mammal> set(${item.sex}, ${item.species})")
}

//fun set(boxed: Box<in Mammal>, item: Mammal) {
//    boxed.item = item
//    println("<in Mammal> set(${item.species}, ${item.sex})")
//}

//// Automatically select the most specific for "in"
//fun set(boxed: Box<in Dog>, item: Dog) {
//    boxed.item = item
//    println("<in Dog> set(${item.species},${item.kind})")
//}
//
//fun set(boxed: Box<in Cat>, item: Cat) {
//    boxed.item = item
//    println("<in Cat> set(${item.species},${item.collar})")
//}

fun <T : Cat> set(boxed: Box<in T>, item: T) {
    boxed.item = item
    println("<T : Cat> set(${item.sex}, ${item.species}, ${item.collar})")
}

fun <T : Kitten> set(boxed: Box<in T>, item: T) {
    boxed.item = item
    println("<in Kitten> set(${item.sex},${item.collar},${item.toy})")
}




fun mainTypeProjections() {

    val animalBox = Box(Animal("Species Squid"))
    val mammalBox = Box(Mammal("Sex female"))
    val dogBox       = Box(Dog("Kind Terrier"))
    val catBox       = Box(Cat("Collar Diamond"))
    val kittenBox = Box(Kitten("Toy squeak mouse"))

    examine(animalBox)
    examine(mammalBox)
    examine(dogBox)
    examine(catBox)
    examine(kittenBox)

    println()

//    set(animalBox, Animal("Species Insect"))
    set(mammalBox, Mammal("Sex third-sex"))
//    set(catBox, Cat("Collar Plastic"))
//    set(mammalBox, Dog("Kind Cocker Spaniel"))
//    set(animalBox, Kitten("Toy lil ball x1"))
//    set(kittenBox, Kitten("Toy lil ball x2"))

    println()

    mammalBox.item.describe()
//    catBox.item.describe()
//    (animalBox.item).describe()
    //(animalBox.item as Kitten).describe(/*animalBox.item*/)
//    (kittenBox.item).describe()
}