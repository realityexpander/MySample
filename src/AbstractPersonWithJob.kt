import kotlin.reflect.full.*
import kotlin.reflect.jvm.jvmName

interface InterfaceName {
    abstract val name: String // var must be in scope of implementing class

    fun displayName() {
        println("<InterfaceName> displayName() $name")
    }

//    abstract fun setName(s: String)
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

    fun quitJob() {
        job = "<InterfaceJob> quitJob() - Unemployed"
    }
}
interface InterfaceVehicle {
    abstract val vehicleType: String// found in scope of the implementing class
    abstract var numWheels: Int

    fun displayVehicle(extraInfo: String) {
        println("<InterfaceVehicle> displayVehicle() vehicle=$vehicleType numWheels=$numWheels extraInfo=$extraInfo")
    }

}
interface InterfaceCar : InterfaceVehicle {
    abstract var displayCarCount: Int // found in scope of the implementing class
    abstract val car: String          // found in scope of the implementing class

    fun displayCar(message: String = "Default Car Message") {
        displayCarCount += 1
        println("<InterfaceCar> displayCar(message) car=$car, message=\"$message\", displayCarCount=$displayCarCount")
    }
    fun displayCar() {
        displayVehicle(car)
        displayCar("displayCarCount=$displayCarCount")
        numWheels += 1
    }
}
interface InterfaceJobAndCar : InterfaceJob, InterfaceCar{
    fun displayJobAndCarInit() : Boolean // Implementing Class must implement this fun signature

