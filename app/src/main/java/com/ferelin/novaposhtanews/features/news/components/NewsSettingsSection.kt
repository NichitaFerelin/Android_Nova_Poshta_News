package com.ferelin.novaposhtanews.features.news.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ferelin.novaposhtanews.R
import com.ferelin.novaposhtanews.common.composeui.components.AppOptionButton
import com.ferelin.novaposhtanews.common.composeui.components.CountryIcon

@Composable
fun NewsSettingsSection(
    modifier: Modifier = Modifier,
    mdNewsEnabled: Boolean,
    uaNewsEnabled: Boolean,
    autoLoadEnabled: Boolean,
    sortByCategoryEnabled: Boolean,
    ascendingTimestampOrderEnabled: Boolean,
    onMdNewsCheckedChange: (Boolean) -> Unit,
    onUaNewsCheckedChange: (Boolean) -> Unit,
    onAutoLoadCheckedChange: (Boolean) -> Unit,
    onSortByCategoryClick: () -> Unit,
    onAscendingTimestampOrderClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(id = R.dimen.padding_horizontal_base),
                vertical = dimensionResource(id = R.dimen.padding_horizontal_settings_content),
            )
            .background(color = MaterialTheme.colorScheme.background),
    ) {
        NewsSwitchOption(
            title = stringResource(id = R.string.title_news_md),
            description = stringResource(id = R.string.hint_news_md_option),
            checked = mdNewsEnabled,
            iconSlot = { CountryIcon(iconId = R.drawable.ic_moldova) },
            onCheckedChange = onMdNewsCheckedChange,
        )
        Spacer(
            modifier = Modifier.height(
                dimensionResource(id = R.dimen.padding_vertical_base_items),
            ),
        )
        NewsSwitchOption(
            title = stringResource(id = R.string.title_news_ua),
            description = stringResource(id = R.string.hint_news_ua_option),
            checked = uaNewsEnabled,
            iconSlot = { CountryIcon(iconId = R.drawable.ic_ukraine) },
            onCheckedChange = onUaNewsCheckedChange,
        )
        Spacer(
            modifier = Modifier.height(
                dimensionResource(id = R.dimen.padding_vertical_base_items),
            ),
        )
        NewsSwitchOption(
            title = stringResource(id = R.string.title_auto_load),
            description = stringResource(id = R.string.hint_auto_load_option),
            checked = autoLoadEnabled,
            iconSlot = {
                Icon(
                    modifier = Modifier.size(dimensionResource(id = R.dimen.country_icon_default)),
                    painter = painterResource(id = R.drawable.ic_restart),
                    contentDescription = stringResource(id = R.string.hint_auto_load_option),
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            },
            onCheckedChange = onAutoLoadCheckedChange,
        )
        Spacer(
            modifier = Modifier.height(
                dimensionResource(id = R.dimen.padding_vertical_base_items),
            ),
        )
        AppOptionButton(
            title = stringResource(
                id = if (sortByCategoryEnabled) {
                    R.string.hint_sort_by_category
                } else {
                    R.string.hint_sort_by_date
                },
            ),
            contentDescription = stringResource(id = R.string.description_sort_type),
            iconId = R.drawable.ic_calendar,
            onClick = onSortByCategoryClick,
        )
        Spacer(
            modifier = Modifier.height(
                dimensionResource(id = R.dimen.padding_vertical_base_items),
            ),
        )
        AppOptionButton(
            title = stringResource(
                id = if (ascendingTimestampOrderEnabled) {
                    R.string.hint_new_first
                } else {
                    R.string.hint_old_first
                },
            ),
            contentDescription = stringResource(id = R.string.description_order_type),
            iconId = R.drawable.ic_swap,
            onClick = onAscendingTimestampOrderClick,
        )
    }
}
