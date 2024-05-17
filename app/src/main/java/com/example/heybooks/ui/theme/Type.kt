package com.example.heybooks.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.example.heybooks.R

private val Grotesk = FontFamily(
    Font(R.font.grotesk_light),
    Font(R.font.grotesk_medium, FontWeight.W500),
    Font(R.font.grotesk_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
val typography = Typography(
// subtitle1
    titleLarge = TextStyle(
        textDecoration = TextDecoration.None,
        fontFamily = Grotesk,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp
    ),
    //subtitle2
    titleMedium = TextStyle(
        textDecoration = TextDecoration.None,
        fontFamily = Grotesk,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    //caption
    titleSmall = TextStyle(
        textDecoration = TextDecoration.None,
        fontFamily = Grotesk,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    //body1
    bodyLarge = TextStyle(
        textDecoration = TextDecoration.None,
        fontFamily = Grotesk,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    //body2
    bodyMedium = TextStyle(
        textDecoration = TextDecoration.None,
        fontFamily = Grotesk,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    //overline
    headlineMedium = TextStyle(
        textDecoration = TextDecoration.None,
        fontFamily = Grotesk,
        fontWeight = FontWeight.W500,
        fontSize = 12.sp

    ),
    //h2
    displayLarge = TextStyle(
        textDecoration = TextDecoration.None,
        fontFamily = Grotesk,
        fontWeight = FontWeight.W600,
        fontSize = 48.sp
    ),
    //h3
    displayMedium = TextStyle(
        textDecoration = TextDecoration.None,
        fontFamily = Grotesk,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp
    ),
    //h4
    displaySmall = TextStyle(
        textDecoration = TextDecoration.None,
        fontFamily = Grotesk,
        fontWeight = FontWeight.W600,
        fontSize = 30.sp
    ),
    //h5

    headlineLarge = TextStyle(
        textDecoration = TextDecoration.None,
        fontFamily = Grotesk,
        fontWeight = FontWeight.W600,
        fontSize = 24.sp
    ),
    //h6
    headlineSmall = TextStyle(
        textDecoration = TextDecoration.None,
        fontFamily = Grotesk,
        fontWeight = FontWeight.W600,
        fontSize = 20.sp
    ),
    //button
    labelLarge = TextStyle(
        textDecoration = TextDecoration.None,
        fontFamily = Grotesk,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
)