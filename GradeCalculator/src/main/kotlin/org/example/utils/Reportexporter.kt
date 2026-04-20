package org.example.utils

import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.element.Text
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.xwpf.usermodel.*
import org.example.model.Student
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Handles exporting grade results to multiple formats:
 * Excel (.xlsx), Word (.docx), PDF (.pdf), HTML (.html)
 *
 * Uses lambdas for row building and formatting helpers.
 */
object ReportExporter {

    // ── Lambda — formats a grade result row as a list of strings ─────────────
    private val formatRow: (Int, Student) -> List<String> = { index, student ->
        listOf(
            "$index",
            student.name,
            student.scores.joinToString(", ") { "%.0f".format(it) },
            "%.1f".format(student.average ?: 0.0),
            student.grade ?: "N/A",
            if (student.passed == true) "PASS" else "FAIL"
        )
    }

    // ── Lambda — generates a timestamp string ─────────────────────────────────
    private val timestamp: () -> String = {
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"))
    }

    private val headers = listOf("#", "Student Name", "Scores", "Average", "Grade", "Result")

    // =========================================================================
    // EXCEL EXPORT
    // =========================================================================
    fun exportExcel(students: List<Student>, outputFile: File): Boolean {
        return try {
            val workbook = XSSFWorkbook()
            val sheet = workbook.createSheet("Grade Report")

            // Styles
            val headerFont = workbook.createFont().apply {
                bold = true
                fontHeightInPoints = 11
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

            // Header row
            val headerRow = sheet.createRow(0)
            headers.forEachIndexed { col, title ->
                headerRow.createCell(col).apply {
                    setCellValue(title)
                    cellStyle = headerStyle
                }
            }

            // Data rows using lambda
            students.forEachIndexed { idx, student ->
                val row = sheet.createRow(idx + 1)
                val cols = formatRow(idx + 1, student)
                cols.forEachIndexed { col, value ->
                    row.createCell(col).apply {
                        setCellValue(value)
                        cellStyle = when {
                            col == 5 && value == "PASS" -> passStyle
                            col == 5 && value == "FAIL" -> failStyle
                            col != 1 -> centerStyle
                            else -> workbook.createCellStyle()
                        }
                    }
                }
            }

            // Summary row
            val passed = students.count { it.passed == true }
            val summaryRow = sheet.createRow(students.size + 2)
            summaryRow.createCell(0).setCellValue(
                "Generated: ${timestamp()}   |   Total: ${students.size}   Passed: $passed   Failed: ${students.size - passed}"
            )

            // Auto size columns
            headers.indices.forEach { sheet.autoSizeColumn(it) }

            FileOutputStream(outputFile).use { workbook.write(it) }
            workbook.close()
            true
        } catch (e: Exception) {
            println("Excel export error: ${e.message}")
            false
        }
    }

    // =========================================================================
    // WORD EXPORT (.docx)
    // =========================================================================
    fun exportWord(students: List<Student>, outputFile: File): Boolean {
        return try {
            val doc = XWPFDocument()

            // ── Title ─────────────────────────────────────────────────────────
            doc.createParagraph().apply {
                alignment = ParagraphAlignment.CENTER
                createRun().apply {
                    setText("GRADE CALCULATOR PRO")
                    isBold = true
                    fontSize = 18
                    color = "0A2A6E"
                }
            }
            doc.createParagraph().apply {
                alignment = ParagraphAlignment.CENTER
                createRun().apply {
                    setText("ICT University — Academic Grade Report")
                    fontSize = 12
                    color = "555555"
                }
            }
            doc.createParagraph().apply {
                alignment = ParagraphAlignment.CENTER
                createRun().apply {
                    setText("Generated: ${timestamp()}")
                    fontSize = 10
                    color = "888888"
                }
            }
            doc.createParagraph() // spacer

            // ── Table ─────────────────────────────────────────────────────────
            val table = doc.createTable(students.size + 1, headers.size)
            table.setInsideHBorder(XWPFTable.XWPFBorderType.SINGLE, 1, 0, "CCCCCC")
            table.setInsideVBorder(XWPFTable.XWPFBorderType.SINGLE, 1, 0, "CCCCCC")

            // Header row
            val headerRow = table.getRow(0)
            headers.forEachIndexed { col, title ->
                val cell = headerRow.getCell(col) ?: headerRow.addNewTableCell()
                cell.setColor("0A2A6E")
                cell.paragraphs[0].apply {
                    alignment = ParagraphAlignment.CENTER
                    createRun().apply {
                        setText(title)
                        isBold = true
                        color = "FFFFFF"
                        fontSize = 10
                    }
                }
            }

            // Data rows using lambda
            students.forEachIndexed { idx, student ->
                val row = table.getRow(idx + 1) ?: table.createRow()
                val cols = formatRow(idx + 1, student)
                val passed = student.passed == true

                cols.forEachIndexed { col, value ->
                    val cell = row.getCell(col) ?: row.addNewTableCell()
                    if (col == 5) cell.setColor(if (passed) "C8F7C5" else "FADADD")
                    if (idx % 2 == 0 && col != 5) cell.setColor("F0F4FF")
                    cell.paragraphs[0].apply {
                        alignment = if (col == 1) ParagraphAlignment.LEFT else ParagraphAlignment.CENTER
                        createRun().apply {
                            setText(value)
                            fontSize = 10
                            if (col == 5) isBold = true
                        }
                    }
                }
            }

            // ── Summary ───────────────────────────────────────────────────────
            doc.createParagraph() // spacer
            val passed = students.count { it.passed == true }
            doc.createParagraph().apply {
                createRun().apply {
                    setText("Summary: Total ${students.size} students   |   Passed: $passed   |   Failed: ${students.size - passed}")
                    isBold = true
                    fontSize = 11
                    color = "0A2A6E"
                }
            }

            // ── Grade Scale ───────────────────────────────────────────────────
            doc.createParagraph()
            doc.createParagraph().apply {
                createRun().apply {
                    setText("Grade Scale Reference")
                    isBold = true
                    fontSize = 12
                    color = "0A2A6E"
                }
            }
            val scaleText = "A: 80–100  |  B+: 70–79  |  B: 60–69  |  C+: 55–59  |  C: 50–54  |  D+: 45–49  |  D: 40–44  |  F: 0–39"
            doc.createParagraph().apply {
                createRun().apply {
                    setText(scaleText)
                    fontSize = 10
                    color = "555555"
                }
            }

            FileOutputStream(outputFile).use { doc.write(it) }
            doc.close()
            true
        } catch (e: Exception) {
            println("Word export error: ${e.message}")
            false
        }
    }

    // =========================================================================
    // PDF EXPORT
    // =========================================================================
    fun exportPdf(students: List<Student>, outputFile: File): Boolean {
        return try {
            val pdfWriter = PdfWriter(outputFile)
            val pdfDoc = PdfDocument(pdfWriter)
            val document = Document(pdfDoc)

            val navyBlue = DeviceRgb(10, 42, 110)
            val lightBlue = DeviceRgb(240, 244, 255)
            val passGreen = DeviceRgb(200, 247, 197)
            val failRed = DeviceRgb(250, 218, 221)
            val white = ColorConstants.WHITE

            // ── Title ─────────────────────────────────────────────────────────
            document.add(
                Paragraph("GRADE CALCULATOR PRO")
                    .setFontSize(20f)
                    .setBold()
                    .setFontColor(navyBlue)
                    .setTextAlignment(TextAlignment.CENTER)
            )
            document.add(
                Paragraph("ICT University — Academic Grade Report")
                    .setFontSize(12f)
                    .setFontColor(DeviceRgb(80, 80, 80))
                    .setTextAlignment(TextAlignment.CENTER)
            )
            document.add(
                Paragraph("Generated: ${timestamp()}")
                    .setFontSize(9f)
                    .setFontColor(DeviceRgb(130, 130, 130))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20f)
            )

            // ── Table ─────────────────────────────────────────────────────────
            val colWidths = floatArrayOf(30f, 160f, 120f, 60f, 50f, 60f)
            val table = Table(UnitValue.createPointArray(colWidths)).useAllAvailableWidth()

            // Header cells using lambda
            headers.forEach { title ->
                table.addHeaderCell(
                    Cell().add(Paragraph(title).setBold().setFontColor(white).setFontSize(10f))
                        .setBackgroundColor(navyBlue)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setPadding(6f)
                )
            }

            // Data rows using lambda
            students.forEachIndexed { idx, student ->
                val cols = formatRow(idx + 1, student)
                val passed = student.passed == true
                val rowBg = if (idx % 2 == 0) lightBlue else white

                cols.forEachIndexed { col, value ->
                    val cellBg = if (col == 5) (if (passed) passGreen else failRed) else rowBg
                    val cell = Cell().add(
                        Paragraph(value)
                            .setFontSize(10f)
                            .also { if (col == 5) it.setBold() }
                    )
                        .setBackgroundColor(cellBg)
                        .setPadding(5f)
                        .setTextAlignment(if (col == 1) TextAlignment.LEFT else TextAlignment.CENTER)
                    table.addCell(cell)
                }
            }

            document.add(table)

            // ── Summary ───────────────────────────────────────────────────────
            val passed = students.count { it.passed == true }
            document.add(
                Paragraph("\nSummary: Total ${students.size} students   |   Passed: $passed   |   Failed: ${students.size - passed}")
                    .setFontSize(11f)
                    .setBold()
                    .setFontColor(navyBlue)
                    .setMarginTop(16f)
            )

            // ── Grade Scale ───────────────────────────────────────────────────
            document.add(
                Paragraph("\nGrade Scale: A: 80–100  |  B+: 70–79  |  B: 60–69  |  C+: 55–59  |  C: 50–54  |  D+: 45–49  |  D: 40–44  |  F: 0–39")
                    .setFontSize(9f)
                    .setFontColor(DeviceRgb(100, 100, 100))
            )

            document.close()
            true
        } catch (e: Exception) {
            println("PDF export error: ${e.message}")
            false
        }
    }

