package com.ori.purelock.data

/**
 * Icon size for module rows/grid tiles ("Kích thước các biểu tượng").
 *
 * Actual icon size = a layout-dependent base size × this multiplier
 * (Nhỏ=1x, Vừa=1.5x, Lớn=2.25x, Lớn nhất=3x). The base itself differs by
 * "Giao diện chính": larger for the grouped list rows, smaller for grid
 * tiles (see [SettingsRepository] / ModulesScreen, which pick the right
 * base before applying this).
 */
enum class IconSize(val multiplier: Float) {
    SMALL(1f),
    MEDIUM(1.5f),
    LARGE(2.25f),
    EXTRA_LARGE(3f),
}

/**
 * In-app notification sound ("Âm thanh thông báo trong ứng dụng", key `AF`
 * in the original app).
 */
enum class NotificationSoundMode {
    ALWAYS_OFF,
    ALWAYS_ON,
    FOLLOW_DND,
}

/** Main screen layout mode ("Giao diện chính"): grouped list, or an N-column grid. */
enum class MainLayoutMode {
    GROUPED,
    GRID,
}

/**
 * App language override ("Ngôn ngữ"). SYSTEM follows the device language.
 * The full list matches the languages screen of the original app; string
 * resources for each are only as complete as what was recoverable from the
 * original app's translation.json (see values-xx/strings.xml) — anything
 * missing for a given locale silently falls back to the English default,
 * which is normal Android resource behavior.
 */
enum class AppLanguage(val tag: String?) {
    SYSTEM(null),
    ENGLISH("en"),
    SPANISH("es"),
    INDONESIAN("id"),
    ITALIAN("it"),
    POLISH("pl"),
    PORTUGUESE("pt"),
    RUSSIAN("ru"),
    TURKISH("tr"),
    UKRAINIAN("uk"),
    VIETNAMESE("vi"),
    CHINESE_SIMPLIFIED("zh-CN"),
    CHINESE_TRADITIONAL("zh-TW"),
}

/**
 * App theme mode ("Giao diện ứng dụng"). SYSTEM follows the device
 * dark/light setting. MATERIAL_YOU uses Android 12+'s wallpaper-based
 * dynamic color, matching the original app's "Thích nghi (Material You)"
 * option; on older Android versions it falls back to the SYSTEM behavior
 * (dynamic color isn't available there — see AppTheme in Theme.kt).
 */
enum class AppThemeMode {
    SYSTEM,
    LIGHT,
    DARK,
    MATERIAL_YOU,
}
