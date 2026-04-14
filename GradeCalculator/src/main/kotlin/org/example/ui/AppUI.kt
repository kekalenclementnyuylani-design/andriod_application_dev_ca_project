package org.example.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.example.calculator.GradeCalculator
import org.example.model.Student
import org.example.utils.ExcelHelper
import org.example.utils.ReportExporter
import java.io.File
import javax.swing.JFileChooser
import javax.swing.UIManager
import javax.swing.filechooser.FileNameExtensionFilter

// ── App State ─────────────────────────────────────────────────────────────────
enum class AppStep { IDLE, LOADED, PROCESSING, DONE, ERROR }

// ── Export Format descriptor ──────────────────────────────────────────────────
data class ExportFormat(
    val label: String,
    val emoji: String,
    val extension: String,
    val color: Color
)

@Composable
fun GradeApp() {
    // Lazy-initialized calculator — only created when first used
    val calculator by remember { lazy { GradeCalculator() } }
    val scope = rememberCoroutineScope()

    // State
    var step by remember { mutableStateOf(AppStep.IDLE) }
    var sourceFile by remember { mutableStateOf<File?>(null) }
    var rawStudents by remember { mutableStateOf<List<Student>>(emptyList()) }
    var processedStudents by remember { mutableStateOf<List<Student>>(emptyList()) }
    var statusMessage by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    // ── Export formats list ───────────────────────────────────────────────────
    val exportFormats = listOf(
        ExportFormat("Excel", "📊", "xlsx", AppColors.Accent),
        ExportFormat("Word",  "📝", "docx", Color(0xFF4FC3F7)),
        ExportFormat("PDF",   "📄", "pdf",  Color(0xFFFF7043)),
        ExportFormat("HTML",  "🌐", "html", Color(0xFF66BB6A))
    )

    // ── Lambda — open file picker ─────────────────────────────────────────────
    val pickFile: () -> Unit = {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        val chooser = JFileChooser().apply {
            dialogTitle = "Select Student Excel File"
            fileFilter = FileNameExtensionFilter("Excel Files (*.xlsx)", "xlsx")
            isAcceptAllFileFilterUsed = false
        }
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            val file = chooser.selectedFile
            scope.launch {
                step = AppStep.PROCESSING
                statusMessage = "Reading ${file.name}…"
                val students = withContext(Dispatchers.IO) { ExcelHelper.readStudents(file) }
                if (students.isEmpty()) {
                    step = AppStep.ERROR
                    errorMessage = "No student records found. Check your Excel format.\nColumn A = Name, Column B+ = Scores"
                } else {
                    sourceFile = file
                    rawStudents = students
                    step = AppStep.LOADED
                    statusMessage = "Loaded ${students.size} student record(s) from ${file.name}"
                }
            }
        }
    }

    // ── Lambda — run grade calculation ────────────────────────────────────────
    val calculateGrades: () -> Unit = {
        scope.launch {
            step = AppStep.PROCESSING
            statusMessage = "Calculating grades…"
            val results = withContext(Dispatchers.Default) {
                calculator.calculateAll(rawStudents)
            }
            processedStudents = results
            step = AppStep.DONE
            statusMessage = "✓ Grades calculated for ${results.size} student(s). Choose a format to download."
        }
    }

    // ── Lambda — export to any format ────────────────────────────────────────
    val exportReport: (ExportFormat) -> Unit = exportReport@{ format ->
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        val src = sourceFile ?: return@exportReport
        val chooser = JFileChooser().apply {
            dialogTitle = "Save ${format.label} Report"
            selectedFile = File(src.parent, "${src.nameWithoutExtension}_graded.${format.extension}")
            fileFilter = FileNameExtensionFilter(
                "${format.label} Files (*.${format.extension})", format.extension
            )
        }
        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            scope.launch {
                val target = chooser.selectedFile.let {
                    if (!it.name.endsWith(".${format.extension}"))
                        File("${it.absolutePath}.${format.extension}") else it
                }
                statusMessage = "Saving ${format.label} report…"
                val ok = withContext(Dispatchers.IO) {
                    when (format.extension) {
                        "xlsx" -> ReportExporter.exportExcel(processedStudents, target)
                        "docx" -> ReportExporter.exportWord(processedStudents, target)
                        "pdf"  -> ReportExporter.exportPdf(processedStudents, target)
                        "html" -> ReportExporter.exportHtml(processedStudents, target)
                        else   -> false
                    }
                }
                statusMessage = if (ok)
                    "✓ ${format.label} report saved: ${target.name}"
                else
                    "✗ Failed to save ${format.label} report."
            }
        }
    }

    // ── Root layout ───────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.Background)
    ) {
        // Subtle radial glow top-left
        Box(
            modifier = Modifier
                .size(600.dp)
                .offset((-100).dp, (-100).dp)
                .background(
                    Brush.radialGradient(listOf(Color(0x1A1E90FF), Color.Transparent))
                )
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // ── Header ────────────────────────────────────────────────────────
            AppHeader()

            // ── Content ───────────────────────────────────────────────────────
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Left panel — controls + download + grade scale
                Column(
                    modifier = Modifier.width(280.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Step 1 & 2 controls
                    ControlPanel(
                        step = step,
                        sourceFile = sourceFile,
                        processedCount = processedStudents.size,
                        onPickFile = pickFile,
                        onCalculate = calculateGrades
                    )

                    // Download panel — appears only after calculation
                    AnimatedVisibility(
                        visible = step == AppStep.DONE,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        DownloadPanel(exportFormats, exportReport)
                    }

                    GradeScaleCard(calculator.getGradeScaleDisplay())
                }

                // Right panel — student table
                Column(modifier = Modifier.weight(1f)) {
                    when (step) {
                        AppStep.IDLE ->
                            EmptyStateCard()
                        AppStep.PROCESSING ->
                            LoadingCard(statusMessage)
                        AppStep.ERROR ->
                            ErrorCard(errorMessage) {
                                step = AppStep.IDLE
                                errorMessage = ""
                            }
                        AppStep.LOADED ->
                            StudentTable(
                                students = rawStudents,
                                showGrades = false,
                                title = "Loaded Students — Awaiting Calculation"
                            )
                        AppStep.DONE ->
                            StudentTable(
                                students = processedStudents,
                                showGrades = true,
                                title = "Grade Report — ${processedStudents.size} Students"
                            )
                    }
                }
            }

            // ── Status bar ────────────────────────────────────────────────────
            if (statusMessage.isNotEmpty() && step != AppStep.PROCESSING) {
                StatusBar(statusMessage)
            }
        }
    }
}

