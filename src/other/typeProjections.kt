package other

class Box<T>(var item: T)

open class Animal( var sex: String = "Species Default" ) {
    open fun describe() = println("  describe() <other.Animal>=${this.sex}")
}

open class Mammal( var species: String = "Mammal Default") : Animal("Sex Unknown") {
    override fun describe() = println("  describe() <mammals.Mammal>=${this.species}")
}

open class Dog( var kind: String = "Kind Default") : Mammal("Canine") {
    override fun describe() = println("  describe() <other.Dog>=${this.kind}")
}
open class Cat( var collar: String = "Collar Default") : Mammal("Feline") {
    override fun describe() = println("  describe() <other.Cat>=${this.collar}")
}

class Kitten( var toy: String = "Toy Default") : Cat("Collar Diamonds") {
    override fun describe() = println("  describe() <other.Kitten>=${this.toy}")
}

// Forces to only accept most general type (other.Animal), returns specific description based on input class type
fun examine(boxed: Box<out Animal>) { // accepts other.Animal or any subtype of other.Animal
    val animal: Animal = boxed.item

    println("other.examine($boxed) animal.species=${animal.sex}")

    when (animal) { // reverse hierarchical order (deepest subclass first)
        is Kitten -> animal.describe()
        is Dog -> animal.describe()
        is Cat -> animal.describe()
        is Mammal -> animal.describe()
        else      -> animal.describe()
    }

//     boxed.item = other.Animal() //not allowed, "Setter removed by type projection", only "out" is allowed.
}

//fun other.set(boxed: other.Box<in other.Animal>, item: other.Animal) {
//    boxed.item = item
//    println("<in other.Animal> other.set(${item.sex})")
//}
fun <T : Animal> set(boxed: Box<in T>, item: T) {
    boxed.item = item
    println("<T : other.Animal> other.set(${item.sex})")
}

//fun other.set(boxed: other.Box<in mammals.Mammal>, item: mammals.Mammal) {
//    boxed.item = item
//    println("<in mammals.Mammal> other.set(${item.species}, ${item.sex})")
//}
fun <T : Mammal> set(boxed: Box<in T>, item: T) {
    boxed.item = item
    println("<T : mammals.Mammal> other.set(${item.sex}, ${item.species})")
}

//// Automatically select the most specific for "in"
//fun other.set(boxed: other.Box<in other.Dog>, item: other.Dog) {
//    boxed.item = item
//    println("<in other.Dog> other.set(${item.species},${item.kind})")
//}
fun <T : Dog> set(boxed: Box<in T>, item: T) {
    boxed.item = item
    println("<T : other.Dog> other.set(${item.sex}, ${item.species}, ${item.kind})")
}

//fun other.set(boxed: other.Box<in other.Cat>, item: other.Cat) {
//    boxed.item = item
//    println("<in other.Cat> other.set(${item.species},${item.collar})")
//}

fun <T : Cat> set(boxed: Box<in T>, item: T) {
    boxed.item = item
    println("<T : other.Cat> other.set(${item.sex}, ${item.species}, ${item.collar})")
}

fun <T : Kitten> set(boxed: Box<in T>, item: T) {
    boxed.item = item
    println("<T : other.Kitten> other.set(${item.sex},${item.collar},${item.toy})")
}




fun mainTypeProjections() {

    val animalBox = Box(Animal("Species Squid"))
    val mammalBox = Box(Mammal("Sex female"))
    val dogBox       = Box(Dog("Kind Terrier"))
    val catBox       = Box(Cat("Collar Diamond"))
    val kittenBox = Box(Kitten("Toy squeak mouse"))

//    other.examine(animalBox)
//    other.examine(mammalBox)
//    other.examine(dogBox)
//    other.examine(catBox)
    examine(kittenBox)

    println()

    set(animalBox, Animal("Species Insect"))
    set(mammalBox, Mammal("Sex third-sex"))
    set(catBox, Cat("Collar Plastic"))
    set(dogBox, Dog("Kind Cocker Spaniel"))
    set(kittenBox, Kitten("Toy lil ball x2"))

    set(animalBox, Mammal("Mammal species"))
//    other.set(mammalBox, other.Animal("Test")) // not allowed
    set(mammalBox, Cat("Fido"))
//    other.set(catBox, mammals.Mammal("some mammal") ) // not allowed
    set(mammalBox, Kitten("Fluffy"))
//    other.set(kittenBox, mammals.Mammal("some mammal")) // not allowed

    println()

    mammalBox.item.describe()
//    catBox.item.describe()
    animalBox.item.describe()
    //(animalBox.item as other.Kitten).describe(/*animalBox.item*/)
    (kittenBox.item).describe()
}