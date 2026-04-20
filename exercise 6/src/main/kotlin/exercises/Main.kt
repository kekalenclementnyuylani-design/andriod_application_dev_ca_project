package exercises

// ─────────────────────────────────────────────────
// Exercise 2: Implement a Logger Using Delegation
// ─────────────────────────────────────────────────

// 1. Logger interface
interface Logger {
    fun log(message: String)
}

// 2a. ConsoleLogger — prints to the console
class ConsoleLogger : Logger {
    override fun log(message: String) {
        println(message)
    }
}

// 2b. FileLogger — simulated file output
class FileLogger : Logger {
    override fun log(message: String) {
        println("File: $message")
    }
}

// 3. Application delegates all Logger calls via 'by'
class Application(logger: Logger) : Logger by logger {
    fun doWork() {
        log("Starting work...")
        log("Work completed!")
    }
}

fun main() {
    println("--- Console Logger ---")
    Application(ConsoleLogger()).doWork()

    println("\n--- File Logger ---")
    Application(FileLogger()).doWork()
}
