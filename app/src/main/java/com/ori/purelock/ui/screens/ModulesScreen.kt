package com.ori.purelock.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ori.purelock.R
import com.ori.purelock.data.MainLayoutMode
import com.ori.purelock.data.Module
import com.ori.purelock.data.ModuleGroup
import com.ori.purelock.data.ResolvedModuleIcon
import com.ori.purelock.data.SampleModules
import com.ori.purelock.data.SettingsRepository
import com.ori.purelock.data.resolveModuleIcons
import com.ori.purelock.ui.components.ModuleGridItem
import com.ori.purelock.ui.components.ModuleRow
import com.ori.purelock.ui.components.ModulesSkeleton
import com.ori.purelock.ui.components.SectionCard
import com.ori.purelock.ui.components.SectionHeader
import kotlin.math.roundToInt

/**
 * Main screen: "Pure Lock" — the module list, shown grouped into Make up /
 * Life up / Guardians / Dependencies or as an N-column grid depending on
 * the "Giao diện chính" setting, with icon sizes following the "Kích thước
 * các biểu tượng" setting (see [SettingsRepository]).
 *
 * On first entry, shows [ModulesSkeleton] — a shimmering skeleton shaped
 * like the real top bar + grouped list — in place of the module list, and
 * instead of the OS's default icon splash, until the module data is
 * *actually* ready: every module's real install state/icon is checked up
 * front, in parallel, via [resolveModuleIcons]. The skeleton is dismissed
 * exactly when that finishes, no earlier and no artificially later, and the
 * results are handed down to each row/tile so they don't re-check
 * themselves.
 */
@Composable
fun ModulesScreen(
    onModuleClick: (Module) -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    modules: List<Module> = SampleModules,
) {
    val context = LocalContext.current
    val settings = remember(context) { SettingsRepository.getInstance(context) }
    val iconSize by settings.iconSize.collectAsState()
    val mainLayout by settings.mainLayout.collectAsState()
    val groupOrder by settings.groupOrder.collectAsState()
    val gridColumns by settings.gridColumns.collectAsState()
    val hideDependencyModules by settings.hideDependencyModules.collectAsState()

    var isLoading by remember { mutableStateOf(true) }
    var resolvedIcons by remember { mutableStateOf<Map<String, ResolvedModuleIcon>>(emptyMap()) }
    LaunchedEffect(modules) {
        isLoading = true
        resolvedIcons = resolveModuleIcons(context, modules)
        isLoading = false
    }

    if (isLoading) {
        ModulesSkeleton(modifier = modifier)
        return
    }

    // Base size (bigger for grouped rows, smaller for grid tiles) × the
    // Nhỏ/Vừa/Lớn/Lớn nhất multiplier (now fractional: 1x/1.5x/2.25x/3x),
    // rounded to the nearest whole dp since ModuleIcon takes an Int size.
    val groupedIconSizeDp = (SettingsRepository.ICON_BASE_DP_GROUPED * iconSize.multiplier).roundToInt()
    val gridIconSizeDp = (SettingsRepository.ICON_BASE_DP_GRID * iconSize.multiplier).roundToInt()

    // AI in the original app: hide Good Lock + the 2 Good Guardians modules
    // (the whole "Dependencies" group) from the list.
    val visibleModules = remember(modules, hideDependencyModules) {
        if (hideDependencyModules) modules.filter { it.group != ModuleGroup.DEPENDENCIES } else modules
    }

    val grouped = visibleModules.groupBy { it.group }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Filled.Tune,
                            contentDescription = stringResource(R.string.settings_title),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { innerPadding ->
        when (mainLayout) {
            MainLayoutMode.GROUPED -> {
                LazyColumn(
                    contentPadding = PaddingValues(
                        top = innerPadding.calculateTopPadding(),
                        bottom = innerPadding.calculateBottomPadding() + 24.dp,
                    ),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    for (group in groupOrder.order) {
                        val rows = grouped[group].orEmpty()
                        if (rows.isEmpty()) continue

                        item(key = "header_${group.name}") {
                            SectionHeader(text = group.displayName, uppercase = false)
                        }
                        item(key = "card_${group.name}") {
                            SectionCard {
                                rows.forEach { module ->
                                    ModuleRow(
                                        module = module,
                                        onClick = { onModuleClick(module) },
                                        iconSizeDp = groupedIconSizeDp,
                                        resolvedIcon = resolvedIcons[module.packageName],
                                    )
                                }
                            }
                        }
                    }
                }
            }

            MainLayoutMode.GRID -> {
                val orderedModules = remember(visibleModules, groupOrder) {
                    visibleModules.sortedBy { groupOrder.order.indexOf(it.group) }
                }
                LazyVerticalGrid(
                    columns = GridCells.Fixed(gridColumns),
                    contentPadding = PaddingValues(
                        top = innerPadding.calculateTopPadding(),
                        start = 12.dp,
                        end = 12.dp,
                        bottom = innerPadding.calculateBottomPadding() + 24.dp,
                    ),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(orderedModules, key = { it.id }) { module ->
                        ModuleGridItem(
                            module = module,
                            onClick = { onModuleClick(module) },
                            iconSizeDp = gridIconSizeDp,
                            resolvedIcon = resolvedIcons[module.packageName],
                        )
                    }
                }
            }
        }
    }
}
