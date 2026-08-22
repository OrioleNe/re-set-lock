package com.ori.purelock.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ori.purelock.data.MainLayoutMode

/**
 * Picker for "Giao diện chính": Theo nhóm (grouped) vs Lưới (grid). When
 * Lưới is selected, a column-count slider appears below it, matching the
 * original app's "Lưới 2-10 cột" option (here scoped to 2-6 for a
 * phone-sized grid of real app icons).
 */
@Composable
fun MainLayoutDialog(
    title: String,
    groupedLabel: String,
    gridLabelFor: (Int) -> String,
    doneLabel: String,
    selectedMode: MainLayoutMode,
    selectedColumns: Int,
    columnsRange: IntRange,
    onDone: (MainLayoutMode, Int) -> Unit,
    onDismiss: () -> Unit,
) {
    var mode by remember { mutableStateOf(selectedMode) }
    var columns by remember { mutableStateOf(selectedColumns) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = mode == MainLayoutMode.GROUPED,
                            onClick = { mode = MainLayoutMode.GROUPED },
                        )
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(selected = mode == MainLayoutMode.GROUPED, onClick = null)
                    Text(text = groupedLabel, modifier = Modifier.padding(start = 8.dp))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = mode == MainLayoutMode.GRID,
                            onClick = { mode = MainLayoutMode.GRID },
                        )
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(selected = mode == MainLayoutMode.GRID, onClick = null)
                    Text(text = gridLabelFor(columns), modifier = Modifier.padding(start = 8.dp))
                }
                if (mode == MainLayoutMode.GRID) {
                    Slider(
                        value = columns.toFloat(),
                        onValueChange = { columns = it.toInt() },
                        valueRange = columnsRange.first.toFloat()..columnsRange.last.toFloat(),
                        steps = (columnsRange.last - columnsRange.first - 1).coerceAtLeast(0),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 40.dp, end = 8.dp),
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onDone(mode, columns) }) {
                Text(doneLabel, color = MaterialTheme.colorScheme.primary)
            }
        },
    )
}