// ── Header ────────────────────────────────────────────────────────────────────
@Composable
private fun AppHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp).clip(CircleShape)
                    .background(Brush.radialGradient(listOf(AppColors.Accent, AppColors.Primary))),
                contentAlignment = Alignment.Center
            ) {
                Text("G", color = Color.White, fontWeight = FontWeight.Black, fontSize = 18.sp)
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Grade Calculator Pro", color = AppColors.TextPrimary,
                    fontSize = 22.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                Text("ICT University — Academic Grade Processor",
                    color = AppColors.TextSecondary, fontSize = 12.sp, letterSpacing = 1.sp)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("A" to AppColors.GradeA, "B+" to AppColors.GradeBPlus,
                "B" to AppColors.GradeB, "F" to AppColors.GradeF).forEach { (grade, color) ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(color.copy(alpha = 0.15f))
                        .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(grade, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
    Divider(color = AppColors.Border, thickness = 1.dp)
}

// ── Control Panel (Step 1 & 2 only) ──────────────────────────────────────────
@Composable
private fun ControlPanel(
    step: AppStep,
    sourceFile: File?,
    processedCount: Int,
    onPickFile: () -> Unit,
    onCalculate: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppColors.Surface,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("STEPS", color = AppColors.TextSecondary, fontSize = 11.sp,
                fontWeight = FontWeight.Bold, letterSpacing = 2.sp)

            // Step 1 — Upload
            StepButton(
                number = "1",
                label = if (sourceFile != null) sourceFile.name else "Upload Excel File",
                sublabel = if (sourceFile != null) "✓ File loaded" else "Select .xlsx student file",
                enabled = step != AppStep.PROCESSING,
                active = step == AppStep.IDLE,
                done = sourceFile != null,
                color = AppColors.Primary,
                onClick = onPickFile
            )

            // Step 2 — Calculate
            StepButton(
                number = "2",
                label = "Calculate Grades",
                sublabel = if (step == AppStep.DONE) "✓ $processedCount grades computed" else "Process all students",
                enabled = step == AppStep.LOADED || step == AppStep.DONE,
                active = step == AppStep.LOADED,
                done = step == AppStep.DONE,
                color = AppColors.Accent,
                onClick = onCalculate
            )
        }
    }
}

