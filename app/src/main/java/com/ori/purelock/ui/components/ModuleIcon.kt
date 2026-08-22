package com.ori.purelock.ui.components

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ori.purelock.data.Module
import com.ori.purelock.data.ResolvedModuleIcon

/**
 * Shows a module's real icon: the actual installed app's icon if the module
 * is installed, otherwise its real branded icon loaded from Samsung's own
 * Galaxy Store CDN ([Module.iconUrl]). Falls back to a plain colored tile
 * only if neither of those is available.
 *
 * @param resolved The install check already done up front for this module
 * (see [com.ori.purelock.data.resolveModuleIcons], run once by
 * `ModulesScreen` before its loading indicator is dismissed). When
 * provided, no per-item PackageManager check happens here — this composable
 * just renders the result. Left null only as a standalone fallback (e.g.
 * previews) where it does its own one-off check.
 */
@Composable
fun ModuleIcon(
    module: Module,
    modifier: Modifier = Modifier,
    sizeDp: Int = 44,
    resolved: ResolvedModuleIcon? = null,
) {
    val context = LocalContext.current
    var selfCheckedIcon by remember(module.packageName) { mutableStateOf<Bitmap?>(null) }
    var selfChecked by remember(module.packageName) { mutableStateOf(false) }

    LaunchedEffect(module.packageName, resolved) {
        if (resolved != null) return@LaunchedEffect
        selfCheckedIcon = try {
            val drawable = context.packageManager.getApplicationIcon(module.packageName)
            drawableToBitmap(drawable)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
        selfChecked = true
    }

    val installedIcon = resolved?.installedIcon ?: selfCheckedIcon
    val checkedInstalled = resolved != null || selfChecked

    val shape = RoundedCornerShape(14.dp)

    when {
        installedIcon != null -> {
            Image(
                bitmap = installedIcon!!.asImageBitmap(),
                contentDescription = null,
                modifier = modifier.size(sizeDp.dp),
            )
        }
        checkedInstalled && module.iconUrl != null -> {
            AsyncImage(
                model = module.iconUrl,
                contentDescription = null,
                modifier = modifier.size(sizeDp.dp).background(androidx.compose.ui.graphics.Color(module.fallbackColor), shape),
            )
        }
        else -> {
            // Still checking install state, or no icon available at all.
            Box(
                modifier = modifier
                    .size(sizeDp.dp)
                    .background(androidx.compose.ui.graphics.Color(module.fallbackColor), shape),
            )
        }
    }
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
