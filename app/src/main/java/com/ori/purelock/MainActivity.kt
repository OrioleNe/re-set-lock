package com.ori.purelock

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ori.purelock.data.AppThemeMode
import com.ori.purelock.data.SettingsRepository
import com.ori.purelock.logic.ModuleLauncher
import com.ori.purelock.ui.screens.ModulesScreen
import com.ori.purelock.ui.screens.SettingsScreen
import com.ori.purelock.ui.theme.AppTheme
import com.ori.purelock.util.LocaleHelper

// Standard Android motion durations (com.android.internal.R "config_shortAnimTime"),
// used below to match the platform's own push/pop feel instead of Navigation
// Compose's newer built-in default transition.
private const val TRANSITION_DURATION_MS = 300

private object Routes {
    const val MODULES = "modules"
    const val SETTINGS = "settings"
}

class MainActivity : ComponentActivity() {

    // Applies the saved "Ngôn ngữ" setting before any resources are resolved,
    // so the whole activity (including its title in the recents/app switcher)
    // comes up in the right language from the very first frame.
    override fun attachBaseContext(newBase: Context) {
        val language = SettingsRepository.getInstance(newBase).language.value
        super.attachBaseContext(LocaleHelper.wrap(newBase, language))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Must be called before super.onCreate(). Releasing the OS icon
        // splash with an unconditional `true` (not tied to any data load)
        // means it disappears the instant the first frame is ready to
        // draw — the earliest point Android itself allows. Our own
        // ModulesSkeleton (in ModulesScreen) takes over from there for the
        // real "modules ready" wait, so the skeleton is what actually
        // covers the transition, not the OS splash.
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { false }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PureLockApp()
        }
    }
}

@Composable
fun PureLockApp() {
    val context = LocalContext.current
    val settings = remember(context) { SettingsRepository.getInstance(context) }
    val themeMode by settings.themeMode.collectAsState()
    val useCustomFont by settings.useCustomFont.collectAsState()
    val darkTheme = when (themeMode) {
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
        // Material You itself doesn't dictate light/dark — it follows the
        // device's own current setting, same as the original app's option.
        AppThemeMode.MATERIAL_YOU -> isSystemInDarkTheme()
    }

    AppTheme(
        darkTheme = darkTheme,
        dynamicColor = themeMode == AppThemeMode.MATERIAL_YOU,
        useCustomFont = useCustomFont,
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            val navController = rememberNavController()

            // Explicit transitions here override Navigation Compose 2.8+'s
            // own built-in default (a scale/fade crossfade) with the
            // familiar native Android push: the new screen slides in from
            // the right over the old one, which dims and shifts slightly
            // left underneath it; reversed on back — including via the
            // system predictive-back gesture preview, since it's enabled
            // in the manifest (android:enableOnBackInvokedCallback).
            NavHost(navController = navController, startDestination = Routes.MODULES) {
                composable(Routes.MODULES) {
                    ModulesScreen(
                        onModuleClick = { module -> ModuleLauncher.openModule(context, module) },
                        onSettingsClick = { navController.navigate(Routes.SETTINGS) },
                    )
                }
                composable(
                    Routes.SETTINGS,
                    enterTransition = {
                        slideInHorizontally(
                            animationSpec = tween(TRANSITION_DURATION_MS),
                            initialOffsetX = { fullWidth -> fullWidth },
                        ) + fadeIn(tween(TRANSITION_DURATION_MS))
                    },
                    exitTransition = {
                        slideOutHorizontally(
                            animationSpec = tween(TRANSITION_DURATION_MS),
                            targetOffsetX = { fullWidth -> -fullWidth / 4 },
                        ) + fadeOut(tween(TRANSITION_DURATION_MS), targetAlpha = 0.5f)
                    },
                    popEnterTransition = {
                        slideInHorizontally(
                            animationSpec = tween(TRANSITION_DURATION_MS),
                            initialOffsetX = { fullWidth -> -fullWidth / 4 },
                        ) + fadeIn(tween(TRANSITION_DURATION_MS), initialAlpha = 0.5f)
                    },
                    popExitTransition = {
                        slideOutHorizontally(
                            animationSpec = tween(TRANSITION_DURATION_MS),
                            targetOffsetX = { fullWidth -> fullWidth },
                        ) + fadeOut(tween(TRANSITION_DURATION_MS))
                    },
                ) {
                    SettingsScreen(
                        onBackClick = { navController.popBackStack() },
                    )
                }
            }
        }
    }
}
