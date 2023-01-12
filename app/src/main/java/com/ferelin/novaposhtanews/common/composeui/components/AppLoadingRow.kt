package com.ferelin.novaposhtanews.common.composeui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ferelin.novaposhtanews.R
import com.ferelin.novaposhtanews.features.news.uistate.NewsLceState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppLoadingRow(
    modifier: Modifier = Modifier,
    lceState: NewsLceState,
    iconSlot: @Composable RowScope.() -> Unit,
) {
    IconStateRow(
        modifier = modifier,
        iconSlot = iconSlot,
        textSlot = {
            AnimatedContent(targetState = lceState) {
                when (it) {
                    is NewsLceState.Loading -> {
                        AppCircularProgressIndicator()
                    }
                    is NewsLceState.Content -> {
                        Text(
                            text = stringResource(id = R.string.title_news_loaded),
                            color = MaterialTheme.colorScheme.tertiary,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                    is NewsLceState.Error -> {
                        Text(
                            text = stringResource(id = R.string.error_news_load),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                    else -> Unit
                }
            }
        },
    )
}
