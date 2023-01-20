package com.ferelin.novaposhtanews.common.composeui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.ferelin.novaposhtanews.R

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = dimensionResource(id = R.dimen.elevation_base),
            pressedElevation = dimensionResource(id = R.dimen.elevation_pressed),
            disabledElevation = dimensionResource(id = R.dimen.elevation_disabled),
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onBackground,
        ),
        content = content,
    )
}