// ── Download Panel — 4 format buttons in 2x2 grid ────────────────────────────
@Composable
private fun DownloadPanel(
    formats: List<ExportFormat>,
    onExport: (ExportFormat) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppColors.Surface,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("DOWNLOAD REPORT", color = AppColors.TextSecondary,
                fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)

            // Row 1 — Excel + Word
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                formats.take(2).forEach { format ->
                    FormatButton(
                        format = format,
                        modifier = Modifier.weight(1f),
                        onClick = { onExport(format) }
                    )
                }
            }

            // Row 2 — PDF + HTML
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                formats.drop(2).forEach { format ->
                    FormatButton(
                        format = format,
                        modifier = Modifier.weight(1f),
                        onClick = { onExport(format) }
                    )
                }
            }
        }
    }
}

// ── Single format download button ─────────────────────────────────────────────
@Composable
private fun FormatButton(
    format: ExportFormat,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(62.dp)
            .border(1.dp, format.color.copy(alpha = 0.45f), RoundedCornerShape(12.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = format.color.copy(alpha = 0.09f),
            contentColor = AppColors.TextPrimary
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(format.emoji, fontSize = 20.sp)
            Spacer(Modifier.height(2.dp))
            Text(
                format.label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = format.color
            )
        }
    }
}

// ── Step Button ───────────────────────────────────────────────────────────────
@Composable
private fun StepButton(
    number: String,
    label: String,
    sublabel: String,
    enabled: Boolean,
    active: Boolean,
    done: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    val borderColor = when {
        done   -> color.copy(alpha = 0.6f)
        active -> color
        else   -> AppColors.Border
    }
    val bgColor = if (active || done) color.copy(alpha = 0.08f) else Color.Transparent

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = bgColor,
            contentColor = if (enabled) AppColors.TextPrimary else AppColors.TextDisabled,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = AppColors.TextDisabled
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp).clip(CircleShape)
                    .background(if (enabled) color.copy(0.25f) else AppColors.Border),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (done) "✓" else number,
                    color = if (enabled) color else AppColors.TextDisabled,
                    fontSize = 12.sp, fontWeight = FontWeight.Bold
                )
            }
            Column {
                Text(label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(sublabel, fontSize = 10.sp,
                    color = if (enabled) AppColors.TextSecondary else AppColors.TextDisabled,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

// ── Grade Scale Card ──────────────────────────────────────────────────────────
@Composable
private fun GradeScaleCard(items: List<Pair<String, String>>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AppColors.Surface,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("GRADE SCALE", color = AppColors.TextSecondary, fontSize = 11.sp,
                fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            items.forEach { (grade, range) ->
                val color = gradeColor(grade)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(color.copy(alpha = 0.18f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(grade, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(range, color = AppColors.TextSecondary, fontSize = 12.sp)
                }
            }
            Spacer(Modifier.height(4.dp))
            Divider(color = AppColors.Border)
            Text("Minimum passing score: 40.0", color = AppColors.Warning,
                fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
    }
}

// ── Student Results Table ─────────────────────────────────────────────────────
@Composable
private fun StudentTable(
    students: List<Student>,
    showGrades: Boolean,
    title: String
) {
    val passed = if (showGrades) students.count { it.passed == true } else 0
    val failed = if (showGrades) students.size - passed else 0

    Surface(modifier = Modifier.fillMaxSize(), color = AppColors.Surface,
        shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, color = AppColors.TextPrimary,
                    fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                if (showGrades) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        StatBadge("$passed PASS", AppColors.Pass)
                        StatBadge("$failed FAIL", AppColors.Fail)
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(AppColors.SurfaceVariant)
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text("#", color = AppColors.TextSecondary, fontSize = 11.sp,
                    fontWeight = FontWeight.Bold, modifier = Modifier.width(32.dp))
                Text("STUDENT NAME", color = AppColors.TextSecondary, fontSize = 11.sp,
                    fontWeight = FontWeight.Bold, modifier = Modifier.weight(1.8f), letterSpacing = 1.sp)
                Text("SCORES", color = AppColors.TextSecondary, fontSize = 11.sp,
                    fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f), letterSpacing = 1.sp)
                if (showGrades) {
                    Text("AVG", color = AppColors.TextSecondary, fontSize = 11.sp,
                        fontWeight = FontWeight.Bold, modifier = Modifier.width(60.dp),
                        textAlign = TextAlign.Center, letterSpacing = 1.sp)
                    Text("GRADE", color = AppColors.TextSecondary, fontSize = 11.sp,
                        fontWeight = FontWeight.Bold, modifier = Modifier.width(60.dp),
                        textAlign = TextAlign.Center, letterSpacing = 1.sp)
                    Text("RESULT", color = AppColors.TextSecondary, fontSize = 11.sp,
                        fontWeight = FontWeight.Bold, modifier = Modifier.width(70.dp),
                        textAlign = TextAlign.Center, letterSpacing = 1.sp)
                }
            }

            Spacer(Modifier.height(8.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                itemsIndexed(students) { index, student ->
                    StudentRow(index + 1, student, showGrades, index % 2 == 0)
                }
            }
        }
    }
}

@Composable
private fun StudentRow(index: Int, student: Student, showGrades: Boolean, alternate: Boolean) {
    val bg = if (alternate) AppColors.SurfaceVariant.copy(alpha = 0.4f) else Color.Transparent
    val gradeColor = if (showGrades) gradeColor(student.grade) else AppColors.TextSecondary
    val passed = student.passed == true

    Row(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$index", color = AppColors.TextDisabled, fontSize = 12.sp,
            modifier = Modifier.width(32.dp))
        Text(student.name, color = AppColors.TextPrimary, fontSize = 13.sp,
            fontWeight = FontWeight.Medium, modifier = Modifier.weight(1.8f),
            maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(
            student.scores.joinToString("  ") { "%.0f".format(it) },
            color = AppColors.TextSecondary, fontSize = 12.sp,
            modifier = Modifier.weight(2f), maxLines = 1, overflow = TextOverflow.Ellipsis
        )
        if (showGrades) {
            Text("%.1f".format(student.average ?: 0.0), color = AppColors.TextPrimary,
                fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
                modifier = Modifier.width(60.dp), textAlign = TextAlign.Center)
            Box(modifier = Modifier.width(60.dp), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(6.dp))
                        .background(gradeColor.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(student.grade ?: "—", color = gradeColor,
                        fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Box(modifier = Modifier.width(70.dp), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(6.dp))
                        .background(if (passed) AppColors.PassBg else AppColors.FailBg)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(if (passed) "PASS" else "FAIL",
                        color = if (passed) AppColors.Pass else AppColors.Fail,
                        fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ── Empty / Loading / Error States ───────────────────────────────────────────
@Composable
private fun EmptyStateCard() {
    Surface(modifier = Modifier.fillMaxSize(), color = AppColors.Surface,
        shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Box(modifier = Modifier.size(80.dp).clip(CircleShape)
                .background(AppColors.SurfaceVariant),
                contentAlignment = Alignment.Center) {
                Text("📊", fontSize = 36.sp)
            }
            Spacer(Modifier.height(20.dp))
            Text("No File Loaded", color = AppColors.TextPrimary,
                fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text("Upload a student Excel file to get started.",
                color = AppColors.TextSecondary, fontSize = 13.sp, textAlign = TextAlign.Center)
            Spacer(Modifier.height(6.dp))
            Text("Format: Column A = Name, Column B+ = Scores",
                color = AppColors.TextDisabled, fontSize = 11.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun LoadingCard(message: String) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        0.4f, 1f, InfiniteRepeatableSpec(tween(800), RepeatMode.Reverse)
    )
    Surface(modifier = Modifier.fillMaxSize(), color = AppColors.Surface,
        shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            CircularProgressIndicator(color = AppColors.Primary, modifier = Modifier.size(48.dp))
            Spacer(Modifier.height(20.dp))
            Text(message, color = AppColors.TextPrimary.copy(alpha = alpha),
                fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun ErrorCard(message: String, onDismiss: () -> Unit) {
    Surface(modifier = Modifier.fillMaxSize(), color = AppColors.Surface,
        shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            Text("⚠", fontSize = 40.sp)
            Spacer(Modifier.height(16.dp))
            Text("Something Went Wrong", color = AppColors.Fail,
                fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Text(message, color = AppColors.TextSecondary, fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp))
            Spacer(Modifier.height(24.dp))
            Button(onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = AppColors.SurfaceVariant)) {
                Text("Try Again", color = AppColors.TextPrimary)
            }
        }
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────
@Composable
private fun StatBadge(text: String, color: Color) {
    Box(
        modifier = Modifier.clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun StatusBar(message: String) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(AppColors.Surface)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(AppColors.Pass))
        Text(message, color = AppColors.TextSecondary, fontSize = 12.sp)
    }
}