package org.khiot.iotcibiruwetan.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.khiot.iotcibiruwetan.R

// ✅ Define custom font
val PlusJakartaSans = FontFamily(
    Font(R.font.plus_jakarta_sans_regular, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans_bold, FontWeight.Bold)
)

val textColor = Color(android.graphics.Color.parseColor("#2B4141"))

// ✅ Apply font ke Typography
val AppTypography = Typography(
    bodyLarge = androidx.compose.ui.text.TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodySmall = androidx.compose.ui.text.TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = Color(0xFF6B7280)
    ),
    headlineMedium = androidx.compose.ui.text.TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    headlineLarge = androidx.compose.ui.text.TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 24.sp
    ),
    labelLarge = androidx.compose.ui.text.TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp
    )
)

// ✅ Custom color scheme
private val LightColors = lightColorScheme(
    primary = Color(0xFF0EB1D2),
    secondary = Color(0xFF00677A),
    tertiary = Color(0xFF5C5C5C)
)

@Composable
fun MyAppTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = AppTypography,
        content = content,
    )
}
