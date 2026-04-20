package org.example

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import org.example.ui.GradeApp

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Grade Calculator Pro — ICT University",
        state = WindowState(
            width = 1100.dp,
            height = 720.dp,
            position = WindowPosition(Alignment.Center)
        ),
        resizable = true,
        visible = true
    ) {
        GradeApp()
    }
}