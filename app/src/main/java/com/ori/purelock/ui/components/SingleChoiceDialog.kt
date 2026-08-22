package com.ori.purelock.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp

/** One selectable row inside [SingleChoiceDialog]: what to show, and the value it selects. */
data class ChoiceOption<T>(val value: T, val label: String)

/**
 * A simple "pick one" dialog (radio list) used by every settings row that
 * opens a picker — Ngôn ngữ, Giao diện ứng dụng, Kích thước biểu tượng.
 * Tapping an option applies it immediately and closes the dialog, matching
 * standard Android settings picker behavior.
 */
@Composable
fun <T> SingleChoiceDialog(
    title: String,
    options: List<ChoiceOption<T>>,
    selected: T,
    onSelect: (T) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = option.value == selected,
                                onClick = {
                                    onSelect(option.value)
                                    onDismiss()
                                },
                            )
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(selected = option.value == selected, onClick = null)
                        Text(text = option.label, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        },
        confirmButton = {},
    )
}
