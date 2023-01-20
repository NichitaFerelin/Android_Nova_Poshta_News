package com.ferelin.novaposhtanews.features.news.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ferelin.novaposhtanews.R
import com.ferelin.novaposhtanews.common.composeui.components.AppButton
import com.ferelin.novaposhtanews.common.composeui.components.AppCircularProgressIndicator
import com.ferelin.novaposhtanews.common.composeui.components.CountryIcon
import com.ferelin.novaposhtanews.features.news.uistate.NewsLceState

@Composable
fun NewsLoadMoreSection(
    modifier: Modifier = Modifier,
    isUaNewsEnabled: Boolean,
    uaButtonEnabled: Boolean,
    uaNewsLceState: NewsLceState,
    isMdNewsEnabled: Boolean,
    mdNewsLceState: NewsLceState,
    onLoadMoreUaClick: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.padding_vertical_base_items),
        ),
    ) {
        AnimatedVisibility(visible = isMdNewsEnabled && mdNewsLceState is NewsLceState.Content) {
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
                onClick = { /**/ },
            ) {
                CountryIcon(iconId = R.drawable.ic_moldova)
                Spacer(
                    modifier = Modifier.width(
                        dimensionResource(id = R.dimen.padding_horizontal_base),
                    ),
                )
                Text(
                    text = stringResource(id = R.string.hint_news_loaded),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
        AnimatedVisibility(visible = isUaNewsEnabled) {
            AppButton(
                modifier = Modifier.fillMaxWidth(),
                enabled = uaButtonEnabled,
                onClick = onLoadMoreUaClick,
            ) {
                CountryIcon(iconId = R.drawable.ic_ukraine)
                Spacer(
                    modifier = Modifier.width(
                        dimensionResource(id = R.dimen.padding_horizontal_base),
                    ),
                )
                Text(
                    text = stringResource(
                        id = if (uaButtonEnabled) {
                            R.string.hint_load_more
                        } else {
                            R.string.hint_news_loaded
                        },
                    ),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
        AnimatedVisibility(
            visible = uaNewsLceState is NewsLceState.Loading
                || uaNewsLceState is NewsLceState.Error,
        ) {
            Crossfade(targetState = uaNewsLceState) { lce ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    when (lce) {
                        is NewsLceState.Loading -> {
                            AppCircularProgressIndicator()
                        }
                        is NewsLceState.Error -> {
                            Text(
                                text = stringResource(id = R.string.error_news_page_load),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.headlineSmall,
                            )
                        }
                        else -> Unit
                    }
                }
            }
        }
    }
}
