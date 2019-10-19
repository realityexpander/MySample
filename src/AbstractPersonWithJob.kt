interface InterfaceName {
    abstract val name: String
    fun displayName() {
        println("<IDisplayName> displayName() $name")
    }
}
interface InterfaceSSN : InterfaceName {
    abstract override val name: String
    abstract val ssn: String

    fun displaySSN() {
        println("<IDisplaySSN> displaySSN() $name's ssn=$ssn")
    }
}
interface InterfaceJob : InterfaceSSN {
    abstract override val name: String
    abstract override val ssn: String
    abstract var job: String

    fun displayJob() {
        println("<IDisplayJob> displayJob() $name's job is $job, slave=$ssn")
    }

}
interface InterfaceCar {
    abstract var displayCarCount: Int // this var must be in visible the class block
    abstract val car: String        // this var must be in visible the class block

    fun displayCar(message: String = "Default Car Message") {
        displayCarCount += 1
        println("<IDisplayCar> displayCar(message) car=$car, message=\"$message\", displayCarCount=$displayCarCount")
    }
    fun displayCar() {
        this.displayCar("starting displayCarCount=$displayCarCount")
    }
}
interface InterfaceJobAndCar : InterfaceJob, InterfaceCar{
    abstract override var job: String
    abstract override val car: String

    fun displayJobAndCarInit() : Boolean // Implementing Class must implement this fun signature

    fun displayJobAndCar(){ // Default Fun provided
        displayJob()
        displayCar()
        //println("<IDisplayJobAndCar> displayJobAndCar() ***** jobcar=$job/$car *****")
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


class Intern(name: String,
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
            ): AbstractPerson(name),
                    InterfaceCar,
                    InterfaceSSN {

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
            ) : AbstractPersonWithJob(name, job),
                    InterfaceJobAndCar {
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
                                ) : AbstractPerson(name),
                                        InterfaceJob {

    init {
        println("<PersonWithJobViaInterfaces> NEW Person:$name, job=$job, ssn=$ssn")
    }

    override fun toString(): String {
        return "ssn=$ssn, job=$job, " + super.toString()
    }
}

class PersonWithCar(name: String,
                    override val car: String = "Unknown Car"
) : AbstractPerson(name),
                           InterfaceCar {

    override var displayCarCount: Int = 0

    init {
        println("<PersonWithCar> NEW PersonWithCar: $name")
    }

    override fun toString(): String {
        return "car=$car, " + super<AbstractPerson>.toString()
    }
}

class PersonWithCarAndJob(name: String,
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
        println("<Person> NEW Person: $name")
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
//    personNamedWithCar.job = "Simple Job"
    val personWithJobAndCar = PersonWithCarAndJob("personWithJobAndCar Mr. Somebody", "123-23-3232", "Butcher", "Red Jeep")

    val personWithJobAbs = PersonWithJobViaAbstractClass("personWithJobViaAbstracts Good Ol' Joe", "123-23-1223", "Jet mechanic")
    val personNamedWithJobIntf = PersonWithJobViaInterfaces("personWithJobViaInterfaces Good Ol' Jack", "123-54-3434", "Car Junker")
    val intern = Intern("intern Sally", "454-34-23443", "Ms. Mills Class", 2022)
    intern.setName("Little sally")
    println("\n")

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

    personWithJobAndCar.displayJobAndCar()

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