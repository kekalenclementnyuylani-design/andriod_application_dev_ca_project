package org.example.calculator

import org.example.model.Student

/**
 * GradeCalculator INHERITS from Calculator.
 *
 * Demonstrates:
 *  - Inheritance (extends Calculator)
 *  - Polymorphism (overrides calculate() and validate())
 *  - Lazy initialization (gradeScale, passMark)
 *  - Lambdas (assignGrade, formatAverage, buildSummary)
 */
class GradeCalculator : Calculator() {

    // ── Polymorphism — declaring type ────────────────────────────────────────
    override val calculatorType: String = "GradeCalculator"

    // ── Lazy initialization — grade map built only when first needed ─────────
    private val gradeScale: Map<ClosedRange<Double>, String> by lazy {
        mapOf(
            80.0..100.0 to "A",
            70.0..79.9  to "B+",
            60.0..69.9  to "B",
            55.0..59.9  to "C+",
            50.0..54.9  to "C",
            45.0..49.9  to "D+",
            40.0..44.9  to "D",
            0.0..39.9   to "F"
        )
    }

    private val passMark: Double by lazy { 40.0 }

    // ── POLYMORPHISM — overrides abstract method in Calculator ───────────────
    override fun calculate(values: List<Double>): Double {
        if (!validate(values)) return 0.0
        val avg = values.average()
        return roundTo(avg, 1)   // uses lambda from parent class
    }

    // ── POLYMORPHISM — overrides open method in Calculator ───────────────────
    override fun validate(values: List<Double>): Boolean {
        return values.isNotEmpty() && values.all { it in 0.0..100.0 }
    }

    // ── Lambda — maps average score to letter grade ──────────────────────────
    val assignGrade: (Double) -> String = { average ->
        gradeScale.entries
            .firstOrNull { average in it.key }
            ?.value ?: "F"
    }

    // ── Lambda — checks pass/fail ────────────────────────────────────────────
    val isPassing: (Double) -> Boolean = { average ->
        average >= passMark
    }

    // ── Lambda — formats average for display ─────────────────────────────────
    val formatAverage: (Double) -> String = { avg ->
        "%.1f".format(avg)
    }

    // ── Lambda — builds a one-line summary for a student ─────────────────────
    val buildSummary: (Student) -> String = { student ->
        val avg = student.average ?: 0.0
        val grade = student.grade ?: "N/A"
        val result = if (student.passed == true) "PASS" else "FAIL"
        "${student.name.padEnd(24)} Avg: ${formatAverage(avg).padEnd(7)} Grade: ${grade.padEnd(4)} [$result]"
    }

    /**
     * Processes a single student — calculates average, assigns grade, sets pass/fail.
     */
    fun processStudent(student: Student): Student {
        val avg = calculate(student.scores)
        val grade = assignGrade(avg)
        return student.copy(
            average = avg,
            grade = grade,
            passed = isPassing(avg)
        )
    }

    /**
     * Processes all students in a list.
     * Uses lambda + functional style (map).
     */
    fun calculateAll(students: List<Student>): List<Student> =
        students.map { processStudent(it) }

    /**
     * Returns grade scale as a list of pairs for display in UI.
     */
    fun getGradeScaleDisplay(): List<Pair<String, String>> = listOf(
        "A"  to "80 – 100",
        "B+" to "70 – 79",
        "B"  to "60 – 69",
        "C+" to "55 – 59",
        "C"  to "50 – 54",
        "D+" to "45 – 49",
        "D"  to "40 – 44",
        "F"  to "0 – 39"
    )
}
