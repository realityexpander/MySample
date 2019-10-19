interface InterfaceName {
    abstract val name: String // var must be in scope of implementing class

    fun displayName() {
        println("<InterfaceName> displayName() $name")
    }
}
interface InterfaceSSN : InterfaceName {
    abstract val ssn: String // found in scope of implementing class

    fun displaySSN() {
        println("<InterfaceSSN> displaySSN() $name's ssn=$ssn")
    }
}
interface InterfaceJob : InterfaceSSN {
    abstract var job: String // found in scope of the implementing class

    fun displayJob() {
        println("<InterfaceJob> displayJob() $name's job is $job, slave=$ssn")
    }
}
interface InterfaceCar {
    abstract var displayCarCount: Int // found in scope of the implementing class
    abstract val car: String          // found in scope of the implementing class

    fun displayCar(message: String = "Default Car Message") {
        displayCarCount += 1
        println("<InterfaceCar> displayCar(message) car=$car, message=\"$message\", displayCarCount=$displayCarCount")
    }
    fun displayCar() {
        this.displayCar("<InterfaceCar>starting displayCarCount=$displayCarCount")
    }
}
interface InterfaceJobAndCar : InterfaceJob, InterfaceCar{
    fun displayJobAndCarInit() : Boolean // Implementing Class must implement this fun signature

    fun displayJobAndCar(){ // Default Fun provided
//        displayJob()
//        displayCar()
        println("<InterfaceJobAndCar> displayJobAndCar() ***** $name's job/car/ssn=$job/$car/$ssn *****")
    }
}

interface InterfaceDNDStats : InterfaceName {
    abstract var stats: DNDStats

    fun displayDNDStats() {
        with (stats) {
            println("DND Stats for $name: STR:$valSTR, DEX:$valDEX, CHR:$valCHR, HP:$valHP")
        }
    }
}

data class Position(var name: String, var position: String)
data class DNDStats( var valSTR: Int,
                    var valDEX: Int,
                    var valCHR: Int,
                    var valHP: Int )

class PersonDNDWithJob(override val name: String, // InterfaceName
                       var position: Position,  // InterfaceDNDStats
                       override var stats: DNDStats,
                       override val ssn: String,  // InterfaceDNDStats
                       override var job: String  // InterfaceJob, var stats: DNDStats){}
) :  InterfaceJob, InterfaceDNDStats {
    init {
        println("A New hero Enters: $name")
    }

    fun displayPosition() {
        println("Position for $name: ${position.name}@${position.position}")
    }
}

abstract class AbstractPerson(name: String) {
    private var _name: String = name // constructed here
    val name: String // constructed here
        get() = _name

    internal fun setName(name: String) {
        _name = name
    }

    init {
        println("\n<AbstractPerson> NEW person: name=$name.")
    }

    override fun toString(): String {
        return "name=$name"
    }
}

abstract class AbstractPersonWithJob : AbstractPerson, InterfaceJob {
    final override val ssn: String  // constructed here
    final override var job: String  // constructed here

    constructor(name: String, ssn: String, job: String) : super(name) {
        this.ssn = ssn
        this.job = job
        this.setName("$name ***")
        println("<AbstractPersonWithJob> NEW personWithJob: ${this.name}, ssn=$ssn and my job is $job")
    }
    constructor(name: String,
                job: String) : this(name, "NO SSN ASSIGNED", job)
    constructor(name:String) : this(name, "NO SSN ASSIGNED", "NO JOB ASSIGNED")

//    override fun displaySSN() {
//        println("<AbstractPerson> DEFAULT message: $name's ssn is $ssn, job is $job ")
//    }

    override fun toString(): String {
        return "ssn=$ssn, job=$job" + super<AbstractPerson>.toString()
    }

}


class Intern(name: String, // passed to the AbstractPerson constructor first
             override val ssn: String,
             override var job: String,
             private var gradYear: Int
            ): AbstractPerson(name),
                   InterfaceJob {
    init {
        println("<Intern> name=$name, ssn=$ssn, gradYear=$gradYear")
    }

    fun displayGradYear() {
        println("<Intern> displayGradYear() name=$name, Grad Year = $gradYear")
    }

    override fun toString(): String {
        return "job=$job, ssn=$ssn, gradYear=${gradYear}, " + super<AbstractPerson>.toString()
    }
}

class Student(name: String, // created in AbstractPerson
              override val ssn: String, // created in IDisplaySSN
              val gradeLevel: Int,
              val chore: String = "NO CHORES ASSIGNED",
              override var car: String = "Ferrari" // created in IDisplayCar
            ): AbstractPerson(name), InterfaceCar, InterfaceSSN {

    override var displayCarCount: Int = 0

    constructor(name: String,
                ssn: String,
                gradeLevel: Int
               ): this(name, ssn, gradeLevel, "NO CHORES ASSIGNED") {
        println("<Student> CONSTRUCTOR ${this.name}")
    }

    init{
        println("<Student> NEW student: $name in grade $gradeLevel ")
    }

    override fun displaySSN() {
        println("<Student> $name's SSN is scrambled ${
                ssn.toString()
                .toCharArray()
                .map{c: Char -> c+20 }
                .toCharArray()
                .joinToString("") 
        }")
    }

    fun displayChore() {
        println("<Student> $name has chore $chore is in grade $gradeLevel")
    }

//    override fun displayCar() {
////        super.displayCar(this.car) // call interface method
//        println("<Student> displayCar() $name's car is a $car, x=$x")
//    }

}

