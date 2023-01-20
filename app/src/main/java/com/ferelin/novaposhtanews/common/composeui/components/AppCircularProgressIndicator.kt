package com.ferelin.novaposhtanews.common.composeui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.ferelin.novaposhtanews.R

@Composable
fun AppCircularProgressIndicator(
    modifier: Modifier = Modifier,
) {
    CircularProgressIndicator(
        modifier = modifier.size(dimensionResource(id = R.dimen.circular_progress_bar_size)),
        color = MaterialTheme.colorScheme.surface,
        strokeWidth = dimensionResource(id = R.dimen.circular_progress_bar_stroke_width),
    )
}
