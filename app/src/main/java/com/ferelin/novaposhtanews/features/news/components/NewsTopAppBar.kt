package com.ferelin.novaposhtanews.features.news.components

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ferelin.novaposhtanews.R
import com.ferelin.novaposhtanews.common.composeui.components.AppTopAppBar

@Composable
fun NewsTopAppBar(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit,
) {
    Surface(shadowElevation = dimensionResource(id = R.dimen.elevation_top_bar)) {
        AppTopAppBar(
            modifier = modifier,
            title = {
                Image(
                    painter = painterResource(id = R.drawable.logo_nova_poshta),
                    contentDescription = stringResource(id = R.string.description_nova_poshta_logo),
                )
            },
            actions = {
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = stringResource(id = R.string.description_settings),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            },
        )
    }
}
