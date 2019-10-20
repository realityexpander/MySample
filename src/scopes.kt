open class BaseClass {
    private val valPrivate = 1
    protected open val valProtectedOpen = 2
    protected val valProtected = 3
    internal val valInternal = 4
    public val valPublic = 5  // public by default
    protected var valOuterNested : Nested
        private set

    protected class Nested {
        public val nestedPublic: Int = 6
    }

    init {
        valOuterNested = Nested()
    }

    open fun display() {
        println("<Outer>\nvalPrivate=$valPrivate, \n" +
                "valProtectedOpen=$valProtectedOpen, \n" +
                "valProtected=$valProtected, \n" +
                "valInternal=$valInternal, \n" +
                "valPublic=$valPublic, \n" +
                "valOuterNested.nestedPublic=${valOuterNested.nestedPublic}")
    }
}

class Subclass : BaseClass() {
    // valPrivate not visible
    // valProtectedOpen is visible, overrideable
    // valProtected is visible, not overrideable due to default final
    // valInternal is visible (same module)
    // valPublic is visible
    // valOuterNested.nestedPublic is visible

    override val valProtectedOpen = 200 // ok to override a protected var if its declared open
//  override val valProtected = 10 // Can't do this unless its declared open

    override fun display() {
        println(("<Subclass>\nvalProtectedOpen=$valProtectedOpen, \n" +
                "valProtected=$valProtected, \n" +
                "valInternal=$valInternal, \n" +
                "valPublic=$valPublic, \n" +
                "valOuterNested.nestedPublic=${valOuterNested.nestedPublic}"))
    }
}

class UnrelatedClass(o: BaseClass) {
    // o.valPrivate not visible
    // o.valProtected not visible
    // o.valInternal is visible (same module)
    // o.valPublic is visible
    // o.valOuterNested not visible
    // Outer.Nested not visible
    // Nested::nestedPublic not visible

    init {
        println("<UnrelatedClass>\no.valInternal=${o.valInternal}, \n" +
                            "o.valPublic=${o.valPublic}" )
    }

}

fun main3(args: Array<String>) {

    val outer = BaseClass()
    outer.display()
//  outer.valOuterNested // Protected and access not allowed
    println()

    val subclass = Subclass()
    subclass.display()
    println()

    val unrelated = UnrelatedClass(outer)

}