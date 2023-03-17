package com.ferelin.novaposhtanews.features.news.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ferelin.novaposhtanews.features.news.NewsScreenRoute

const val NAVIGATION_NEWS_ROUTE = "navigation_route"

fun NavGraphBuilder.newsScreen() {
    composable(route = NAVIGATION_NEWS_ROUTE) {
        NewsScreenRoute()
    }
}
