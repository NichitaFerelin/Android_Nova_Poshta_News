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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ferelin.novaposhtanews.R
import com.ferelin.novaposhtanews.common.composeui.components.AppFloatingActionsButton
import com.ferelin.novaposhtanews.features.news.uicomponents.BaseNewsItem
import com.ferelin.novaposhtanews.features.news.uicomponents.NewsFirstFetchLoadingSection
import com.ferelin.novaposhtanews.features.news.uicomponents.NewsTopAppBar
import com.ferelin.novaposhtanews.utils.openUrl
import com.ferelin.shared.features.news.uistate.NewsLceState
import com.ferelin.shared.features.news.uistate.NewsMdUi
import com.ferelin.shared.features.news.uistate.NewsUiState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

internal const val SCROLL_TO_TOP_TARGET_INDEX = 0

@Composable
fun NewsScreenRoute(
    viewModel: NewsAndroidViewModel = koinViewModel(),
) {
    val uiState by viewModel.contractViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    NewsScreen(
        uiState = uiState,
        onRepeatInitialLoadClick = viewModel.contractViewModel::onRepeatInitialLoadNewsClick,
        onFirstVisibleIndexChanged = viewModel.contractViewModel::onFirstVisibleIndexChanged,
        onNewsItemClick = { context.openUrl(it.url) },
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
private fun NewsScreen(
    uiState: NewsUiState,
    onRepeatInitialLoadClick: () -> Unit,
    onFirstVisibleIndexChanged: (Int) -> Unit,
    onNewsItemClick: (NewsMdUi) -> Unit,
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .onEach(onFirstVisibleIndexChanged)
            .launchIn(this)
    }

    Scaffold(
        topBar = { NewsTopAppBar() },
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
                    isUaNewsEnabled = false,
                    uaNewsLceState = NewsLceState.None,
                    mdNewsLceState = uiState.firstFetchNewsMdLce,
                    isMdNewsEnabled = true,
                    onRepeatClick = onRepeatInitialLoadClick,
                )
            }
            items(
                items = uiState.news,
                key = { it.id },
            ) {
                BaseNewsItem(
                    title = it.title,
                    date = it.createdAt,
                    url = it.url,
                    onClick = { onNewsItemClick(it) },
                    countryIconId = R.drawable.ic_moldova,
                )
            }
        }
    }
}
