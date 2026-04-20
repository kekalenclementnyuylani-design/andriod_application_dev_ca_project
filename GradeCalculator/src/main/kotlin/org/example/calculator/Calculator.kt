package org.example.calculator

/**
 * Abstract base class — all calculator types derive from this.
 *
 * Demonstrates:
 *  - Abstract class
 *  - Polymorphism via abstract methods that subclasses override
 *  - Lambda property (roundTo)
 *  - Lazy initialization (operationLabel)
 */
abstract class Calculator {

    // ── Lazy initialization — only created when first accessed ──────────────
    val operationLabel: String by lazy {
        "[$calculatorType] Ready"
    }

    /** Subclasses must declare their type name */
    abstract val calculatorType: String

    /**
     * Core calculation method — POLYMORPHISM.
     * Each subclass provides its own implementation.
     */
    abstract fun calculate(values: List<Double>): Double

    /**
     * Optional hook — subclasses can override for custom validation.
     * Default: reject empty lists.
     */
    open fun validate(values: List<Double>): Boolean = values.isNotEmpty()

    // ── Lambda utility — reusable rounding function ─────────────────────────
    val roundTo: (Double, Int) -> Double = { value, places ->
        val factor = Math.pow(10.0, places.toDouble())
        Math.round(value * factor) / factor
    }
}
