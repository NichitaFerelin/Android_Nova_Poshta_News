@file:OptIn(ExperimentalAnimationApi::class)

package com.ferelin.novaposhtanews.features.news.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ferelin.novaposhtanews.R
import com.ferelin.novaposhtanews.common.composeui.components.AppButton
import com.ferelin.novaposhtanews.common.composeui.components.AppLoadingRow
import com.ferelin.novaposhtanews.common.composeui.components.CountryIcon
import com.ferelin.novaposhtanews.features.news.uistate.NewsLceState

@Composable
fun NewsFirstFetchLoadingSection(
    modifier: Modifier = Modifier,
    isUaNewsEnabled: Boolean,
    uaNewsLceState: NewsLceState,
    isMdNewsEnabled: Boolean,
    mdNewsLceState: NewsLceState,
    onRepeatClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.padding_horizontal_base)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(id = R.dimen.padding_vertical_base_items),
        ),
    ) {
        val showTogether = shouldShowTogether(
            uaNewsLceState = uaNewsLceState,
            mdNewsLceState = mdNewsLceState,
            isUaNewsEnabled = isUaNewsEnabled,
            isMdNewsEnabled = isMdNewsEnabled,
        )
        AnimatedContent(targetState = showTogether) { together ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                if (together) {
                    NewsDoubleIconLoadingRow(newsLceState = mdNewsLceState)
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(
                            dimensionResource(id = R.dimen.padding_vertical_base_items),
                        ),
                    ) {
                        AnimatedVisibility(
                            visible = isMdNewsEnabled,
                            exit = fadeOut(),
                            enter = fadeIn(),
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center,
                            ) {
                                AppLoadingRow(
                                    lceState = mdNewsLceState,
                                    iconSlot = { CountryIcon(iconId = R.drawable.ic_moldova) },
                                )
                            }
                        }
                        AnimatedVisibility(
                            visible = isUaNewsEnabled,
                            exit = fadeOut(),
                            enter = fadeIn(),
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center,
                            ) {
                                AppLoadingRow(
                                    lceState = uaNewsLceState,
                                    iconSlot = { CountryIcon(iconId = R.drawable.ic_ukraine) },
                                )
                            }
                        }
                    }
                }
            }
        }

        val showErrorSection = shouldShowErrorSection(
            uaNewsLceState = uaNewsLceState,
            mdNewsLceState = mdNewsLceState,
            isUaNewsEnabled = isUaNewsEnabled,
            isMdNewsEnabled = isMdNewsEnabled,
        )
        AnimatedVisibility(visible = showErrorSection) {
            AppButton(onClick = onRepeatClick) {
                Text(
                    text = stringResource(id = R.string.hint_repeat_load),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

@Stable
private fun shouldShowTogether(
    uaNewsLceState: NewsLceState,
    mdNewsLceState: NewsLceState,
    isUaNewsEnabled: Boolean,
    isMdNewsEnabled: Boolean,
) = uaNewsLceState == mdNewsLceState && isUaNewsEnabled && isMdNewsEnabled

@Stable
private fun shouldShowErrorSection(
    uaNewsLceState: NewsLceState,
    mdNewsLceState: NewsLceState,
    isUaNewsEnabled: Boolean,
    isMdNewsEnabled: Boolean,
) = (uaNewsLceState is NewsLceState.Error && isUaNewsEnabled) ||
    (mdNewsLceState is NewsLceState.Error && isMdNewsEnabled)
