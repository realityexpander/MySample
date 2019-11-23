package logger

private fun PassByReference() {
    class Logger() {
        var hours: Int = 0
    }

    class Simulate(val loggerRef: Logger) {
        fun doSomething() {
            loggerRef.hours += 1
        }
    }

    val log1 = Logger()
    val log2 = Logger()
    val sim1 = Simulate(log1)
    val sim2 = Simulate(log2)
    sim1.doSomething()
    sim2.doSomething()
    sim2.doSomething()
    println(log1.hours) //This should be 1
    println(log2.hours) //This should be 2
}