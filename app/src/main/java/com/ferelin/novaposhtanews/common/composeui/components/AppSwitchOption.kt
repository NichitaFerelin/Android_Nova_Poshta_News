package com.ferelin.novaposhtanews.common.composeui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.ferelin.novaposhtanews.R

@Composable
fun AppSwitchOption(
    modifier: Modifier = Modifier,
    checked: Boolean,
    content: @Composable RowScope.() -> Unit,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.padding_horizontal_base),
        ),
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
        AppSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}
