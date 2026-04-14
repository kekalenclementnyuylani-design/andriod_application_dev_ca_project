package org.example.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ── Colour Palette — Dark Navy Professional ──────────────────────────────────
object AppColors {
    // Backgrounds
    val Background     = Color(0xFF050D1A)   // deepest navy
    val Surface        = Color(0xFF0A1628)   // card background
    val SurfaceVariant = Color(0xFF0F2040)   // elevated card
    val Border         = Color(0xFF1A3A6B)   // subtle borders

    // Accents
    val Primary        = Color(0xFF1E90FF)   // dodger blue — primary action
    val PrimaryDim     = Color(0xFF1565C0)   // darker blue — hover/pressed
    val Accent         = Color(0xFF00D4FF)   // cyan — highlights
    val AccentGlow     = Color(0x3300D4FF)   // glow effect (transparent)

    // Status
    val Pass           = Color(0xFF00E676)   // bright green — PASS
    val PassBg         = Color(0x1500E676)   // green tint background
    val Fail           = Color(0xFFFF1744)   // vivid red — FAIL
    val FailBg         = Color(0x15FF1744)   // red tint background
    val Warning        = Color(0xFFFFAB00)   // amber — warning

    // Text
    val TextPrimary    = Color(0xFFECF0FF)   // near white with blue tint
    val TextSecondary  = Color(0xFF7B9AC4)   // muted blue-grey
    val TextDisabled   = Color(0xFF3D5A80)   // very dim

    // Grade badge colours
    val GradeA         = Color(0xFF00E676)
    val GradeBPlus     = Color(0xFF29B6F6)
    val GradeB         = Color(0xFF26C6DA)
    val GradeCPlus     = Color(0xFFFFCA28)
    val GradeC         = Color(0xFFFFB300)
    val GradeDPlus     = Color(0xFFFF7043)
    val GradeD         = Color(0xFFFF5722)
    val GradeF         = Color(0xFFFF1744)
}

// ── Grade → colour mapping ────────────────────────────────────────────────────
fun gradeColor(grade: String?): Color = when (grade) {
    "A"  -> AppColors.GradeA
    "B+" -> AppColors.GradeBPlus
    "B"  -> AppColors.GradeB
    "C+" -> AppColors.GradeCPlus
    "C"  -> AppColors.GradeC
    "D+" -> AppColors.GradeDPlus
    "D"  -> AppColors.GradeD
    else -> AppColors.GradeF
}
