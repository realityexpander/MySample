package other

interface IName {
    abstract val namePerson: String // var must be in scope of implementing class

    fun displayName() {
        println("<InterfaceName> displayName() $namePerson")
    }
}

interface ISex : IName {
    abstract var sex: String
}

interface ISocSecNum : IName {
    abstract var ssn: String // found in scope of implementing class

    fun displaySSN() {
        println("<InterfaceSSN> displaySSN() $namePerson's ssn=$ssn")
    }
}

interface IJob       : ISocSecNum {
    abstract var job: String // found in scope of the implementing class

    fun displayJob() {
        println("<InterfaceJob> displayJob() $namePerson's job is $job, slave=$ssn")
    }

    fun quitJob() {
        job = "<InterfaceJob> quitJob() - Unemployed"
    }
}

interface IJobAndCar : IJob, ICar {
    fun displayJobAndCarInit() : Boolean // Implementing Class must implement this fun signature

    fun displayJobAndCar(){ // Default Fun provided
        displayJob()
        displayCar()
        println("<InterfaceJobAndCar> displayJobAndCar() ***** $namePerson's job/car/ssn=$job/$car/$ssn *****")
    }
}

interface IDNDStats  : IName {
    abstract var stats: DNDStats

    fun displayDNDStats() {
        with (stats) {
            println("DND Stats for $namePerson: STR:$valSTR, DEX:$valDEX, CHR:$valCHR, HP:$valHP")
        }
    }

    fun subtractHitPoints() {
        stats.valHP -= 10
        println("${stats.valHP}")
    }
}

interface IVehicle {
    abstract val vehicleType: String// found in scope of the implementing class
    abstract var numWheels: Int

    fun displayVehicle(extraInfo: String) {
        println("<InterfaceVehicle> displayVehicle() vehicle=$vehicleType numWheels=$numWheels extraInfo=$extraInfo")
    }

}

interface ICar  : IVehicle {
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

// I can't believe i wrote this function
//        this.setLoadingStatus("Loaded")
//        println("<${this.javaClass.name}> init() ${this.toString()}")
fun <T : AbstractPersonIName> setLoadingCompleted(localThis: T) {
    (localThis as T).setLoadingStatus("Loaded")
    println("* <${(localThis as T)::class.simpleName}> init() ${localThis.toString()}")
}

data class Position(var name: String, var position: String)

data class DNDStats(var valSTR: Int,
                    var valDEX: Int,
                    var valCHR: Int,
                    var valHP: Int )

abstract class AbstractPersonISex(override var sex: String = "Male") : ISex {

    init {
        println("\n<other.AbstractPersonISex> init() ${this::class.simpleName} ${this.toString()}")
    }

