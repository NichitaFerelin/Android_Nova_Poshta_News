package com.ferelin.novaposhtanews.common.composeui.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import com.ferelin.novaposhtanews.R

@Composable
fun AppOptionButton(
    modifier: Modifier = Modifier,
    title: String,
    contentDescription: String,
    @DrawableRes iconId: Int,
    onClick: () -> Unit,
) {
    AppButton(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = dimensionResource(id = R.dimen.button_min_height)),
        onClick = onClick,
    ) {
        Crossfade(targetState = title) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.size(dimensionResource(id = R.dimen.country_icon_default)),
                    painter = painterResource(id = iconId),
                    contentDescription = contentDescription,
                )
                Spacer(
                    modifier = Modifier.width(
                        dimensionResource(id = R.dimen.padding_horizontal_base),
                    ),
                )
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}
