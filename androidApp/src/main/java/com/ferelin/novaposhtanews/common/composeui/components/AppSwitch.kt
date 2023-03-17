package com.ferelin.novaposhtanews.common.composeui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppSwitch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Switch(
        modifier = modifier,
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colorScheme.outline,
            checkedTrackColor = MaterialTheme.colorScheme.surfaceTint,
            checkedBorderColor = MaterialTheme.colorScheme.outline,
            checkedIconColor = MaterialTheme.colorScheme.outline,
            uncheckedThumbColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
            uncheckedTrackColor = MaterialTheme.colorScheme.primaryContainer,
            uncheckedBorderColor = MaterialTheme.colorScheme.secondary,
            uncheckedIconColor = MaterialTheme.colorScheme.onPrimary,
        ),
    )
}
