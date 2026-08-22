package com.ori.purelock.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Material 3 Expressive shape scale.
 *
 * Expressive pushes corner radii noticeably larger than classic M3,
 * especially at the extra-small/small end (list rows, chips) and the
 * extra-large end (sheets, big cards) — this is a big part of what gives
 * the system its softer, friendlier look compared to standard M3.
 */
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp),
)

/** Shape used for module/setting row icon containers. */
val IconContainerShape = RoundedCornerShape(14.dp)
