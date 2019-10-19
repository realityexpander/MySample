
interface IDisplaySSN {
    abstract val ssn: String
    abstract val name: String

    fun displaySSN() {
        println("<IDisplaySSN> displaySSN() $name's ssn=$ssn")
    }
}

interface IDisplayJob : IDisplaySSN {
    abstract override val name: String
    abstract override val ssn: String
    abstract val job: String

    fun displayJob() {
        println("<IDisplayJob> displayJob() -> $name's job is $job, slave=$ssn")
    }
}

interface IDisplayCar {
    abstract var x: Int
    abstract val car: String

    fun displayCar(message: String = "Default Car Message") {
        x += 1
        println("<IDisplayCar> displayCar(message) message=$message, x=$x")
    }
    fun displayCar() {
        println("<IDisplayCar> displayCar() car = $car")
        //this.displayCar("<IDisplayCar> displayCar() x=$x")
    }
}

interface IDisplayJobAndCar : IDisplayJob, IDisplayCar{
    abstract override val job: String
    abstract override val car: String

    fun displayJobAndCarInit() : Boolean // Implementing Class must implement a fun

    fun displayJobAndCar(){ // Default Fun provided
        displayJob()
        displayCar()
        println("<IDisplayJobAndCarWithStars> displayJobAndCar() ***** jobcar=$job$car *****")
    }
}


abstract class AbstractNamedPerson(name: String) {
    private var _name: String = name
    val name: String // constructed here
        get() = _name

    protected fun setName(name: String) {
        _name = name
    }
}

abstract class AbstractPersonWithJob : AbstractNamedPerson, IDisplayJob {
    override var ssn: String // constructed here
    override var job: String

    constructor(name: String, ssn: String, job: String) : super(name) {
        this.ssn = ssn
        this.job = job
        this.setName("$name ***")
        println("<AbstractPerson> NEW person: ${this.name}, ssn=$ssn and my job is $job")
    }
    constructor(name: String,
                job: String) : this(name, "NO SSN ASSIGNED", job)
    constructor(name:String) : this(name, "NO SSN ASSIGNED", "NO JOB ASSIGNED")

//    override fun displaySSN() {
//        println("<AbstractPerson> DEFAULT message: $name's ssn is $ssn, job is $job ")
//    }
//    abstract fun displaySSN()

}

class Student(name: String,
              ssn: String,
              private val gradeLevel: Int,
              private val chore: String, // Backed by job
              override var car: String = "Ferrari"
            ): AbstractPersonWithJob(name, ssn, chore),
                IDisplayCar,
                IDisplaySSN {

    override var x: Int = 0

    constructor(name: String,
                ssn: String,
                gradeLevel: Int
               ): this(name,
                        ssn,
                        gradeLevel,
                  "NO CHORES ASSIGNED") {
        println("<Student> CONSTRUCTOR ${this.name}")
    }

    init{
        println("<Student> NEW student: $name in grade $gradeLevel ")
    }


//    override fun displaySSN() {
//        println("<Student> $name's SSN is scrambled ${ssn.toString().toCharArray().map{c: Char -> c+20 }.toCharArray().joinToString("")}")
//    }

//    override fun displayJob() {
//        print("<Student> displayJob()->")
//        super.displayJob()
//    }

    fun displayChore() {
        //super.displayJob()
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
             ): AbstractPersonWithJob(name, ssn, job), IDisplaySSN {

    init{
        println("<Teacher> NEW teacher: $name with job=$job ssn=$ssn in being paid $payRate")
    }

//    override fun displayJob() {
////        super.displayJob()
//        println("<Teacher> displayJob() $name has Job:$job ssn=$ssn, pay=$$payRate")
//    }
}

class Parent(name: String,
             job: String,
             override var x: Int = 0
) : AbstractPersonWithJob(name, job), IDisplayJobAndCar {
    override val car: String = "minivan"

    init{
        println("<Parent> NEW Parent: $name and job is $job")
    }

    override fun displayJobAndCarInit(): Boolean {
        println("<Parent> displayJobAndCarInit() called")
        return true
    }
    override fun displayJob() {
//        super.displayJob()
        println("<Parent> $name has Job: $job, ssn=$ssn")
    }

}

class PersonNamedWithJob( name: String,
                          override var ssn: String,
                          override var job: String
                        ) : AbstractPersonWithJob(name, ssn, job), IDisplayJob {
    init {
        println("<PersonNamedWithJob> NEW Person:$name, job=$job, ssn=$ssn")
    }
}

class PersonNamed(name: String,
                  val ssn: String,
                  override var x: Int = 0,
                  override val car: String = "Unknown Car"
                 ) : AbstractNamedPerson(name), IDisplayCar {

    init {
        println("<Person> NEW Person: $name")
    }

//    override fun displaySSN() {
//        println("<Person> displaySSN() $name's SSN is $ssn")
//    }
//     fun displayJob() {
//        println("<Person> $name has **UNKNOWN JOB**")
//        //super.displayJob()
//    }
}

fun doTeacherStudent() {
    val teach = Teacher("teach Ms. Mills", "123-56-7890", "Math Teacher", 10000)
    val student = Student("s1 Little Jimmy", "223-32-1234", 2, "Erase chalkboards")
    val student2 = Student("s2 Useless Johnny", "123-232-5634", 3)
    student2.car = "Old Junker Ford"
    val parent = Parent("parent Mr. BigShot", "Banker")
    val person = PersonNamed("person Mr. Nobody","007-74-5218" )
//    person.job = "Simple Job"

    val personWithJob = PersonNamedWithJob("personWithJob Good Ol' Joe", "123-23-1223", "Jet mechanic")

    println("\n")

//    teach.displayJob()
//    teach.displaySSN()

    student.displayJob()
    student.displaySSN()
//    student.displayChore()
//    student.displayCar()

//    student2.displayJob()
//    student2.displaySSN()
//    student2.displayChore()
//    student2.displayCar()

//    parent.displayJob()
//    parent.displaySSN()
//    parent.displayJobAndCar()

//    person.displayJob()
//    person.displaySSN()

    personWithJob.displayJob()
    personWithJob.displaySSN()
}