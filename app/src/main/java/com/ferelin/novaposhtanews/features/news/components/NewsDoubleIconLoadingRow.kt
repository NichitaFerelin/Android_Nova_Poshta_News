package com.ferelin.novaposhtanews.features.news.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.ferelin.novaposhtanews.R
import com.ferelin.novaposhtanews.common.composeui.components.AppLoadingRow
import com.ferelin.novaposhtanews.common.composeui.components.CountryIcon
import com.ferelin.novaposhtanews.features.news.uistate.NewsLceState

@Composable
fun NewsDoubleIconLoadingRow(
    modifier: Modifier = Modifier,
    newsLceState: NewsLceState,
) {
    AppLoadingRow(
        modifier = modifier,
        lceState = newsLceState,
        iconSlot = {
            CountryIcon(iconId = R.drawable.ic_moldova)
            Spacer(
                modifier = Modifier.width(
                    dimensionResource(id = R.dimen.padding_horizontal_base_items),
                ),
            )
            CountryIcon(iconId = R.drawable.ic_ukraine)
        },
    )
}