    fun displayJobAndCar(){ // Default Fun provided
        displayJob()
        displayCar()
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

abstract class AbstractPerson(name: String) {
//    private var _name: String = name // constructed here
//    val name: String // constructed here
//        get() = _name
//    internal fun setName(name: String) {
//        _name = name
//    }

    var name: String = name
        internal set

    private var dingDangIt: String = "Dang" // Showing abstract class state


    init {
        println("\n<AbstractPerson> init() person: name=$name. [$dingDangIt]")
        dingDangIt = "Ding"
    }

    fun getDing(): String {
        return "[$dingDangIt]"
    }

    override fun toString(): String {
        return "name=$name"
    }
}

open class Person(name:String) {
    private var _name: String = name // constructed here
    val name: String // constructed here
        get() = _name

    internal fun setName(name: String) {
        _name = name
    }

    init {
        println("\n<Person> init() person: name=$name.")
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
        this.name ="$name ***"
        println("<AbstractPersonWithJob> constructor() name=${this.name}, ssn=$ssn and my job is $job")
    }
    constructor(name: String,
                job: String) : this(name, "NO SSN ASSIGNED", job)
    constructor(name:String) : this(name, "NO SSN ASSIGNED", "NO JOB ASSIGNED")

//    override fun displaySSN() {
//        println("<AbstractPerson> DEFAULT message: $name's ssn is $ssn, job is $job ")
//    }

//    override fun quitJob() {
//        job = "<AbstractPersonWithJob> quitJob() - Unemployed"
//    }

    override fun toString(): String {
        return "ssn=$ssn, job=$job, " + super.toString()
    }

}

class PersonDNDWithJob(name: String, // InterfaceName
                       var position: Position,  // InterfaceDNDStats
                       override var stats: DNDStats,
                       override val ssn: String,  // InterfaceDNDStats
                       override var job: String  // InterfaceJob, var stats: DNDStats){}
) :  AbstractPerson(name), InterfaceJob, InterfaceDNDStats {
    init {
        println("A New hero Enters: $name")
    }

    fun displayPosition() {
        println("Position for $name: ${position.name}@${position.position}")
    }
}

class Intern(name: String, // passed to the AbstractPerson constructor first
             override val ssn: String,
             override var job: String,
             private var gradYear: Int
            ): AbstractPerson(name),
                   InterfaceJob {
    init {
        println("<Intern> init() name=$name, ssn=$ssn, gradYear=$gradYear")
    }

    fun displayGradYear() {
        println("<Intern> displayGradYear() name=$name, Grad Year = $gradYear")
    }

    override fun toString(): String {
        return "job=$job, ssn=$ssn, gradYear=${gradYear}, " + super<AbstractPerson>.toString()
    }
}

class StudentViaOnlyInterfaces(override val name: String,
                               override val ssn: String,
                               override val car: String = "Unknown Car"
                           ) : InterfaceSSN, InterfaceCar {
    override var vehicleType: String = "Unknown Vehicle Type"
    override var numWheels: Int = 4
    override var displayCarCount: Int = 0

}

class Student(name: String, // created in AbstractPerson
              override val ssn: String, // created for InterfaceSSN
              val gradeLevel: Int,
              val chore: String = "NO CHORES ASSIGNED",
              override var car: String = "Ferrari", // created for InterfaceCar
              override val vehicleType: String  = "Unknown Vehicle"// created for InterfaceVehicle
): AbstractPerson(name), InterfaceCar, InterfaceSSN {

    override var displayCarCount: Int = 0
    override var numWheels: Int = 4

    constructor(name: String,
                ssn: String,
                gradeLevel: Int
               ): this(name,
                        ssn,
                        gradeLevel,
                        "NO CHORES ASSIGNED",
                        "NO CAR ASSIGNED",
                        "NO VEHICLE ASSIGNED"
            ) {
        println("<Student> CONSTRUCTOR ${this.name}")
    }

    init{
        println("<Student> init() student: $name in grade $gradeLevel ")
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
        println("<Teacher> init() teacher: $name with job=$job ssn=$ssn in being paid $payRate")
    }

//    override fun displayJob() {
////        super.displayJob()
//        println("<Teacher> displayJob() $name has Job:$job ssn=$ssn, pay=$$payRate")
//    }

    override fun quitJob() {
        println("<Teacher> Called quitJob()")
        super.quitJob()
    }
}

class Parent(name: String,
             job: String
) : AbstractPersonWithJob(name, job), InterfaceJobAndCar {
    override val car: String = "Minivan" // for IDisplay Car
    override var displayCarCount: Int = 0 // for IDisplay Car
    override val vehicleType: String = "UNknown Vehicle Type"
    override var numWheels: Int = 4

    init{
        println("<Parent> init() Parent: $name and job is $job")
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
        this.job = name.reversed()
        println("<PersonWithJobViaAbstractClass> init() name=$name, job=$job, ssn=$ssn")
    }

//    fun quitJob() {
//        job = "Unemployed"
//    }

    override fun toString(): String {
        return "<PersonWithJobViaAbstractClass> toString(), " + super.toString()
    }
}

class PersonWithJobViaInterfaces(override val name: String,
                                 override val ssn: String,
                                 override var job: String
) :  InterfaceJob {

    init {
        println("<PersonWithJobViaInterfaces> init() Person:$name, job=$job, ssn=$ssn")
    }

    override fun toString(): String {
        return "ssn=$ssn, job=$job, " + super.toString()
    }
}

class PersonWithCar(name: String,
                    override val car: String = "Unknown Car",
                    override val vehicleType: String = "Unknown Car"
) : AbstractPerson(name), InterfaceCar {

    override var numWheels: Int = 4
    override var displayCarCount: Int = 0

    init {
        println("<PersonWithCar> init() PersonWithCar: $name")
    }

    override fun toString(): String {
        return "car=$car, " + super<AbstractPerson>.toString()
    }
}

class PersonWithJobAndCar(name: String,
                          override val ssn: String,
                          override var job: String,
                          override val car: String = "Unknown Car",
                          override val vehicleType: String = "Unknown Car"
                         ) : AbstractPerson(name),
                                 InterfaceJobAndCar {
    override var numWheels: Int = 4

    override fun displayJobAndCarInit(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override var displayCarCount: Int = 0

    init {
        println("<PersonWithJobAndCar> init() PersonWithJobAndCar: $name")
    }

    override fun toString(): String {
        return "ssn=$ssn, job=$job, car=$car, " + super<AbstractPerson>.toString()
    }
}


fun doTeacherStudent() {
    val teach = Teacher("teach Ms. Mills", "123-56-7890", "Math Teacher", 10000)
//    val student = Student("s1 Little Jimmy", "223-32-1234", 2, "Erase chalkboards")
//    val student2 = Student("s2 Useless Johnny", "123-232-5634", 3)
//    student2.car = "Old Junker Ford"
//    val parent = Parent("parent Mr. BigShot", "Banker")
//    parent.job = "Unemployed"
//    val personWithCar = PersonWithCar("personJobAndCar Mr. Nobody",
//            "Black Limousine",
//            vehicleType = "1988 Fleetwood")

//    val studentViaInterfaces = StudentViaOnlyInterfaces("StudentViaInterfaces Billy Jo",
//            "396-53-2342",
//            "Dune Buggy Kit Car")
//    studentViaInterfaces.vehicleType = "Rad '60s runabout"

//    val personWithJobAndCar = PersonWithJobAndCar("personWithJobAndCar Mr. Somebody", "123-23-3232", "Butcher", "Red Jeep", "Sport Utility")
//        personWithJobAndCar.job = "Simple Job"

//    val personWithJobAbs = PersonWithJobViaAbstractClass("personWithJobViaAbstract Ol' Joe", "123-23-1223", "Jet mechanic")
//    val personNamedWithJobInt = PersonWithJobViaInterfaces("personWithJobViaInterfaces Good Ol' Jack", "123-54-3434", "Car Junker")
//    val intern = Intern("intern Sally", "454-34-23443", "Ms. Mills Class", 2022)
//    intern.setName("Little sally")
//
    val stats = DNDStats(10, 20, 20, 10)
    val personDND = PersonDNDWithJob("Victorious Hero",
                        Position("This is it", "my job"),
                        stats, "123-23-2211",
                        "Dragon Slayer" )
    personDND.name = "Slayer of Dragon"

    println("\n")

//    personDND.displayDNDStats()
//    personDND.displayJob()
//    personDND.displayPosition()

    teach.displayJob()
    teach.displaySSN()
    println("teach.dingDangIt=${teach.getDing()}")

//    student.displayJob()
//    student.displaySSN()
//    student.displayChore()
//    student.displayCar()

//    student2.displayJob()
//    student2.displaySSN()
//    student2.displayChore()
//    student2.displayCar()
//      println(with(student2) {"student2 gradeLevel=${gradeLevel}, chore=${chore}"} )

//    personWithCar.displayCar()
//    println(personWithCar.toString())
//    println("${personWithCar.numWheels}, ${personWithCar.displayCarCount}")

//    studentViaInterfaces.displayCar()
//    println(studentViaInterfaces.numWheels)


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
//    personWithJobAbs.quitJob()
//    println(personWithJobAbs.toString())

//    personNamed.setName(foo)
//    println(personNamed.toString())

//    println(intern.toString())
//    intern.displayGradYear()
//    intern.displayJob()
//    intern.displaySSN()
}