    override fun toString(): String {
        return ", sex=$sex, " + super.toString()
    }
}

// Abstract classes for other.Person
abstract class AbstractPersonIName(name: String
) : AbstractPersonISex() {
    override var namePerson: String = name // constructed here
        internal set

    private var loadingStatus: String = "Loading..." // Showing abstract class internal state


    init {
        println("<other.AbstractPersonIName> init() ${this.toString()}}")
    }

    private fun getLoadingStatus(): String {
        return "[$loadingStatus]"
    }
    fun setLoadingStatus(status: String) {
        loadingStatus = status
    }

    override fun toString(): String {
        return "name=$namePerson, ${getLoadingStatus()}" + super.toString()
    }
}

abstract class AbstractPersonIJob(name: String,
                                  override var ssn: String, // Constructed here
                                  override var job: String  // Constructed here
) : AbstractPersonIName(name), IJob {
//    final override var ssn: String  // constructed here
//    final override var job: String  // constructed here
    //    constructor(name: String, ssn: String, job: String) : this(name, ssn, job) {
//        this.name ="$name ***"
//    }
    init {
        println("<AbstractPersonWithJob> init() ${this.toString()}")
//        this.namePerson = "$name GOTTA JOB"
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


// Concrete Base Class for other.Person
class Person(name: String) : AbstractPersonIName(name)

open class PersonOld(name:String) {
    private var _name: String = name // constructed here
    val name: String // constructed here
        get() = _name

    internal fun setName(name: String) {
        _name = name
    }

    init {
        println("\n<other.Person> init() name=$name.")
    }

    override fun toString(): String {
        return "name=$name"
    }
}


// Created using only interfaces
class PersonIJobViaInterfaces(override val namePerson: String,
                              override var ssn: String,
                              override var job: String
) : IJob {

    init {
        println("<PersonWithJobViaInterfaces> init() other.Person:$namePerson, job=$job, ssn=$ssn")
    }

    override fun toString(): String {
        return "ssn=$ssn, job=$job, " + super.toString()
    }
}

class StudentViaOnlyInterfaces(override val namePerson: String,
                               override var ssn: String,
                               override val car: String = "Unknown Car"
) : ISocSecNum, ICar {
    override var vehicleType: String = "Unknown Vehicle Type"
    override var numWheels: Int = 4
    override var displayCarCount: Int = 0

}


// Created using a mix of Abstract & interfaces
class PersonDNDIJob(name: String, // InterfaceName
                    var position: Position,  // InterfaceDNDStats
                    override var stats: DNDStats,
                    override var ssn: String,  // InterfaceDNDStats
                    override var job: String  // InterfaceJob, var stats: other.DNDStats){}
) :  AbstractPersonIName(name), IJob, IDNDStats {

    init {
        println("A New hero Enters: ${this.toString()}")
    }

    fun displayPosition() {
        println("other.Position for $namePerson: ${position.name}@${position.position}")
    }

}

class Intern(name: String, // passed to the AbstractPerson constructor first
             override var ssn: String,
             override var job: String,
             private var gradYear: Int
            ) : AbstractPersonIName(name), IJob {
    init {
        println("<other.Intern> init() name=$name, ssn=$ssn, gradYear=$gradYear")
    }

    fun displayGradYear() {
        println("<other.Intern> displayGradYear() ${this.toString()}")
    }

    override fun toString(): String {
        return "job=$job, ssn=$ssn, gradYear=${gradYear}, " + super<AbstractPersonIName>.toString()
    }
}

class Student(name: String,
              override var ssn: String, // created for InterfaceSSN
              var gradeLevel: Int = 1,
              var chore: String = "NO CHORES ASSIGNED",
              override var car: String = "Ferrari", // created for InterfaceCar
              override var vehicleType: String  = "Unknown Vehicle"// created for InterfaceVehicle
) : AbstractPersonIName(name), ICar, ISocSecNum {

    override var displayCarCount: Int = 0
    override var numWheels: Int = 4

    constructor(name: String, ssn: String, gradeLevel: Int): this(name, ssn, gradeLevel,
                                                                "NO CHORES ASSIGNED",
                                                                "NO CAR ASSIGNED",
                                                                "NO VEHICLE ASSIGNED"
            ) {
        println("<other.Student> CONSTRUCTOR ${this.namePerson}")
    }

    init{
        println("<other.Student> init() student: $name in grade $gradeLevel ")
    }

    override fun displaySSN() {
        println("<other.Student> $namePerson's SSN is scrambled ${
                ssn.toString()
                .toCharArray()
                .map{c: Char -> c+20 }
                .toCharArray()
                .joinToString("") 
        }")
    }

    fun displayChore() {
        println("<other.Student> $namePerson has chore $chore is in grade $gradeLevel")
    }

//    override fun displayCar() {
////        super.displayCar(this.car) // call interface method
//        println("<other.Student> displayCar() $name's car is a $car, x=$x")
//    }

}

open class Teacher(name: String = "No name given",
                   ssn: String = "No ssn given",
                   job: String = "Substitute other.Teacher",
                   val payRate: Int = 100
             ) : AbstractPersonIJob(name, ssn, job) {

    init{
        println("<other.Teacher> init() ${this.toString()}")
    }

//    override fun displayJob() {
////        super.displayJob()
//        println("<other.Teacher> displayJob() $name has Job:$job ssn=$ssn, pay=$$payRate")
//    }

    override fun quitJob() {
        println("<other.Teacher> Called quitJob()")
        super.quitJob()
    }

    fun teachStudent(student: Student) {
        student.gradeLevel++
        println("other.Student ${student.namePerson} is now grade ${student.gradeLevel}")
        student.chore = "Head of ${student.chore}"
    }

    override fun toString(): String {
        return "payRate=$payRate, " + super.toString()
    }

}

class Parent(name: String,
             job: String
) : AbstractPersonIJob(name, job), IJobAndCar {
    override val car: String = "Minivan" // for IDisplay Car
    override var displayCarCount: Int = 0 // for IDisplay Car
    override val vehicleType: String = "UNknown Vehicle Type"
    override var numWheels: Int = 4

    init{
        println("<other.Parent> init() other.Parent: $name and job is $job")
    }

    override fun displayJobAndCarInit(): Boolean {
        println("<other.Parent> displayJobAndCarInit() called")
        return true
    }

//    override fun displayJob() {
//        //println("AbstractWithJob Route")
//        //super<AbstractPersonWithJob>.displayJob()
//
//        //println("IDisplayJobAndCar Route")
//        super<IDisplayJobAndCar>.displayJob()
//
//        //println("<other.Parent> $name has Job: $job, ssn=$ssn")
//    }

}

class PersonIJobViaAbstractClass(name: String,
                                 ssn: String,
                                 job: String
                                   ) : AbstractPersonIJob(name, ssn, job) {
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

class PersonICar(name: String,
                 override val car: String = "Unknown Car",
                 override val vehicleType: String = "Unknown Car"
) : AbstractPersonIName(name), ICar {

    override var numWheels: Int = 4
    override var displayCarCount: Int = 0

    init {
        println("<PersonWithCar> init() PersonWithCar: $name")
    }

    override fun toString(): String {
        return "car=$car, " + super<AbstractPersonIName>.toString()
    }
}

class PersonWithJobAndCar(name: String,
                          override var ssn: String,
                          override var job: String,
                          override val car: String = "Unknown Car",
                          override val vehicleType: String = "Unknown Car"
                         ) : AbstractPersonIName(name), IJobAndCar {
    override var numWheels: Int = 4

    override fun displayJobAndCarInit(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override var displayCarCount: Int = 0

    init {
        println("<other.PersonWithJobAndCar> init() other.PersonWithJobAndCar: $name")
    }

    override fun toString(): String {
        return "ssn=$ssn, job=$job, car=$car, " + super<AbstractPersonIName>.toString()
    }
}


fun mainTeacherStudentParent() {

    println("\n*** Teachers & Students & Parents ***\n")

    fun teacher(): Teacher {
        val teach = Teacher("teach Ms. Mills", "123-56-7890", "Math other.Teacher", 10000)
        return teach
    }
    fun student2(): Student {
        val student2 = Student("s2 Useless Johnny", "123-232-5634", 3)
        student2.car = "Old Junker Ford"
        return student2
    }
    fun parent(): Parent {
        val parent = Parent("parent Mr. BigShot", "Banker")
        parent.job = "Unemployed"
        return parent
    }
    fun student(): Student {
        val student = Student("s1 Little Jimmy", "223-32-1234", 3, "Erase chalkboards")
        student.gradeLevel = 2
        return student
    }
    fun personWithCar(): PersonICar {
        return PersonICar("personJobAndCar Mr. Nobody",
                "Black Limousine",
                vehicleType = "1988 Fleetwood")
    }
    fun studentViaOnlyInterfaces(): StudentViaOnlyInterfaces {
        val studentViaInterfaces = StudentViaOnlyInterfaces("StudentViaInterfaces Billy Jo",
                "396-53-2342",
                "Dune Buggy Kit Car")
        studentViaInterfaces.vehicleType = "Rad '60s runabout"
        return studentViaInterfaces
    }
    fun personWithJobAndCar(): PersonWithJobAndCar {
        val personWithJobAndCar = PersonWithJobAndCar("personWithJobAndCar Mr. Somebody", "123-23-3232", "Butcher", "Red Jeep", "Sport Utility")
        personWithJobAndCar.job = "Simple Job"
        return personWithJobAndCar
    }
    fun personWithJobViaAbstractClass() =
            PersonIJobViaAbstractClass("personWithJobViaAbstract Ol' Joe", "123-23-1223", "Jet mechanic")
    fun personWithJobViaInterfaces() =
            PersonIJobViaInterfaces("personWithJobViaInterfaces Good Ol' Jack", "123-54-3434", "Car Junker")
    fun intern(): Intern {
        val intern = Intern("intern Sally", "454-34-23443", "Ms. Mills Class", 2022)
        //intern.namePerson ="Little sally"
        return intern
    }
    fun personDNDWithJob(): PersonDNDIJob {
        val stats = DNDStats(10, 20, 20, 10)
        val personDND = PersonDNDIJob("Victorious Hero",
                Position("This is it", "my job"),
                stats,
                "123-23-2211",
                "Dragon Slayer")
//        personDND.name = "Slayer of Dragon"
        return personDND
    }

    val teacher = teacher()
    val student = student()
//    val student2 = student2()
//    val parent = parent()
//    val personWithCar = personWithCar()
//    val studentViaInterfaces = studentViaOnlyInterfaces()
//    val personWithJobAndCar = personWithJobAndCar()
//    val personWithJobAbs = personWithJobViaAbstractClass()
//    val personNamedWithJobInt = personWithJobViaInterfaces()
    val internPerson = intern()
//    val personDND = personDNDWithJob()

    println("\n")

//    personDND.displayDNDStats()
//    personDND.displayJob()
//    personDND.displayPosition()

//    teacher.displayJob()
//    teach.displaySSN()
//    println("teach.dingDangIt=${teach.getDing()}")

//    student.displayChore()
//    student.displaySSN()
//    student.displayChore()
//    student.displayCar()

    teacher.teachStudent(student)
    student.displayChore()

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

//    println(internPerson.toString())
//    internPerson.displayGradYear()
//    internPerson.displayJob()
//    internPerson.displaySSN()
}