class Teacher(name: String,
              ssn: String,
              job: String,
              private val payRate: Int
             ): AbstractPersonWithJob(name, ssn, job) {

    init{
        println("<Teacher> NEW teacher: $name with job=$job ssn=$ssn in being paid $payRate")
    }

//    override fun displayJob() {
////        super.displayJob()
//        println("<Teacher> displayJob() $name has Job:$job ssn=$ssn, pay=$$payRate")
//    }
}

class Parent(name: String,
             job: String
            ) : AbstractPersonWithJob(name, job), InterfaceJobAndCar {
    override val car: String = "minivan" // for IDisplay Car
    override var displayCarCount: Int = 0 // for IDisplay Car

    init{
        println("<Parent> NEW Parent: $name and job is $job")
    }

    override fun displayJobAndCarInit(): Boolean {
        println("<Parent> displayJobAndCarInit() called")
        return true
    }

//    override fun displayJob() {
//        //println("AbstractWithJob Route")
//        //super<AbstractPersonWithJob>.displayJob()
//
//        //println("IDisplayJobAndCar Route")
//        super<IDisplayJobAndCar>.displayJob()
//
//        //println("<Parent> $name has Job: $job, ssn=$ssn")
//    }

}

class PersonWithJobViaAbstractClass(name: String,
                                    ssn: String,
                                    job: String
                                   ) : AbstractPersonWithJob(name, ssn, job) {
    init {
        println("<PersonWithJobViaAbstractClass> NEW Person:$name, job=$job, ssn=$ssn")
    }

    override fun toString(): String {
        return "ssn=$ssn, job=$job, " + super.toString()
    }
}

class PersonWithJobViaInterfaces(name: String,
                                 override val ssn: String,
                                 override var job: String
                                ) : AbstractPerson(name), InterfaceJob {

    init {
        println("<PersonWithJobViaInterfaces> NEW Person:$name, job=$job, ssn=$ssn")
    }

    override fun toString(): String {
        return "ssn=$ssn, job=$job, " + super.toString()
    }
}

class PersonWithCar(name: String,
                    override val car: String = "Unknown Car"
                   ) : AbstractPerson(name), InterfaceCar {

    override var displayCarCount: Int = 0

    init {
        println("<PersonWithCar> NEW PersonWithCar: $name")
    }

    override fun toString(): String {
        return "car=$car, " + super<AbstractPerson>.toString()
    }
}

class PersonWithJobAndCar(name: String,
                          override val ssn: String,
                          override var job: String,
                          override val car: String = "Unknown Car"
                          ) : AbstractPerson(name),
                                 InterfaceJobAndCar {

    override fun displayJobAndCarInit(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override var displayCarCount: Int = 0

    init {
        println("<PersonWithJobAndCar> NEW PersonWithJobAndCar: $name")
    }

    override fun toString(): String {
        return "ssn=$ssn, job=$job, car=$car, " + super<AbstractPerson>.toString()
    }
}


fun doTeacherStudent() {
    val teach = Teacher("teach Ms. Mills", "123-56-7890", "Math Teacher", 10000)
    val student = Student("s1 Little Jimmy", "223-32-1234", 2, "Erase chalkboards")
    val student2 = Student("s2 Useless Johnny", "123-232-5634", 3)
    student2.car = "Old Junker Ford"
    val parent = Parent("parent Mr. BigShot", "Banker")
    parent.job = "Unemployed"
    val personNamedWithCar = PersonWithCar("personJobAndCar Mr. Nobody", "Black Limousine")

    val personWithJobAndCar = PersonWithJobAndCar("personWithJobAndCar Mr. Somebody", "123-23-3232", "Butcher", "Red Jeep")
        personWithJobAndCar.job = "Simple Job"

    val personWithJobAbs = PersonWithJobViaAbstractClass("personWithJobViaAbstracts Good Ol' Joe", "123-23-1223", "Jet mechanic")
    val personNamedWithJobIntf = PersonWithJobViaInterfaces("personWithJobViaInterfaces Good Ol' Jack", "123-54-3434", "Car Junker")
    val intern = Intern("intern Sally", "454-34-23443", "Ms. Mills Class", 2022)
    intern.setName("Little sally")

    val stats = DNDStats(10, 20, 20, 10)
    val personDND = PersonDNDWithJob("Victorious Hero", Position("This is it", "my job"), stats, "123-23-2211", "Teleprompter" )

    println("\n")

    personDND.displayDNDStats()
//    personDND.displayJob()
//    personDND.displayPosition()

//    teach.displayJob()
//    teach.displaySSN()

//    student.displayJob()
//    student.displaySSN()
//    student.displayChore()
//    student.displayCar()

//    student2.displayJob()
//    student2.displaySSN()
//    student2.displayChore()
//    student2.displayCar()
//      println(with(student2) {"student2 gradeLevel=${gradeLevel}, chore=${chore}"} )

//    personNamedWithCar.displayCar()
//    println(personNamedWithCar.toString())

//    personWithJobAndCar.displayJobAndCar()

//    parent.displayJob()
//    parent.displaySSN()
//    parent.displayJobAndCar()

//    person.displayJob()
//    person.displaySSN()

//    personNamedWithJobIntf.displayJob()
//    personNamedWithJobIntf.displaySSN()
//    println(personNamedWithJobIntf.toString())

//    println()
//    personWithJobAbs.displayName()
//    personWithJobAbs.displayJob()
//    personWithJobAbs.displaySSN()
//    println(personWithJobAbs.toString())

//    personNamed.setName(foo)
//    println(personNamed.toString())

//    println(intern.toString())
//    intern.displayGradYear()
//    intern.displayJob()
//    intern.displaySSN()
}