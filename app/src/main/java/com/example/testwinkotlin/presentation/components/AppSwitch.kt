package com.example.testwinkotlin.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope

@Composable
fun AppSwitch(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppText(
            text = "Biometric Login",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier
                .padding(start = 20.dp)
        )
        Switch(
            checked = isChecked,
            onCheckedChange = {
                onCheckedChange(it)
            }
        )
    }
}

