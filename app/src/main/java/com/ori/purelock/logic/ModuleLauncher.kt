package com.ori.purelock.logic

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.ori.purelock.data.Module

/**
 * Recreates the original Pure Lock app's "open a module" behavior:
 *
 * 1. If the module is installed, try its normal launcher intent first
 *    (works for most modules). Nice Shot is special-cased to skip this,
 *    matching the original app (its standard launch intent doesn't lead
 *    anywhere useful).
 * 2. If that's not available, build an explicit intent using the module's
 *    known settings Activity ([Module.launcherClassOverride]) if it has one
 *    (LockStar, Routines+, ClockFace).
 * 3. If it still doesn't have one, fall back to the package's first
 *    exported activity (best-effort, same heuristic the original app used).
 * 4. If the module isn't installed at all (or nothing above works), send
 *    the user to install it: Galaxy Store first, falling back to the Play
 *    Store, falling back to a browser link.
 */
object ModuleLauncher {

    fun isInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getApplicationInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    /** Opens the module if installed, otherwise sends the user to install it. */
    fun openModule(context: Context, module: Module) {
        val intent = buildLaunchIntent(context, module)
        if (intent != null) {
            try {
                context.startActivity(intent)
                return
            } catch (e: ActivityNotFoundException) {
                // Fall through to store redirect below.
            }
        }
        openInStore(context, module.packageName)
    }

    private fun buildLaunchIntent(context: Context, module: Module): Intent? {
        val packageManager = context.packageManager

        // Standard launcher intent, unless this is Nice Shot (matches the
        // original app, which deliberately skips it for that one module).
        if (module.packageName != "com.samsung.android.app.captureplugin") {
            packageManager.getLaunchIntentForPackage(module.packageName)?.let { return it }
        }

        // Explicit settings Activity, when the module needs one.
        module.launcherClassOverride?.let { className ->
            val intent = Intent().setComponent(ComponentName(module.packageName, className))
            if (isInstalled(context, module.packageName)) return intent
        }

        // Last resort: the package's first exported activity.
        if (isInstalled(context, module.packageName)) {
            return try {
                val activities = packageManager.getPackageInfo(
                    module.packageName,
                    PackageManager.GET_ACTIVITIES,
                ).activities
                activities?.firstOrNull { it.exported }?.let { activityInfo ->
                    Intent().setComponent(ComponentName(activityInfo.packageName, activityInfo.name))
                }
            } catch (e: PackageManager.NameNotFoundException) {
                null
            }
        }

        return null
    }

    /** Galaxy Store first, then Play Store, then a plain browser link. */
    private fun openInStore(context: Context, packageName: String) {
        val galaxyStoreIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("samsungapps://ProductDetail/$packageName"),
        )
        try {
            context.startActivity(galaxyStoreIntent)
            return
        } catch (e: ActivityNotFoundException) {
            // Galaxy Store not installed — try Play Store next.
        }

        val playStoreIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("market://details?id=$packageName"),
        )
        try {
            context.startActivity(playStoreIntent)
            return
        } catch (e: ActivityNotFoundException) {
            // No app store app available — fall back to the browser.
        }

        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=$packageName"),
        )
        context.startActivity(browserIntent)
    }
}
