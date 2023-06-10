package com.inditex.wms.rfdevice.win.winkotlin.theme.customShapes

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class TriangleShape(pointingRight: Boolean) : Shape {
    val pointingRight = pointingRight
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val trianglePath = Path().apply {

            if (pointingRight) {

                moveTo(x = size.width, y = size.height/2)
                lineTo(x = 0f, y = size.height)
                lineTo(x = 0f, y = 0f)
            } else {

                moveTo(x = 0f, y = size.height/2)
                lineTo(x = size.width, y = size.height)
                lineTo(x = size.width, y = 0f)
            }
        }
        return Outline.Generic(path = trianglePath)
    }
}