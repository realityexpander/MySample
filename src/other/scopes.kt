package other

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
    // valPrivate                  NOT visible
    // valProtectedOpen            is visible, overrideable (not public)
    // valProtected                is visible, not overrideable due to default final (not public)
    // valInternal                 is visible (same module)
    // valPublic                   is visible
    // valOuterNested.nestedPublic is visible

    override val valProtectedOpen = 200 // OK to override a protected var, only if its declared open
//  override val valProtected = 10      // Can't override this unless its declared open

    override fun display() {
        println(("<other.Subclass>\nvalProtectedOpen=$valProtectedOpen, \n" +
                "valProtected=$valProtected, \n" +
                "valInternal=$valInternal, \n" +
                "valPublic=$valPublic, \n" +
                "valOuterNested.nestedPublic=${valOuterNested.nestedPublic}"))
    }
}

class UnrelatedClass(o: BaseClass) {
    // o.valPrivate         NOT visible
    // o.valProtected       NOT visible
    // o.valProtectedOpen   NOT visible
    // o.valInternal        is visible (same module only, outside module invisible)
    // o.valPublic          is visible
    // o.valOuterNested     NOT visible
    // Outer.Nested         NOT visible
    // Nested::nestedPublic NOT visible

    init {
        println("<other.UnrelatedClass>\no.valInternal=${o.valInternal}, \n" +
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