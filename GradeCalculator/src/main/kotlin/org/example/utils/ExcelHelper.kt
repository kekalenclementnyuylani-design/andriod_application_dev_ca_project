package org.example.utils

import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.*
import org.example.model.Student
import java.io.File
import java.io.FileOutputStream

/**
 * Handles all Excel reading and writing via Apache POI.
 *
 * Expected input format:
 *   Column A  — Student name
 *   Column B+ — Numeric scores (as many columns as needed)
 */
object ExcelHelper {

    // ── Lambda — safely reads a numeric cell value ───────────────────────────
    private val readNumericCell: (Cell?) -> Double? = { cell ->
        when (cell?.cellType) {
            CellType.NUMERIC -> cell.numericCellValue
            CellType.STRING  -> cell.stringCellValue.toDoubleOrNull()
            else             -> null
        }
    }

    // ── Lambda — safely reads a string cell value ────────────────────────────
    private val readStringCell: (Cell?) -> String = { cell ->
        when (cell?.cellType) {
            CellType.STRING  -> cell.stringCellValue.trim()
            CellType.NUMERIC -> cell.numericCellValue.toInt().toString()
            else             -> ""
        }
    }

    /**
     * Reads student records from the first sheet of an Excel file.
     * Row 0 = header (skipped). Column 0 = name. Columns 1+ = scores.
     */
    fun readStudents(file: File): List<Student> {
        val students = mutableListOf<Student>()

        try {
            val workbook: Workbook = WorkbookFactory.create(file)
            val sheet = workbook.getSheetAt(0)

            for (rowIndex in 1..sheet.lastRowNum) {
                val row = sheet.getRow(rowIndex) ?: continue
                val name = readStringCell(row.getCell(0))
                if (name.isBlank()) continue

                val scores = mutableListOf<Double>()
                for (colIndex in 1 until row.lastCellNum) {
                    val score = readNumericCell(row.getCell(colIndex))
                    if (score != null) scores.add(score)
                }

                if (scores.isNotEmpty()) {
                    students.add(Student(name = name, scores = scores))
                }
            }

            workbook.close()
        } catch (e: Exception) {
            println("Excel read error: ${e.message}")
        }

        return students
    }

    /**
     * Writes processed student results into a new "Results" sheet in a new file.
     * Copies all original data and appends calculated columns.
     *
     * @return true if write succeeded, false otherwise
     */
    fun writeResults(
        sourceFile: File,
        outputFile: File,
        students: List<Student>
    ): Boolean {
        return try {
            val workbook = XSSFWorkbook()

            // ── Style helpers ────────────────────────────────────────────────
            val headerFont = workbook.createFont().apply {
                bold = true
                fontHeightInPoints = 12
                color = IndexedColors.WHITE.index
            }
            val headerStyle = workbook.createCellStyle().apply {
                setFont(headerFont)
                fillForegroundColor = IndexedColors.DARK_BLUE.index
                fillPattern = FillPatternType.SOLID_FOREGROUND
                alignment = HorizontalAlignment.CENTER
                borderBottom = BorderStyle.MEDIUM
            }

            val passStyle = workbook.createCellStyle().apply {
                fillForegroundColor = IndexedColors.LIGHT_GREEN.index
                fillPattern = FillPatternType.SOLID_FOREGROUND
                alignment = HorizontalAlignment.CENTER
            }
            val failStyle = workbook.createCellStyle().apply {
                fillForegroundColor = IndexedColors.ROSE.index
                fillPattern = FillPatternType.SOLID_FOREGROUND
                alignment = HorizontalAlignment.CENTER
            }
            val centerStyle = workbook.createCellStyle().apply {
                alignment = HorizontalAlignment.CENTER
            }

            // ── Build Results sheet ──────────────────────────────────────────
            val sheet = workbook.createSheet("Results")

            // Find max number of score columns
            val maxScores = students.maxOfOrNull { it.scores.size } ?: 0

            // Header row
            val headers = mutableListOf("Student Name")
            repeat(maxScores) { i -> headers.add("Score ${i + 1}") }
            headers.addAll(listOf("Average", "Grade", "Result"))

            val headerRow = sheet.createRow(0)
            headers.forEachIndexed { col, title ->
                headerRow.createCell(col).apply {
                    setCellValue(title)
                    cellStyle = headerStyle
                }
            }

            // Data rows
            students.forEachIndexed { rowIdx, student ->
                val row = sheet.createRow(rowIdx + 1)

                // Name
                row.createCell(0).setCellValue(student.name)

                // Scores
                student.scores.forEachIndexed { col, score ->
                    row.createCell(col + 1).apply {
                        setCellValue(score)
                        cellStyle = centerStyle
                    }
                }

                // Average
                val avgCol = maxScores + 1
                row.createCell(avgCol).apply {
                    setCellValue(student.average ?: 0.0)
                    cellStyle = centerStyle
                }

                // Grade
                row.createCell(avgCol + 1).apply {
                    setCellValue(student.grade ?: "N/A")
                    cellStyle = centerStyle
                }

                // Result (PASS/FAIL with colour)
                val passed = student.passed == true
                row.createCell(avgCol + 2).apply {
                    setCellValue(if (passed) "PASS" else "FAIL")
                    cellStyle = if (passed) passStyle else failStyle
                }
            }

            // Summary row
            val summaryRow = sheet.createRow(students.size + 2)
            val passed = students.count { it.passed == true }
            summaryRow.createCell(0).setCellValue(
                "Total: ${students.size}   |   Passed: $passed   |   Failed: ${students.size - passed}"
            )

            // Auto-size all columns
            for (col in 0 until headers.size) {
                sheet.autoSizeColumn(col)
            }

            // Write file
            FileOutputStream(outputFile).use { workbook.write(it) }
            workbook.close()
            true

        } catch (e: Exception) {
            println("Excel write error: ${e.message}")
            false
        }
    }
}
