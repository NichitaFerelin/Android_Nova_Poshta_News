package com.ferelin.novaposhtanews.features.news.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.ferelin.novaposhtanews.R
import com.ferelin.novaposhtanews.common.composeui.components.AppSwitchOption

@Composable
fun NewsSwitchOption(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    checked: Boolean,
    iconSlot: @Composable () -> Unit,
    onCheckedChange: (Boolean) -> Unit,
) {
    AppSwitchOption(
        modifier = modifier,
        checked = checked,
        onCheckedChange = onCheckedChange,
        content = {
            iconSlot()
            Spacer(
                modifier = Modifier.width(
                    dimensionResource(id = R.dimen.padding_horizontal_base),
                ),
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        },
    )
}
