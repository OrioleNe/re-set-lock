package com.ori.purelock.data

/**
 * A single Good Lock module entry shown in the Pure Lock module list
 * (e.g. LockStar, QuickStar, Battery Guardian...).
 *
 * Field values (package names, explicit launcher activities, and CDN icon
 * URLs) were recovered from the original Pure Lock APK's decompiled logic,
 * so opening a module and showing its real branded icon works the same way
 * the original app did.
 */
data class Module(
    val id: String,
    val name: String,
    val group: ModuleGroup,
    /** Package name of the module, used to check install state / launch it. */
    val packageName: String,
    /**
     * Explicit Activity class to launch for this module, when the module's
     * normal launcher intent doesn't lead to its settings screen (this is
     * only needed for a handful of modules — LockStar, Routines+, ClockFace
     * in the original app). Null means "use the standard launch intent, or
     * fall back to the package's first exported activity".
     */
    val launcherClassOverride: String? = null,
    /**
     * Real branded icon, hosted on Samsung's own Galaxy Store CDN, used as
     * the icon when the module isn't installed (so it still gets its actual
     * icon instead of a generic placeholder). When the module IS installed,
     * the app loads its real icon straight from the installed package
     * instead of this URL.
     */
    val iconUrl: String? = null,
    /** Fallback tile color, used only if [iconUrl] is null/fails to load and the module isn't installed. */
    val fallbackColor: Long = 0xFF9E9E9EL,
)

/**
 * The 4 real sections used by the original app's module list, confirmed
 * directly from screenshots of the live app (the APK/server data itself
 * never names them anywhere in code — see [GroupOrder]).
 */
enum class ModuleGroup(val displayName: String) {
    MAKE_UP("Make up"),
    LIFE_UP("Life up"),
    GUARDIANS("Guardians"),
    DEPENDENCIES("Dependencies"),
}

/**
 * Display order of [ModuleGroup] sections ("Nhóm" setting, key `AL` in the
 * original app).
 *
 * Confirmed from screenshots of the real app: only "Make up" and "Life up"
 * ever swap places — "Guardians" and "Dependencies" always stay put as the
 * 3rd and 4th sections. The original preference actually had a 3rd choice
 * ("Unit, Guardians, Family") that loaded a completely different, legacy
 * module set from Fine Lock's server (`mv=0` vs `mv=1"); that data was
 * never in the app itself and isn't reproducible here, so only the two
 * real Make up / Life up orderings are offered.
 */
enum class GroupOrder(val order: List<ModuleGroup>) {
    LIFE_UP_FIRST(listOf(ModuleGroup.LIFE_UP, ModuleGroup.MAKE_UP, ModuleGroup.GUARDIANS, ModuleGroup.DEPENDENCIES)),
    MAKE_UP_FIRST(listOf(ModuleGroup.MAKE_UP, ModuleGroup.LIFE_UP, ModuleGroup.GUARDIANS, ModuleGroup.DEPENDENCIES)),
}
