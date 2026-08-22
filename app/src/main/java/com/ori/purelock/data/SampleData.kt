package com.ori.purelock.data

/**
 * Module catalog matching the original Pure Lock app, with real package
 * names, explicit launcher activities, and real branded icon URLs (hosted
 * on Samsung's own Galaxy Store CDN) recovered from the original app's
 * decompiled code.
 *
 * The UI grouping (Make up / Life up / Guardians / Dependencies) matches
 * screenshots of the live original app — the taxonomy itself is never
 * present in the APK, since the module list and its section names are
 * fetched live from Fine Lock's own server at runtime.
 */
val SampleModules: List<Module> = listOf(
    // Unit
    Module(
        id = "lockstar", name = "LockStar", group = ModuleGroup.MAKE_UP,
        packageName = "com.samsung.systemui.lockstar",
        // Original app picks one of these two depending on Android version.
        launcherClassOverride = "com.samsung.systemui.lockstar.presentation.settings.launch.LockStarLaunchActivity",
        iconUrl = "https://img.samsungapps.com/productNew/000003002072/IconImage_20210126070105138_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFF3D5AFE,
    ),
    Module(
        id = "quickstar", name = "QuickStar", group = ModuleGroup.MAKE_UP,
        packageName = "com.samsung.android.qstuner",
        iconUrl = "https://img.samsungapps.com/productNew/000003084407/IconImage_20210202053623653_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFF00BFA5,
    ),
    Module(
        id = "routinesplus", name = "Routines +", group = ModuleGroup.LIFE_UP,
        packageName = "com.samsung.android.app.routineplus",
        launcherClassOverride = "com.samsung.android.app.routineplus.main.RoutinePlusActivity",
        iconUrl = "https://img.samsungapps.com/productNew/000005563964/IconImage_20210430085320241_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFF7C4DFF,
    ),
    Module(
        id = "clockface", name = "ClockFace", group = ModuleGroup.MAKE_UP,
        packageName = "com.samsung.android.app.clockface",
        launcherClassOverride = "com.samsung.android.app.clockface.setting.ClockFaceSetting",
        iconUrl = "https://img.samsungapps.com/productNew/000003202866/IconImage_20210129030402069_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFF2979FF,
    ),
    Module(
        id = "multistar", name = "MultiStar", group = ModuleGroup.LIFE_UP,
        packageName = "com.samsung.android.multistar",
        iconUrl = "https://img.samsungapps.com/productNew/000003266196/IconImage_20210129072250320_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFF00C853,
    ),
    Module(
        id = "navstar", name = "NavStar", group = ModuleGroup.MAKE_UP,
        packageName = "com.samsung.systemui.navillera",
        iconUrl = "https://img.samsungapps.com/productNew/000003488383/IconImage_20210120235414304_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFF00B0FF,
    ),
    Module(
        id = "niceshot", name = "Nice Shot", group = ModuleGroup.LIFE_UP,
        packageName = "com.samsung.android.app.captureplugin",
        iconUrl = "https://img.samsungapps.com/productNew/000003926817/IconImage_20220228075238459_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFF1DE9B6,
    ),
    Module(
        id = "homeup", name = "Home Up", group = ModuleGroup.MAKE_UP,
        packageName = "com.samsung.android.app.homestar",
        iconUrl = "https://img.samsungapps.com/productNew/000004772683/IconImage_20210129072330360_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFF448AFF,
    ),
    Module(
        id = "notistar", name = "NotiStar", group = ModuleGroup.LIFE_UP,
        packageName = "com.samsung.systemui.notilus",
        iconUrl = "https://img.samsungapps.com/productNew/000004085658/IconImage_20210125031042698_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFFFF5252,
    ),
    Module(
        id = "registar", name = "RegiStar", group = ModuleGroup.LIFE_UP,
        packageName = "com.samsung.android.app.galaxyregistry",
        iconUrl = "https://img.samsungapps.com/productNew/000006559401/IconImage_20221107081612803_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFFFFAB00,
    ),

    // Guardians
    Module(
        id = "appbooster", name = "Galaxy App Booster", group = ModuleGroup.GUARDIANS,
        packageName = "com.samsung.android.appbooster",
        iconUrl = "https://img.samsungapps.com/productNew/000004665772/IconImage_20201016032524113_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFFFFC400,
    ),
    Module(
        id = "batteryguardian", name = "Battery Guardian", group = ModuleGroup.GUARDIANS,
        packageName = "com.samsung.android.statsd",
        iconUrl = "https://img.samsungapps.com/productNew/000004665757/IconImage_20201016032606218_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFF7C4DFF,
    ),
    Module(
        id = "batterytracker", name = "Battery Tracker", group = ModuleGroup.GUARDIANS,
        packageName = "com.android.samsung.batteryusage",
        iconUrl = "https://img.samsungapps.com/productNew/000004665776/IconImage_20201016032311624_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFF1DE9B6,
    ),
    Module(
        id = "mediafileguardian", name = "Media File Guardian", group = ModuleGroup.GUARDIANS,
        packageName = "com.samsung.android.mediaguardian",
        iconUrl = "https://img.samsungapps.com/productNew/000004665770/IconImage_20201016032400883_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFF00C853,
    ),
    Module(
        id = "thermalguardian", name = "Thermal Guardian", group = ModuleGroup.GUARDIANS,
        packageName = "com.samsung.android.thermalguardian",
        iconUrl = "https://img.samsungapps.com/productNew/000005422898/IconImage_20210218070822552_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFFFF6E40,
    ),
    Module(
        id = "memoryguardian", name = "Memory Guardian", group = ModuleGroup.GUARDIANS,
        packageName = "com.samsung.android.memoryguardian",
        iconUrl = "https://img.samsungapps.com/productNew/000005422901/IconImage_20210218070820977_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFF448AFF,
    ),
    Module(
        id = "goodguardians", name = "Good Guardians", group = ModuleGroup.DEPENDENCIES,
        packageName = "com.android.samsung.utilityapp",
        iconUrl = "https://img.samsungapps.com/productNew/000004665763/IconImage_20220217005924277_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFFB0BEC5,
    ),
    Module(
        id = "goodguardiansagent", name = "Good Guardians Agent", group = ModuleGroup.DEPENDENCIES,
        packageName = "com.android.samsung.utilityagent",
        iconUrl = "https://img.samsungapps.com/productNew/000004665763/IconImage_20220217005924277_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFFB0BEC5,
    ),

    // Family
    Module(
        id = "themepark", name = "Theme Park", group = ModuleGroup.MAKE_UP,
        packageName = "com.samsung.android.themedesigner",
        iconUrl = "https://img.samsungapps.com/productNew/000004623161/IconImage_20201015013845174_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFFFF7043,
    ),
    Module(
        id = "nicecatch", name = "Nice Catch", group = ModuleGroup.LIFE_UP,
        packageName = "com.samsung.android.app.goodcatch",
        iconUrl = "https://img.samsungapps.com/productNew/000003711875/IconImage_20210120080312661_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFF00BFA5,
    ),
    Module(
        id = "onehandop", name = "One Hand Operation +", group = ModuleGroup.LIFE_UP,
        packageName = "com.samsung.android.sidegesturepad",
        iconUrl = "https://img.samsungapps.com/productNew/000003036952/IconImage_20210120065453642_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFF26C6DA,
    ),
    Module(
        id = "edgelightingplus", name = "Edge lighting+", group = ModuleGroup.LIFE_UP,
        packageName = "com.samsung.android.edgelightingplus",
        iconUrl = "https://img.samsungapps.com/productNew/000007063417/IconImage_20230810093123452_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFF7C4DFF,
    ),
    Module(
        id = "edgetouch", name = "Edge Touch", group = ModuleGroup.LIFE_UP,
        packageName = "com.samsung.android.app.edgetouch",
        iconUrl = "https://img.samsungapps.com/productNew/000003084395/IconImage_20210202053450084_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFF00C853,
    ),
    Module(
        id = "soundassistant", name = "Sound Assistant", group = ModuleGroup.LIFE_UP,
        packageName = "com.samsung.android.soundassistant",
        iconUrl = "https://img.samsungapps.com/productNew/000003206291/IconImage_20210218030909753_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFF7E57C2,
    ),
    Module(
        id = "pentastic", name = "Pentastic", group = ModuleGroup.MAKE_UP,
        packageName = "com.samsung.android.pentastic",
        iconUrl = "https://img.samsungapps.com/productNew/000005198690/IconImage_20201009130957263_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFF9575CD,
    ),
    Module(
        id = "wonderland", name = "Wonderland", group = ModuleGroup.MAKE_UP,
        packageName = "com.samsung.android.wonderland.wallpaper",
        iconUrl = "https://img.samsungapps.com/productNew/000005212031/IconImage_20210121043147258_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFF5C6BC0,
    ),
    Module(
        id = "keyscafe", name = "Keys Cafe", group = ModuleGroup.MAKE_UP,
        packageName = "com.samsung.android.keyscafe",
        iconUrl = "https://img.samsungapps.com/productNew/000005293390/IconImage_20210129080141877_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFF37474F,
    ),
    Module(
        id = "cameraassistant", name = "Camera Assistant", group = ModuleGroup.LIFE_UP,
        packageName = "com.samsung.android.app.cameraassistant",
        iconUrl = "https://img.samsungapps.com/productNew/000006540978/IconImage_20221024054518247_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFF26A69A,
    ),
    Module(
        id = "goodlock", name = "Good Lock", group = ModuleGroup.DEPENDENCIES,
        packageName = "com.samsung.android.goodlock",
        iconUrl = "https://img.samsungapps.com/productNew/000003080349/IconImage_20210121024443191_NEW_WAP_ICON_512_512.png",
        fallbackColor = 0xFFEC407A,
    ),
)
