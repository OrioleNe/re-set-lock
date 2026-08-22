package com.ori.purelock.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A settings row with a title and description, optionally with a trailing
 * switch (e.g. "Sử dụng font chữ tùy chỉnh") or just a tappable value row
 * (e.g. "Ngôn ngữ" -> "Tiếng Việt").
 *
 * Pass [checked]/[onCheckedChange] to show a switch — tapping anywhere on
 * the row (not just the switch itself) toggles it, matching standard
 * Android settings behavior. Leave them null for a plain tappable row.
 */
@Composable
fun SettingRow(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    checked: Boolean? = null,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    val isSwitchRow = checked != null && onCheckedChange != null
    val rowClick: (() -> Unit)? = when {
        isSwitchRow -> { { onCheckedChange!!(!checked!!) } }
        onClick != null -> onClick
        else -> null
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (rowClick != null) Modifier.clickable(onClick = rowClick) else Modifier
            )
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }
        if (isSwitchRow) {
            Row {
                // onCheckedChange is null here on purpose: the whole row
                // above already handles the toggle, so the switch itself
                // just reflects state and doesn't double-handle taps.
                Switch(checked = checked!!, onCheckedChange = null)
            }
        }
    }
}
