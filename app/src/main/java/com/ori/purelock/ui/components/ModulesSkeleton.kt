package com.ori.purelock.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

/**
 * Skeleton screen shown for [com.ori.purelock.ui.screens.ModulesScreen]'s
 * first frame — replaces both the OS icon splash *and* the old full-screen
 * spinner. It's shaped like the real top bar + grouped module list (just
 * with shimmering gray placeholders instead of real text/icons), so the
 * app reads as "already there, just filling in" rather than "waiting".
 *
 * Row/section counts here are a fixed, generic approximation of the real
 * layout — not read from actual module data, since this is exactly what's
 * on screen *before* that data is available.
 */
@Composable
fun ModulesSkeleton(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        // Top bar placeholder: title on the left, action icon on the right.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .shimmerEffect()
            )
        }

        // Two group-shaped sections, each with a header bar and a card of
        // avatar+label rows — matching SectionHeader/SectionCard/ModuleRow.
        SkeletonGroup(rowCount = 3)
        SkeletonGroup(rowCount = 2)
    }
}

@Composable
private fun SkeletonGroup(rowCount: Int, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        // Section header placeholder ("Unit" / "Guardians" / ...).
        Box(
            modifier = Modifier
                .padding(start = 20.dp, top = 24.dp, end = 20.dp, bottom = 8.dp)
                .width(90.dp)
                .height(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmerEffect()
        )

        // Card placeholder holding rowCount module-row placeholders.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .background(MaterialTheme.colorScheme.surfaceContainer, MaterialTheme.shapes.large)
                .padding(vertical = 4.dp),
        ) {
            repeat(rowCount) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Matches ModuleIcon's real shape/size (RoundedCornerShape(14.dp), 44.dp).
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .shimmerEffect()
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .width(150.dp)
                            .height(16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                }
            }
        }
    }
}
