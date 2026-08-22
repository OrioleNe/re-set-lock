package com.ori.purelock.data

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

/** Real installed icon for a module, or null if it isn't installed. */
data class ResolvedModuleIcon(val installedIcon: Bitmap?)

/**
 * Checks install state — and grabs the real installed icon — for every
 * module at once, in parallel via [PackageManager]. This is the actual
 * "load the modules/tiện ích" work: [ModulesScreen][com.ori.purelock.ui.screens.ModulesScreen]'s
 * loading indicator now waits on this instead of a fixed delay, so it stays
 * up exactly as long as these checks take and no longer.
 */
suspend fun resolveModuleIcons(
    context: Context,
    modules: List<Module>,
): Map<String, ResolvedModuleIcon> = coroutineScope {
    modules
        .map { module ->
            async {
                val icon = try {
                    val drawable = context.packageManager.getApplicationIcon(module.packageName)
                    drawableToBitmap(drawable)
                } catch (e: PackageManager.NameNotFoundException) {
                    null
                }
                module.packageName to ResolvedModuleIcon(icon)
            }
        }
        .awaitAll()
        .toMap()
}

private fun drawableToBitmap(drawable: Drawable): Bitmap {
    val width = drawable.intrinsicWidth.coerceAtLeast(1)
    val height = drawable.intrinsicHeight.coerceAtLeast(1)
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}
