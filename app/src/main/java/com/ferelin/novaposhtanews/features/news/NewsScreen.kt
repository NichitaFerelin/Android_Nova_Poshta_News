@file:OptIn(ExperimentalLifecycleComposeApi::class)

package com.ferelin.novaposhtanews.features.news

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ferelin.novaposhtanews.R
import com.ferelin.novaposhtanews.common.composeui.components.AppFloatingActionsButton
import com.ferelin.novaposhtanews.features.news.components.BaseNewsItem
import com.ferelin.novaposhtanews.features.news.components.NewsBottomSheet
import com.ferelin.novaposhtanews.features.news.components.NewsFirstFetchLoadingSection
import com.ferelin.novaposhtanews.features.news.components.NewsLoadMoreSection
import com.ferelin.novaposhtanews.features.news.components.NewsTopAppBar
import com.ferelin.novaposhtanews.features.news.uistate.NewsUiItem
import com.ferelin.novaposhtanews.features.news.uistate.NewsUiState
import com.ferelin.novaposhtanews.utils.openUrl
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

internal const val SCROLL_TO_TOP_TARGET_INDEX = 0

@Composable
fun NewsScreenRoute(
    viewModel: NewsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    NewsScreen(
        uiState = uiState,
        onSettingsClick = viewModel::onSettingsClick,
        onRepeatInitialLoadClick = viewModel::onRepeatInitialLoadNewsClick,
        onLoadMoreUaNewsClick = viewModel::onLoadMoreUaNewsClick,
        onMdNewsCheckedChange = viewModel::onMdNewsSwitchClick,
        onUaNewsCheckedChange = viewModel::onUaNewsSwitchClick,
        onAutoLoadCheckedChange = viewModel::onAutoLoadSwitchClick,
        onSortByCategoryClick = viewModel::onSortByCategoryClick,
        onAscendingTimestampOrderClick = viewModel::onSortByTimestampClick,
        onSettingsDialogDismiss = viewModel::onSettingsDialogDismiss,
        onFirstVisibleIndexChanged = viewModel::onFirstVisibleIndexChanged,
        onNewsItemClick = { context.openUrl(it.url) },
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
private fun NewsScreen(
    uiState: NewsUiState,
    onSettingsClick: () -> Unit,
    onRepeatInitialLoadClick: () -> Unit,
    onLoadMoreUaNewsClick: () -> Unit,
    onMdNewsCheckedChange: (Boolean) -> Unit,
    onUaNewsCheckedChange: (Boolean) -> Unit,
    onAutoLoadCheckedChange: (Boolean) -> Unit,
    onSortByCategoryClick: () -> Unit,
    onAscendingTimestampOrderClick: () -> Unit,
    onSettingsDialogDismiss: () -> Unit,
    onFirstVisibleIndexChanged: (Int) -> Unit,
    onNewsItemClick: (NewsUiItem) -> Unit,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .onEach(onFirstVisibleIndexChanged)
            .launchIn(this)
    }

    NewsBottomSheet(
        isSettingsDialogActive = uiState.isSettingsDialogActive,
        mdNewsEnabled = uiState.isMdNewsEnabled,
        uaNewsEnabled = uiState.isUaNewsEnabled,
        autoLoadEnabled = uiState.isAutoLoadEnabled,
        sortByCategoryEnabled = uiState.isSortByCategoryEnabled,
        ascendingTimestampOrderEnabled = uiState.isAscendingTimestampOrderEnabled,
        onMdNewsCheckedChange = onMdNewsCheckedChange,
        onUaNewsCheckedChange = onUaNewsCheckedChange,
        onAutoLoadCheckedChange = onAutoLoadCheckedChange,
        onSortByCategoryClick = onSortByCategoryClick,
        onAscendingTimestampOrderClick = onAscendingTimestampOrderClick,
        onSettingsDialogDismiss = onSettingsDialogDismiss,
    ) {
        Scaffold(
            topBar = { NewsTopAppBar(onSettingsClick = onSettingsClick) },
            floatingActionButton = {
                AnimatedVisibility(
                    visible = uiState.isFabButtonVisible,
                    enter = scaleIn(),
                    exit = scaleOut(),
                ) {
                    AppFloatingActionsButton(
                        onClick = {
                            coroutineScope.launch {
                                listState.animateScrollToItem(SCROLL_TO_TOP_TARGET_INDEX)
                            }
                        },
                    )
                }
            },
        ) { paddings ->
            LazyColumn(
                modifier = Modifier.padding(paddings),
                state = listState,
                contentPadding = PaddingValues(
                    top = dimensionResource(id = R.dimen.news_list_padding_top),
                    start = dimensionResource(id = R.dimen.news_list_padding_start),
                    end = dimensionResource(id = R.dimen.news_list_padding_end),
                    bottom = dimensionResource(id = R.dimen.news_list_padding_bottom),
                ),
                verticalArrangement = Arrangement.spacedBy(
                    dimensionResource(id = R.dimen.news_list_items_padding),
                ),
            ) {
                item {
                    NewsFirstFetchLoadingSection(
                        isUaNewsEnabled = uiState.isUaNewsEnabled,
                        uaNewsLceState = uiState.firstFetchNewsUaLce,
                        mdNewsLceState = uiState.firstFetchNewsMdLce,
                        isMdNewsEnabled = uiState.isMdNewsEnabled,
                        onRepeatClick = onRepeatInitialLoadClick,
                    )
                }
                items(
                    items = uiState.news,
                    contentType = { it::class.java },
                    key = { it.key },
                ) {
                    when (it) {
                        is NewsUiItem.Md -> {
                            BaseNewsItem(
                                title = it.title,
                                date = it.createdAt,
                                url = it.url,
                                onClick = { onNewsItemClick(it) },
                                countryIconId = R.drawable.ic_moldova,
                            )
                        }
                        is NewsUiItem.Ua -> {
                            BaseNewsItem(
                                title = it.title,
                                date = it.createdAt,
                                url = it.url,
                                onClick = { onNewsItemClick(it) },
                                countryIconId = R.drawable.ic_ukraine,
                            )
                        }
                    }
                }
                item {
                    NewsLoadMoreSection(
                        isUaNewsEnabled = uiState.isUaNewsEnabled,
                        uaButtonEnabled = !uiState.newsUaEndOfPageReached,
                        uaNewsLceState = uiState.newsUaLce,
                        isMdNewsEnabled = uiState.isMdNewsEnabled,
                        mdNewsLceState = uiState.firstFetchNewsMdLce,
                        onLoadMoreUaClick = onLoadMoreUaNewsClick,
                    )
                }
            }
        }
    }
}
