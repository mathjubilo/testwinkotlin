package com.example.testwinkotlin.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    bodySmall = TextStyle(
        fontFamily = RobotoCondensedLight,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = RobotoCondensedLight,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = RobotoCondensedLight,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    ),

    displaySmall = TextStyle(
        fontFamily = RobotoCondensedRegular,
        fontSize = 13.sp
    ),

    displayMedium = TextStyle(
        fontFamily = RobotoCondensedMedium,
        fontSize = 16.sp
    ),

    displayLarge = TextStyle(
        fontFamily = RobotoCondensedBold,
        fontSize = 20.sp
    ),

    titleSmall = TextStyle(
        fontFamily = RobotoCondensedMedium,
        fontSize = 13.sp
    ),
)
