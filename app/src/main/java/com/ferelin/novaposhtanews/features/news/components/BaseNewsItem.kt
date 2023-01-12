package com.ferelin.novaposhtanews.features.news.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import com.ferelin.novaposhtanews.R

private const val NEWS_ITEM_URL_MAX_LINES = 1
private const val NEWS_ITEM_URL_WEIGHT = 0.5f
private const val NEWS_ITEM_DATE_WEIGHT = 0.5f

@Composable
fun BaseNewsItem(
    modifier: Modifier = Modifier,
    title: String,
    date: String,
    url: String,
    onClick: () -> Unit,
    @DrawableRes countryIconId: Int,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.medium,
            )
            .padding(horizontal = dimensionResource(id = R.dimen.padding_horizontal_base)),
    ) {
        Image(
            modifier = Modifier
                .padding(vertical = dimensionResource(id = R.dimen.news_item_image_padding_vertical))
                .size(dimensionResource(id = R.dimen.news_item_image_size))
                .clip(MaterialTheme.shapes.medium),
            painter = painterResource(id = countryIconId),
            contentDescription = stringResource(id = R.string.description_country_icon),
            contentScale = ContentScale.Crop,
        )
        Spacer(
            modifier = Modifier.width(
                dimensionResource(id = R.dimen.padding_horizontal_base_items),
            ),
        )
        NewsContentSection(
            title = title,
            date = date,
            url = url,
            onClick = onClick,
        )
    }
}

@Composable
private fun NewsContentSection(
    modifier: Modifier = Modifier,
    title: String,
    date: String,
    url: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(id = R.dimen.padding_vertical_base_items)),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )
        Spacer(
            modifier = Modifier.height(
                dimensionResource(id = R.dimen.padding_vertical_base_items),
            ),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            ClickableText(
                modifier = Modifier.weight(NEWS_ITEM_URL_WEIGHT),
                text = buildAnnotatedString {
                    withStyle(
                        MaterialTheme.typography.labelMedium.toSpanStyle().copy(
                            color = MaterialTheme.colorScheme.outline
                        ),
                    ) {
                        append(url)
                    }
                },
                maxLines = NEWS_ITEM_URL_MAX_LINES,
                overflow = TextOverflow.Ellipsis,
                onClick = { onClick() },
            )
            Text(
                modifier = Modifier.weight(NEWS_ITEM_DATE_WEIGHT),
                textAlign = TextAlign.End,
                text = date,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}
