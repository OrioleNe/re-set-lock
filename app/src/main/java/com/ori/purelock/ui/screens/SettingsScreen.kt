package com.ori.purelock.ui.screens

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ori.purelock.R
import com.ori.purelock.data.AppLanguage
import com.ori.purelock.data.AppThemeMode
import com.ori.purelock.data.GroupOrder
import com.ori.purelock.data.IconSize
import com.ori.purelock.data.MainLayoutMode
import com.ori.purelock.data.NotificationSoundMode
import com.ori.purelock.data.SettingsRepository
import com.ori.purelock.ui.components.ChoiceOption
import com.ori.purelock.ui.components.MainLayoutDialog
import com.ori.purelock.ui.components.SectionCard
import com.ori.purelock.ui.components.SectionHeader
import com.ori.purelock.ui.components.SettingRow
import com.ori.purelock.ui.components.SingleChoiceDialog

/** Which picker dialog (if any) is currently open. */
private enum class OpenDialog { NONE, ICON_SIZE, MAIN_LAYOUT, GROUP_ORDER, LANGUAGE, THEME, NOTIFICATION_SOUND }

/**
 * "Thiết lập" (Settings) screen, matching the grouped sections from the
 * original app: Modules & notifications / General settings / App info.
 * (No "Upgrade to Pro" section — Pure Lock is open source.)
 *
 * Kích thước biểu tượng, Giao diện chính, Ngôn ngữ and Giao diện ứng dụng
 * are backed by [SettingsRepository] and take effect immediately app-wide.
 */
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val settings = remember(context) { SettingsRepository.getInstance(context) }

    val iconSize by settings.iconSize.collectAsState()
    val mainLayout by settings.mainLayout.collectAsState()
    val groupOrder by settings.groupOrder.collectAsState()
    val gridColumns by settings.gridColumns.collectAsState()
    val language by settings.language.collectAsState()
    val themeMode by settings.themeMode.collectAsState()
    val notificationSoundMode by settings.notificationSoundMode.collectAsState()
    val hideGoodLockDialog by settings.hideGoodLockDialog.collectAsState()
    val quickUpdateNotice by settings.quickUpdateNotice.collectAsState()
    val hideDependencyModules by settings.hideDependencyModules.collectAsState()
    val useCustomFont by settings.useCustomFont.collectAsState()

    var openDialog by remember { mutableStateOf(OpenDialog.NONE) }

    // Label helpers — kept here (not in string resources) since they depend
    // on runtime enum state, but still pull translated words from resources.
    // Marked @Composable explicitly: local functions don't inherit
    // composability just from being declared inside a @Composable function.
    @Composable
    fun iconSizeLabel(size: IconSize) = when (size) {
        IconSize.SMALL -> stringResource(R.string.icon_size_small)
        IconSize.MEDIUM -> stringResource(R.string.icon_size_medium)
        IconSize.LARGE -> stringResource(R.string.icon_size_large)
        IconSize.EXTRA_LARGE -> stringResource(R.string.icon_size_extra_large)
    }
    @Composable
    fun languageLabel(lang: AppLanguage) = when (lang) {
        AppLanguage.SYSTEM -> stringResource(R.string.language_system)
        AppLanguage.ENGLISH -> stringResource(R.string.language_english)
        AppLanguage.SPANISH -> stringResource(R.string.language_spanish)
        AppLanguage.INDONESIAN -> stringResource(R.string.language_indonesian)
        AppLanguage.ITALIAN -> stringResource(R.string.language_italian)
        AppLanguage.POLISH -> stringResource(R.string.language_polish)
        AppLanguage.PORTUGUESE -> stringResource(R.string.language_portuguese)
        AppLanguage.RUSSIAN -> stringResource(R.string.language_russian)
        AppLanguage.TURKISH -> stringResource(R.string.language_turkish)
        AppLanguage.UKRAINIAN -> stringResource(R.string.language_ukrainian)
        AppLanguage.VIETNAMESE -> stringResource(R.string.language_vietnamese)
        AppLanguage.CHINESE_SIMPLIFIED -> stringResource(R.string.language_chinese_simplified)
        AppLanguage.CHINESE_TRADITIONAL -> stringResource(R.string.language_chinese_traditional)
    }
    @Composable
    fun themeLabel(mode: AppThemeMode) = when (mode) {
        AppThemeMode.SYSTEM -> stringResource(R.string.theme_system)
        AppThemeMode.LIGHT -> stringResource(R.string.theme_light)
        AppThemeMode.DARK -> stringResource(R.string.theme_dark)
        AppThemeMode.MATERIAL_YOU -> stringResource(R.string.theme_material_you)
    }
    @Composable
    fun notificationSoundLabel(mode: NotificationSoundMode) = when (mode) {
        NotificationSoundMode.ALWAYS_OFF -> stringResource(R.string.notif_sound_always_off)
        NotificationSoundMode.ALWAYS_ON -> stringResource(R.string.notif_sound_always_on)
        NotificationSoundMode.FOLLOW_DND -> stringResource(R.string.notif_sound_follow_dnd)
    }
    @Composable
    fun layoutLabel(mode: MainLayoutMode, columns: Int) = when (mode) {
        MainLayoutMode.GROUPED -> stringResource(R.string.setting_main_layout_value)
        MainLayoutMode.GRID -> stringResource(R.string.layout_grid_columns, columns)
    }
    // Group names ("Unit"/"Guardians"/"Family") aren't translated in the
    // original app either, so we join them directly from ModuleGroup.
    fun groupOrderLabel(order: GroupOrder) = order.order.joinToString(", ") { it.displayName }
    // Same resource as layoutLabel() above, but fetched with no format args
    // so we get the raw "%d ..." template string — used to build the plain,
    // non-composable formatting function MainLayoutDialog needs internally.
    val gridColumnsTemplate = stringResource(R.string.layout_grid_columns)

    // Standard Material3 "hide on scroll" app bar: slides fully off-screen
    // as the list scrolls down, and back in when scrolling up — same as the
    // rest of Android, not a custom animation.
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        context.startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse("https://purelock.github.io/help"))
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Filled.QuestionMark,
                            contentDescription = null,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                scrollBehavior = scrollBehavior,
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn(
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding() + 24.dp,
            ),
            modifier = Modifier.fillMaxSize(),
        ) {
            // MODULES & THÔNG BÁO
            item { SectionHeader(text = stringResource(R.string.section_modules_notifications)) }
            item {
                SectionCard {
                    SettingRow(
                        title = stringResource(R.string.setting_modules_up_to_date_title),
                        description = stringResource(R.string.setting_modules_up_to_date_desc),
                        onClick = {
                            // Ported from the original app: always opens a
                            // fixed page, with the device's own Android
                            // version tacked on as a query param.
                            val uri = Uri.parse("https://purelock.github.io/?android=${Build.VERSION.SDK_INT}")
                            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                        },
                    )
                    SettingRow(
                        title = stringResource(R.string.setting_developer_notice_title),
                        description = stringResource(R.string.setting_developer_notice_desc),
                        onClick = {
                            context.startActivity(
                                Intent(Intent.ACTION_VIEW, Uri.parse("https://purelock.github.io/devs_notes"))
                            )
                        },
                    )
                    SettingRow(
                        title = stringResource(R.string.setting_notifications_title),
                        description = stringResource(R.string.setting_notifications_desc),
                        onClick = {
                            val intent = Intent("android.settings.APP_NOTIFICATION_SETTINGS")
                            intent.putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
                            context.startActivity(intent)
                        },
                    )
                }
            }

            // CÁC THIẾT LẬP CHUNG
            item { SectionHeader(text = stringResource(R.string.section_general)) }
            item {
                SectionCard {
                    SettingRow(
                        title = stringResource(R.string.setting_language_title),
                        description = languageLabel(language),
                        onClick = { openDialog = OpenDialog.LANGUAGE },
                    )
                    SettingRow(
                        title = stringResource(R.string.setting_app_theme_title),
                        description = themeLabel(themeMode),
                        onClick = { openDialog = OpenDialog.THEME },
                    )
                    SettingRow(
                        title = stringResource(R.string.setting_group_title),
                        description = groupOrderLabel(groupOrder),
                        onClick = { openDialog = OpenDialog.GROUP_ORDER },
                    )
                    SettingRow(
                        title = stringResource(R.string.setting_main_layout_title),
                        description = layoutLabel(mainLayout, gridColumns),
                        onClick = { openDialog = OpenDialog.MAIN_LAYOUT },
                    )
                    SettingRow(
                        title = stringResource(R.string.setting_icon_size_title),
                        description = iconSizeLabel(iconSize),
                        onClick = { openDialog = OpenDialog.ICON_SIZE },
                    )
                    SettingRow(
                        title = stringResource(R.string.setting_notification_sound_title),
                        description = notificationSoundLabel(notificationSoundMode),
                        onClick = { openDialog = OpenDialog.NOTIFICATION_SOUND },
                    )
                    SettingRow(
                        title = stringResource(R.string.setting_custom_font_title),
                        description = stringResource(R.string.setting_custom_font_desc),
                        checked = useCustomFont,
                        onCheckedChange = { settings.setUseCustomFont(it) },
                    )
                    SettingRow(
                        title = stringResource(R.string.setting_hide_goodlock_dialog_title),
                        description = stringResource(R.string.setting_hide_goodlock_dialog_desc),
                        checked = hideGoodLockDialog,
                        onCheckedChange = { settings.setHideGoodLockDialog(it) },
                    )
                    SettingRow(
                        title = stringResource(R.string.setting_hide_goodlock_modules_title),
                        description = stringResource(R.string.setting_hide_goodlock_modules_desc),
                        checked = hideDependencyModules,
                        onCheckedChange = { settings.setHideDependencyModules(it) },
                    )
                    SettingRow(
                        title = stringResource(R.string.setting_quick_update_notice_title),
                        description = stringResource(R.string.setting_quick_update_notice_desc),
                        checked = quickUpdateNotice,
                        onCheckedChange = { settings.setQuickUpdateNotice(it) },
                    )
                }
            }

            // THÔNG TIN ỨNG DỤNG
            item { SectionHeader(text = stringResource(R.string.section_app_info)) }
            item {
                SectionCard {
                    SettingRow(
                        title = stringResource(R.string.setting_app_version_title),
                        description = stringResource(R.string.setting_app_developer),
                    )
                }
            }
        }

        // Dark gradient scrim behind the status bar, fading in exactly as
        // the app bar collapses/hides — so the status bar icons stay
        // legible over scrolled content once the bar itself is gone,
        // matching the effect in the reference screenshot.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .align(Alignment.TopStart)
                .graphicsLayer { alpha = scrollBehavior.state.collapsedFraction }
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.45f),
                            Color.Transparent,
                        ),
                    )
                )
        )

        }

        when (openDialog) {
            OpenDialog.ICON_SIZE -> SingleChoiceDialog(
                title = stringResource(R.string.setting_icon_size_title),
                options = listOf(
                    ChoiceOption(IconSize.SMALL, iconSizeLabel(IconSize.SMALL)),
                    ChoiceOption(IconSize.MEDIUM, iconSizeLabel(IconSize.MEDIUM)),
                    ChoiceOption(IconSize.LARGE, iconSizeLabel(IconSize.LARGE)),
                    ChoiceOption(IconSize.EXTRA_LARGE, iconSizeLabel(IconSize.EXTRA_LARGE)),
                ),
                selected = iconSize,
                onSelect = { settings.setIconSize(it) },
                onDismiss = { openDialog = OpenDialog.NONE },
            )

            OpenDialog.GROUP_ORDER -> SingleChoiceDialog(
                title = stringResource(R.string.setting_group_title),
                options = listOf(
                    ChoiceOption(GroupOrder.LIFE_UP_FIRST, groupOrderLabel(GroupOrder.LIFE_UP_FIRST)),
                    ChoiceOption(GroupOrder.MAKE_UP_FIRST, groupOrderLabel(GroupOrder.MAKE_UP_FIRST)),
                ),
                selected = groupOrder,
                onSelect = { settings.setGroupOrder(it) },
                onDismiss = { openDialog = OpenDialog.NONE },
            )

            OpenDialog.LANGUAGE -> {
                val activity = context as? Activity
                SingleChoiceDialog(
                    title = stringResource(R.string.setting_language_title),
                    options = listOf(
                        ChoiceOption(AppLanguage.SYSTEM, languageLabel(AppLanguage.SYSTEM)),
                        ChoiceOption(AppLanguage.ENGLISH, languageLabel(AppLanguage.ENGLISH)),
                        ChoiceOption(AppLanguage.SPANISH, languageLabel(AppLanguage.SPANISH)),
                        ChoiceOption(AppLanguage.INDONESIAN, languageLabel(AppLanguage.INDONESIAN)),
                        ChoiceOption(AppLanguage.ITALIAN, languageLabel(AppLanguage.ITALIAN)),
                        ChoiceOption(AppLanguage.POLISH, languageLabel(AppLanguage.POLISH)),
                        ChoiceOption(AppLanguage.PORTUGUESE, languageLabel(AppLanguage.PORTUGUESE)),
                        ChoiceOption(AppLanguage.RUSSIAN, languageLabel(AppLanguage.RUSSIAN)),
                        ChoiceOption(AppLanguage.TURKISH, languageLabel(AppLanguage.TURKISH)),
                        ChoiceOption(AppLanguage.UKRAINIAN, languageLabel(AppLanguage.UKRAINIAN)),
                        ChoiceOption(AppLanguage.VIETNAMESE, languageLabel(AppLanguage.VIETNAMESE)),
                        ChoiceOption(AppLanguage.CHINESE_SIMPLIFIED, languageLabel(AppLanguage.CHINESE_SIMPLIFIED)),
                        ChoiceOption(AppLanguage.CHINESE_TRADITIONAL, languageLabel(AppLanguage.CHINESE_TRADITIONAL)),
                    ),
                    selected = language,
                    onSelect = { picked ->
                        settings.setLanguage(picked)
                        // Resources are only re-resolved on process/activity
                        // recreation, so restart the activity to apply it.
                        activity?.recreate()
                    },
                    onDismiss = { openDialog = OpenDialog.NONE },
                )
            }

            OpenDialog.THEME -> SingleChoiceDialog(
                title = stringResource(R.string.setting_app_theme_title),
                options = listOf(
                    ChoiceOption(AppThemeMode.SYSTEM, themeLabel(AppThemeMode.SYSTEM)),
                    ChoiceOption(AppThemeMode.LIGHT, themeLabel(AppThemeMode.LIGHT)),
                    ChoiceOption(AppThemeMode.DARK, themeLabel(AppThemeMode.DARK)),
                    ChoiceOption(AppThemeMode.MATERIAL_YOU, themeLabel(AppThemeMode.MATERIAL_YOU)),
                ),
                selected = themeMode,
                onSelect = { settings.setThemeMode(it) },
                onDismiss = { openDialog = OpenDialog.NONE },
            )

            OpenDialog.NOTIFICATION_SOUND -> SingleChoiceDialog(
                title = stringResource(R.string.setting_notification_sound_title),
                options = listOf(
                    ChoiceOption(NotificationSoundMode.ALWAYS_OFF, notificationSoundLabel(NotificationSoundMode.ALWAYS_OFF)),
                    ChoiceOption(NotificationSoundMode.ALWAYS_ON, notificationSoundLabel(NotificationSoundMode.ALWAYS_ON)),
                    ChoiceOption(NotificationSoundMode.FOLLOW_DND, notificationSoundLabel(NotificationSoundMode.FOLLOW_DND)),
                ),
                selected = notificationSoundMode,
                onSelect = { settings.setNotificationSoundMode(it) },
                onDismiss = { openDialog = OpenDialog.NONE },
            )

            OpenDialog.MAIN_LAYOUT -> MainLayoutDialog(
                title = stringResource(R.string.setting_main_layout_title),
                groupedLabel = stringResource(R.string.setting_main_layout_value),
                gridLabelFor = { columns -> String.format(gridColumnsTemplate, columns) },
                doneLabel = stringResource(R.string.dialog_done),
                selectedMode = mainLayout,
                selectedColumns = gridColumns,
                columnsRange = SettingsRepository.MIN_GRID_COLUMNS..SettingsRepository.MAX_GRID_COLUMNS,
                onDone = { mode, columns ->
                    settings.setMainLayout(mode)
                    settings.setGridColumns(columns)
                    openDialog = OpenDialog.NONE
                },
                onDismiss = { openDialog = OpenDialog.NONE },
            )

            OpenDialog.NONE -> {}
        }
    }
}
