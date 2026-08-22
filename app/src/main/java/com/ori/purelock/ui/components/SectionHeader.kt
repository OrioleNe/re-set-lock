package com.ori.purelock.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Section header used to group modules ("Unit" / "Guardians" / "Family")
 * and settings ("MODULES & THÔNG BÁO", "CÁC THIẾT LẬP CHUNG"...).
 *
 * [uppercase] matches the settings screen style (all-caps, letter-spaced
 * label); the module list screen uses [uppercase] = false for its plain
 * "Unit" / "Guardians" / "Family" titles.
 */
@Composable
fun SectionHeader(
    text: String,
    modifier: Modifier = Modifier,
    uppercase: Boolean = true,
) {
    Text(
        text = if (uppercase) text.uppercase() else text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
            .padding(PaddingValues(start = 20.dp, top = 24.dp, end = 20.dp, bottom = 8.dp)),
    )
}
