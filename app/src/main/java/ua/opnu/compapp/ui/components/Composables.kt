package ua.opnu.compapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ua.opnu.compapp.ui.theme.AppTypography

@Composable
fun ScreenLabel(text: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            text = text,
            style = AppTypography.bodyMedium
        )

    }

}