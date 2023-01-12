package com.ferelin.novaposhtanews.common.composeui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.ferelin.novaposhtanews.R

@Composable
fun IconStateRow(
    modifier: Modifier = Modifier,
    iconSlot: @Composable RowScope.() -> Unit,
    textSlot: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        iconSlot()
        Spacer(
            modifier = Modifier.width(
                dimensionResource(id = R.dimen.padding_horizontal_base_items),
            ),
        )
        textSlot()
    }
}
