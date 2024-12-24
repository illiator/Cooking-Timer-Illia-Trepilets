package ua.opnu.compapp.ui.screens.viewnote

import androidx.compose.runtime.Composable
import ua.opnu.compapp.ui.components.ScreenLabel

@Composable
fun ViewNoteScreen(id: Long) {
    ScreenLabel(text = "VIEW NOTE WITH ID $id")
}