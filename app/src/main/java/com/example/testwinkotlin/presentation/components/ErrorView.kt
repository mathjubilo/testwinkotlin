package com.example.testwinkotlin.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ErrorView(
    errorMessage: String,
    okAction: () -> Unit
) {

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        AppText(
            text = "Error",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 2.dp)
        )
        AppText(
            text = errorMessage,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 10.dp)
        )
        AppText(
            text = "Ok",
            color = Color.White,
            modifier = Modifier
                .background(Color.Black)
                .padding(
                    horizontal = 10.dp,
                    vertical = 5.dp
                )
                .align(Alignment.CenterHorizontally)
                .clickable {
                    okAction()
                }
        )
    }
}