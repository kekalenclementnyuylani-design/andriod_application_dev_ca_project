package org.example.model

/**
 * Represents a single student record loaded from the Excel file.
 *
 * @param name     Full name of the student
 * @param scores   Raw exam/assignment scores
 * @param average  Computed average (null until calculated)
 * @param grade    Letter grade (null until calculated)
 * @param passed   Pass/fail result (null until calculated)
 */
data class Student(
    val name: String,
    val scores: List<Double>,
    var average: Double? = null,
    var grade: String? = null,
    var passed: Boolean? = null
)
