package com.ori.purelock.data

import android.content.Context
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Persists Pure Lock's user-facing settings (icon size, main layout,
 * language, theme) to SharedPreferences and exposes them as [StateFlow]s so
 * every screen observing them recomposes as soon as a value changes.
 *
 * A single instance is shared app-wide via [getInstance], so a change made
 * in [com.ori.purelock.ui.screens.SettingsScreen] is immediately visible
 * to [com.ori.purelock.ui.screens.ModulesScreen] without any extra wiring.
 */
class SettingsRepository private constructor(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _iconSize = MutableStateFlow(readEnum(KEY_ICON_SIZE, IconSize.MEDIUM))
    val iconSize: StateFlow<IconSize> = _iconSize

    private val _mainLayout = MutableStateFlow(readEnum(KEY_MAIN_LAYOUT, MainLayoutMode.GROUPED))
    val mainLayout: StateFlow<MainLayoutMode> = _mainLayout

    private val _groupOrder = MutableStateFlow(readEnum(KEY_GROUP_ORDER, GroupOrder.MAKE_UP_FIRST))
    val groupOrder: StateFlow<GroupOrder> = _groupOrder

    private val _gridColumns = MutableStateFlow(
        prefs.getInt(KEY_GRID_COLUMNS, DEFAULT_GRID_COLUMNS).coerceIn(MIN_GRID_COLUMNS, MAX_GRID_COLUMNS)
    )
    val gridColumns: StateFlow<Int> = _gridColumns

    private val _language = MutableStateFlow(readEnum(KEY_LANGUAGE, AppLanguage.SYSTEM))
    val language: StateFlow<AppLanguage> = _language

    private val _themeMode = MutableStateFlow(readEnum(KEY_THEME_MODE, AppThemeMode.SYSTEM))
    val themeMode: StateFlow<AppThemeMode> = _themeMode

    private val _notificationSoundMode = MutableStateFlow(
        readEnum(KEY_NOTIFICATION_SOUND, NotificationSoundMode.FOLLOW_DND)
    )
    val notificationSoundMode: StateFlow<NotificationSoundMode> = _notificationSoundMode

    // AE: hide the Good Lock warning dialog. AH: show a quick notice when a
    // module update is available. AI: hide Good Lock + the 2 Good Guardians
    // dependency modules from the list (see ModulesScreen).
    private val _hideGoodLockDialog = MutableStateFlow(prefs.getBoolean(KEY_HIDE_GOODLOCK_DIALOG, false))
    val hideGoodLockDialog: StateFlow<Boolean> = _hideGoodLockDialog

    private val _quickUpdateNotice = MutableStateFlow(prefs.getBoolean(KEY_QUICK_UPDATE_NOTICE, true))
    val quickUpdateNotice: StateFlow<Boolean> = _quickUpdateNotice

    private val _hideDependencyModules = MutableStateFlow(prefs.getBoolean(KEY_HIDE_DEPENDENCY_MODULES, false))
    val hideDependencyModules: StateFlow<Boolean> = _hideDependencyModules

    // AK: use the custom (Google Sans Flex) font instead of the system one.
    private val _useCustomFont = MutableStateFlow(prefs.getBoolean(KEY_USE_CUSTOM_FONT, true))
    val useCustomFont: StateFlow<Boolean> = _useCustomFont

    fun setIconSize(size: IconSize) {
        _iconSize.value = size
        prefs.edit { putString(KEY_ICON_SIZE, size.name) }
    }

    fun setMainLayout(mode: MainLayoutMode) {
        _mainLayout.value = mode
        prefs.edit { putString(KEY_MAIN_LAYOUT, mode.name) }
    }

    fun setGroupOrder(order: GroupOrder) {
        _groupOrder.value = order
        prefs.edit { putString(KEY_GROUP_ORDER, order.name) }
    }

    fun setGridColumns(columns: Int) {
        val clamped = columns.coerceIn(MIN_GRID_COLUMNS, MAX_GRID_COLUMNS)
        _gridColumns.value = clamped
        prefs.edit { putInt(KEY_GRID_COLUMNS, clamped) }
    }

    fun setLanguage(language: AppLanguage) {
        _language.value = language
        prefs.edit { putString(KEY_LANGUAGE, language.name) }
    }

    fun setThemeMode(mode: AppThemeMode) {
        _themeMode.value = mode
        prefs.edit { putString(KEY_THEME_MODE, mode.name) }
    }

    fun setNotificationSoundMode(mode: NotificationSoundMode) {
        _notificationSoundMode.value = mode
        prefs.edit { putString(KEY_NOTIFICATION_SOUND, mode.name) }
    }

    fun setHideGoodLockDialog(hide: Boolean) {
        _hideGoodLockDialog.value = hide
        prefs.edit { putBoolean(KEY_HIDE_GOODLOCK_DIALOG, hide) }
    }

    fun setQuickUpdateNotice(enabled: Boolean) {
        _quickUpdateNotice.value = enabled
        prefs.edit { putBoolean(KEY_QUICK_UPDATE_NOTICE, enabled) }
    }

    fun setHideDependencyModules(hide: Boolean) {
        _hideDependencyModules.value = hide
        prefs.edit { putBoolean(KEY_HIDE_DEPENDENCY_MODULES, hide) }
    }

    fun setUseCustomFont(enabled: Boolean) {
        _useCustomFont.value = enabled
        prefs.edit { putBoolean(KEY_USE_CUSTOM_FONT, enabled) }
    }

    private inline fun <reified T : Enum<T>> readEnum(key: String, default: T): T {
        val raw = prefs.getString(key, null) ?: return default
        return try {
            enumValueOf<T>(raw)
        } catch (e: IllegalArgumentException) {
            default
        }
    }

    companion object {
        private const val PREFS_NAME = "fine_lock_settings"
        private const val KEY_ICON_SIZE = "icon_size"
        private const val KEY_MAIN_LAYOUT = "main_layout"
        private const val KEY_GROUP_ORDER = "group_order"
        private const val KEY_GRID_COLUMNS = "grid_columns"
        private const val KEY_LANGUAGE = "language"
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_NOTIFICATION_SOUND = "notification_sound"
        private const val KEY_HIDE_GOODLOCK_DIALOG = "hide_goodlock_dialog"
        private const val KEY_QUICK_UPDATE_NOTICE = "quick_update_notice"
        private const val KEY_HIDE_DEPENDENCY_MODULES = "hide_dependency_modules"
        private const val KEY_USE_CUSTOM_FONT = "use_custom_font"

        const val DEFAULT_GRID_COLUMNS = 4
        const val MIN_GRID_COLUMNS = 2
        const val MAX_GRID_COLUMNS = 6

        // Base icon sizes the original app multiplies by IconSize before
        // drawing (grouped rows are drawn bigger than grid tiles there too).
        const val ICON_BASE_DP_GROUPED = 40
        const val ICON_BASE_DP_GRID = 28

        @Volatile
        private var instance: SettingsRepository? = null

        fun getInstance(context: Context): SettingsRepository =
            instance ?: synchronized(this) {
                instance ?: SettingsRepository(context).also { instance = it }
            }
    }
}
