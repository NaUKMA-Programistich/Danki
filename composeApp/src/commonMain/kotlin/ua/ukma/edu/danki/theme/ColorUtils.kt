package ua.ukma.edu.danki.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.unit.dp

val mColors
    @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme

val ColorScheme.surfaceContainerLow
    @Composable @ReadOnlyComposable get() = mColors.surfaceColorAtElevation(1.dp)

val ColorScheme.surfaceContainer
    @Composable @ReadOnlyComposable get() = mColors.surfaceColorAtElevation(3.dp)

val ColorScheme.surfaceContainerHigh
    @Composable @ReadOnlyComposable get() = mColors.surfaceColorAtElevation(6.dp)

val ColorScheme.surfaceContainerHighest
    @Composable @ReadOnlyComposable get() = mColors.surfaceVariant
