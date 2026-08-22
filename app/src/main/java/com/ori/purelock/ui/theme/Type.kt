package com.ori.purelock.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ori.purelock.R

/**
 * Builds a [FontFamily] from the bundled Google Sans Flex variable font,
 * pinned to one specific instance along its weight/width/slant/grade/
 * roundness/optical-size axes. Each of the 3 heading levels below is its
 * own pinned instance rather than one flexible family, since every place
 * that uses them always wants that exact same look.
 */
@OptIn(ExperimentalTextApi::class)
private fun googleSansFlexInstance(
    weight: Int,
    width: Int,
    slant: Int,
    grade: Int,
    roundness: Int,
    opticalSize: Int,
): FontFamily = FontFamily(
    Font(
        resId = R.font.google_sans_flex,
        weight = FontWeight(weight),
        variationSettings = FontVariation.Settings(
            FontVariation.Setting("wght", weight.toFloat()),
            FontVariation.Setting("wdth", width.toFloat()),
            FontVariation.Setting("slnt", slant.toFloat()),
            FontVariation.Setting("GRAD", grade.toFloat()),
            FontVariation.Setting("ROND", roundness.toFloat()),
            FontVariation.Setting("opsz", opticalSize.toFloat()),
        ),
    )
)

// Heading 1 — the "Pure Lock" / "Thiết lập" top bar titles.
private val heading1Family = googleSansFlexInstance(
    weight = 1000, width = 151, slant = 0, grade = 100, roundness = 100, opticalSize = 28,
)

// Heading 2 — module names and settings row titles.
private val heading2Family = googleSansFlexInstance(
    weight = 600, width = 100, slant = 0, grade = 100, roundness = 100, opticalSize = 22,
)

// Heading 3 — the description text under settings rows.
private val heading3Family = googleSansFlexInstance(
    weight = 450, width = 100, slant = 0, grade = 100, roundness = 100, opticalSize = 18,
)

/**
 * Material 3 Expressive type scale.
 *
 * Expressive leans on stronger weight contrast between levels (e.g. bold
 * titles next to regular-weight body text) rather than relying on size
 * alone, which is what gives the UI its more "expressive" personality.
 */
val AppTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.15.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.8.sp,
    ),
)

/**
 * Same type scale as [AppTypography], except the 3 roles the custom font
 * setting actually touches — top bar titles, row titles ("tên các ápp" /
 * setting names), and row descriptions — switch to the pinned Google Sans
 * Flex instances above, at the exact sizes given for each. Every other
 * role (titleMedium, labelLarge, labelMedium, etc.) is untouched.
 */
val AppTypographyCustomFont = AppTypography.copy(
    titleLarge = AppTypography.titleLarge.copy(
        fontFamily = heading1Family,
        fontWeight = FontWeight(1000),
    ),
    titleSmall = AppTypography.titleSmall.copy(
        fontFamily = heading2Family,
        fontWeight = FontWeight(600),
    ),
    bodyLarge = AppTypography.bodyLarge.copy(
        fontFamily = heading2Family,
        fontWeight = FontWeight(600),
    ),
    bodyMedium = AppTypography.bodyMedium.copy(
        fontFamily = heading3Family,
        fontWeight = FontWeight(450),
    ),
)
