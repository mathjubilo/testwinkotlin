package com.inditex.wms.rfdevice.win.winkotlin.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.inditex.wms.rfdevice.win.winkotlin.theme.customShapes.TriangleShape

@Composable
fun Triangle(
    width: Float,
    height: Float,
    color: Color,
    pointingRight: Boolean,
    modifier: Modifier = Modifier
) {
    Spacer(
        modifier = modifier
            .width(width.dp)
            .height(height.dp)
            .clip(shape = TriangleShape(pointingRight))
            .background(color)
    )
}