package com.ori.purelock.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ori.purelock.data.Module
import com.ori.purelock.data.ResolvedModuleIcon

/**
 * A single row in the module list (e.g. "LockStar"): the module's real icon
 * on the left (see [ModuleIcon]), the module name, and a trailing chevron.
 */
@Composable
fun ModuleRow(
    module: Module,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconSizeDp: Int = 44,
    resolvedIcon: ResolvedModuleIcon? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ModuleIcon(module = module, sizeDp = iconSizeDp, resolved = resolvedIcon)
        Box(modifier = Modifier.width(16.dp))
        Text(
            text = module.name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