    // =========================================================================
    // HTML EXPORT
    // =========================================================================
    fun exportHtml(students: List<Student>, outputFile: File): Boolean {
        return try {
            val passed = students.count { it.passed == true }
            val failed = students.size - passed

            // Lambda — builds a table row
            val buildRow: (Int, Student) -> String = { idx, student ->
                val cols = formatRow(idx, student)
                val isPassed = student.passed == true
                val resultClass = if (isPassed) "pass" else "fail"
                val rowClass = if (idx % 2 == 0) "even" else "odd"
                buildString {
                    append("<tr class=\"$rowClass\">")
                    cols.forEachIndexed { col, value ->
                        val cls = if (col == 5) " class=\"$resultClass\"" else ""
                        append("<td$cls>$value</td>")
                    }
                    append("</tr>")
                }
            }

            val gradeRows = students.mapIndexed { i, s -> buildRow(i + 1, s) }.joinToString("\n")

            val html = """
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Grade Report — ICT University</title>
  <style>
    * { margin: 0; padding: 0; box-sizing: border-box; }
    body {
      font-family: 'Segoe UI', Tahoma, Geneva, sans-serif;
      background: #050D1A;
      color: #ECF0FF;
      padding: 40px;
      min-height: 100vh;
    }
    .container { max-width: 960px; margin: 0 auto; }
    .header {
      text-align: center;
      margin-bottom: 36px;
      padding: 32px;
      background: linear-gradient(135deg, #0A1628, #0F2040);
      border-radius: 16px;
      border: 1px solid #1A3A6B;
    }
    .header h1 { font-size: 28px; color: #00D4FF; letter-spacing: 2px; margin-bottom: 6px; }
    .header p  { font-size: 14px; color: #7B9AC4; }
    .header .timestamp { font-size: 12px; color: #3D5A80; margin-top: 8px; }

    .stats {
      display: flex; gap: 16px; margin-bottom: 24px;
    }
    .stat-card {
      flex: 1; padding: 16px 20px;
      background: #0A1628;
      border-radius: 12px;
      border: 1px solid #1A3A6B;
      text-align: center;
    }
    .stat-card .value { font-size: 32px; font-weight: 800; }
    .stat-card .label { font-size: 12px; color: #7B9AC4; margin-top: 4px; letter-spacing: 1px; }
    .total   .value { color: #00D4FF; }
    .passing .value { color: #00E676; }
    .failing .value { color: #FF1744; }

    table {
      width: 100%; border-collapse: collapse;
      background: #0A1628;
      border-radius: 12px; overflow: hidden;
      border: 1px solid #1A3A6B;
    }
    thead tr { background: #0A2A6E; }
    thead th {
      padding: 14px 16px; font-size: 11px;
      letter-spacing: 1.5px; color: #ECF0FF;
      text-align: center; font-weight: 700;
    }
    thead th:nth-child(2) { text-align: left; }
    tbody tr.even { background: #0A1628; }
    tbody tr.odd  { background: #0F2040; }
    tbody tr:hover { background: #1A3A6B; transition: 0.2s; }
    td {
      padding: 12px 16px; font-size: 13px;
      color: #ECF0FF; text-align: center;
      border-bottom: 1px solid #1A3A6B;
    }
    td:nth-child(2) { text-align: left; font-weight: 500; }
    td.pass {
      color: #00E676; font-weight: 700;
      background: rgba(0,230,118,0.1);
    }
    td.fail {
      color: #FF1744; font-weight: 700;
      background: rgba(255,23,68,0.1);
    }

    .scale {
      margin-top: 24px; padding: 20px 24px;
      background: #0A1628;
      border-radius: 12px;
      border: 1px solid #1A3A6B;
    }
    .scale h3 { color: #00D4FF; font-size: 13px; letter-spacing: 1px; margin-bottom: 14px; }
    .scale-items { display: flex; flex-wrap: wrap; gap: 10px; }
    .scale-item {
      padding: 6px 14px;
      border-radius: 6px;
      font-size: 12px; font-weight: 700;
      background: rgba(0,212,255,0.1);
      border: 1px solid rgba(0,212,255,0.3);
      color: #00D4FF;
    }
    .footer {
      text-align: center; margin-top: 32px;
      font-size: 11px; color: #3D5A80;
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="header">
      <h1>🎓 GRADE CALCULATOR PRO</h1>
      <p>ICT University — Academic Grade Report</p>
      <p class="timestamp">Generated: ${timestamp()}</p>
    </div>

    <div class="stats">
      <div class="stat-card total">
        <div class="value">${students.size}</div>
        <div class="label">TOTAL STUDENTS</div>
      </div>
      <div class="stat-card passing">
        <div class="value">$passed</div>
        <div class="label">PASSED</div>
      </div>
      <div class="stat-card failing">
        <div class="value">$failed</div>
        <div class="label">FAILED</div>
      </div>
    </div>

    <table>
      <thead>
        <tr>
          <th>#</th><th style="text-align:left">STUDENT NAME</th>
          <th>SCORES</th><th>AVERAGE</th><th>GRADE</th><th>RESULT</th>
        </tr>
      </thead>
      <tbody>
        $gradeRows
      </tbody>
    </table>

    <div class="scale">
      <h3>GRADE SCALE REFERENCE</h3>
      <div class="scale-items">
        <div class="scale-item">A — 80–100</div>
        <div class="scale-item">B+ — 70–79</div>
        <div class="scale-item">B — 60–69</div>
        <div class="scale-item">C+ — 55–59</div>
        <div class="scale-item">C — 50–54</div>
        <div class="scale-item">D+ — 45–49</div>
        <div class="scale-item">D — 40–44</div>
        <div class="scale-item">F — 0–39</div>
      </div>
    </div>

    <div class="footer">Grade Calculator Pro — ICT University © 2025</div>
  </div>
</body>
</html>
            """.trimIndent()

            outputFile.writeText(html)
            true
        } catch (e: Exception) {
            println("HTML export error: ${e.message}")
            false
        }
    }
}