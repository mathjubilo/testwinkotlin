package com.example.testwinkotlin.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun AppText(
    text: String,
    color: Color = MaterialTheme.colorScheme.onSurface,
    style: TextStyle? = MaterialTheme.typography.displaySmall,
    modifier: Modifier = Modifier
) {
    style?.let { it
        Text(
            text =  text,
            color = color,
            style = it,
            modifier = modifier
        )
    }
}