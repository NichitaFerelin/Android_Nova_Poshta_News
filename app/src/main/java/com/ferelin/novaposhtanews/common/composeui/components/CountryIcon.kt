package com.ferelin.novaposhtanews.common.composeui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ferelin.novaposhtanews.R

@Composable
fun CountryIcon(
    modifier: Modifier = Modifier,
    @DrawableRes iconId: Int,
) {
    Image(
        modifier = modifier
            .size(dimensionResource(id = R.dimen.country_icon_default))
            .clip(CircleShape),
        painter = painterResource(iconId),
        contentDescription = stringResource(id = R.string.description_country_icon),
        contentScale = ContentScale.Crop,
    )
}
