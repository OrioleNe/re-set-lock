package com.ori.purelock.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ori.purelock.data.Module
import com.ori.purelock.data.ResolvedModuleIcon

/**
 * A single tile in the grid layout ("Giao diện chính" -> "Lưới N cột"):
 * the module's real icon (see [ModuleIcon]) with its name below, centered.
 */
@Composable
fun ModuleGridItem(
    module: Module,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSizeDp: Int = 44,
    resolvedIcon: ResolvedModuleIcon? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        ModuleIcon(module = module, sizeDp = iconSizeDp, resolved = resolvedIcon)
        Text(
            text = module.name,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
