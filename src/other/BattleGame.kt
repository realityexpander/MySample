package other

// From https://blog.kotlin-academy.com/inheritance-composition-delegation-and-traits-b11c64f11b27

interface IFighter {
    var lifePoints: Int
    fun attack(opponent: IFighter)
    fun turnEnd() {}

}

interface ICharacter : IFighter {
    val nameFighter: String
//        get() = this.javaClass.name
}

interface IWarrior : ICharacter {
    val strength: Int

    fun meleeAttack(opponent: IFighter) {
        println("$nameFighter melee attack with power $strength")
        opponent.lifePoints -= strength
    }
}

interface ISorcerer : ICharacter {
    val spell: Spell
    var manaPoints: Int

    fun canCastSpell() = manaPoints > spell.manaCost
    fun castSpell(opponent: IFighter) {
        if (manaPoints < spell.manaCost) {
            println("$nameFighter tried to cast spell ${spell.name} but not enough mana...")
            return
        }
        println("$nameFighter recites spell [${spell.name}] for damage: ${spell.strength}")
        manaPoints -= spell.manaCost
        opponent.lifePoints -= spell.strength
    }
    override fun turnEnd() {
        manaPoints += 1
    }
}

data class Spell(
    var manaCost: Int = 10,
    var strength: Int = 5,
    var name: String = "Magic Missile" )

data class Goblin(override var lifePoints: Int = 50,
                  override var nameFighter: String = "Blue other.Goblin"
) : ICharacter {

    override fun attack(opponent: IFighter) {
        println("$nameFighter attack (5)")
        opponent.lifePoints -= 5
    }
}

data class Minsk(override var lifePoints: Int = 60
) : IWarrior {
    override var nameFighter: String = this::class.simpleName.toString()
    override val strength: Int = 15

    override fun attack(opponent: IFighter) {
        meleeAttack(opponent)
    }
}

data class Wizard(override var lifePoints: Int = 50,
                  override val spell: Spell = Spell(),
                  override var manaPoints: Int = 20
) : ISorcerer {
    override var nameFighter: String = "Gandalf"

    override fun attack(opponent: IFighter) {
        println("The mighty wizard $nameFighter casts [${spell.name}] (${spell.strength})")
        castSpell(opponent)
    }
}

data class Artur(override var lifePoints: Int = 150,
                 override var manaPoints: Int = 10
) : IWarrior, ISorcerer {
    override var nameFighter: String = this::class.simpleName.toString()
    override var spell = Spell(4, 17, "Fireball")
    override val strength: Int = 5

    override fun attack(opponent: IFighter) {
        if (canCastSpell()) {
            castSpell(opponent)
        } else {
            meleeAttack(opponent)
        }
    }

    override fun turnEnd() {
        super<ISorcerer>.turnEnd()
        manaPoints += 1
    }
}


// Create abstract class extended from concrete class & interface
abstract class AbstractTeacherISorcerer(teacher: Teacher) : Teacher(teacher.namePerson, // + "<other.AbstractTeacherISorcerer>",
            teacher.ssn,
            teacher.job,
            teacher.payRate
           ), ISorcerer {
    override var lifePoints: Int = 20
    override var manaPoints: Int = 10
    override val spell: Spell = Spell(5, 5, "Pop Quiz")

    override fun attack(opponent: IFighter) {
        println("The other.Teacher $namePerson assigns [${spell.name}] (${spell.strength})")
        castSpell(opponent)
    }

    override fun toString(): String {
        return this::class.toString() +"(" +
               "lifePoints=$lifePoints, " +
               "manaPoints=$manaPoints, " +
               "spell=$spell, " + super.toString() + ")"
    }

}


abstract class AbstractTeacherIWarrior(teacher: Teacher) : Teacher(teacher.namePerson,
        teacher.ssn,
        teacher.job,
        teacher.payRate), IWarrior {
    override var nameFighter: String = "Extreme ${teacher.namePerson} ${this::class.simpleName.toString()}"
    override var strength: Int = 40

    override fun attack(opponent: IFighter) {
        println("The other.Teacher $namePerson SMACKS DOWN!")
        meleeAttack(opponent)
    }
}

class TeacherWarrior(teacher: Teacher) : AbstractTeacherIWarrior(teacher) {
    override var lifePoints: Int = 50
}

// Extend from abstract class which extends from concrete class & interface
class TeacherSorcerer(teacher: Teacher = Teacher()) : AbstractTeacherISorcerer(teacher) {
    override var nameFighter: String = teacher.namePerson //+ "<${this.javaClass.name}>"


    init {
        setLoadingCompleted(this)
    }
}

// Extend concrete class & use interfaces
class TeacherIWarriorISorcerer(teacher: Teacher = Teacher()
) : Teacher(teacher.namePerson, teacher.ssn, teacher.job, teacher.payRate),
        IWarrior, ISorcerer {
    override var lifePoints: Int = 40
    override val strength: Int = 10
    override val spell: Spell = Spell(3, 7, "Final Exam")
    override var manaPoints: Int = 10
    override val nameFighter: String = teacher.namePerson
            .reversed().toLowerCase().capitalize() // + "<${this.javaClass.name}>"


    init {
        setLoadingCompleted(this)
    }

    override fun attack(opponent: IFighter) {
        println("$nameFighter assigns [${spell.name}] (${spell.strength})")
        castSpell(opponent)
        opponent.lifePoints -= spell.strength
    }

    override fun toString(): String {
        return this::class.simpleName +"(" +
                "lifePoints=$lifePoints, " +
                "manaPoints=$manaPoints, " +
                "spell=$spell, " + super.toString() + ")"
    }
}

fun simulateCombat(args: Array<ICharacter> ) { // : other.ICharacter, c2: other.ICharacter) {

    var c1 = args[0]
    var c2 = args[1]

    println("\n***Fight Started***\n")
    while (c1.lifePoints > 0 && c2.lifePoints > 0) {
        c1.attack(c2)
        c2.attack(c1)
        c1.turnEnd()
        c2.turnEnd()

        println("${c1.nameFighter} HP=${c1.lifePoints}," +
                " ${c2.nameFighter} HP=${c2.lifePoints}")
    }
    val text = when {
        c1.lifePoints > 0 -> "Winner=$c1\nLoser=$c2"
        c2.lifePoints > 0 -> "Winner=$c2\nLoser=$c1"
        else -> "Both $c1 and $c2 are dead"
    }
    println(text)
}

fun mainBattleGame() {
    var artur = Artur()

    val teacher = Teacher("Ms. Mills", "123-56-7890", "Remedial Math", 10000)
    val teacherSorcerer = TeacherIWarriorISorcerer(teacher)
    val teacherSorcerer2 = TeacherWarrior(teacher)
    teacherSorcerer.setLoadingStatus("Loaded")
    teacherSorcerer2.setLoadingStatus("Loaded")

    println()
    simulateCombat(arrayOf(teacherSorcerer2, teacherSorcerer))
    simulateCombat(arrayOf(artur, teacherSorcerer))

    println()
    teacherSorcerer.displayJob()
//    other.simulateCombat(artur, other.Minsk())
//    other.simulateCombat(artur, other.Goblin())
//    other.simulateCombat(artur, other.Wizard())
}
