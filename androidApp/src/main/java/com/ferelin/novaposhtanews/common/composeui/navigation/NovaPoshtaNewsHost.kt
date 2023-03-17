package com.ferelin.novaposhtanews.common.composeui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ferelin.novaposhtanews.features.news.navigation.NAVIGATION_NEWS_ROUTE
import com.ferelin.novaposhtanews.features.news.navigation.newsScreen

@Composable
fun NovaPoshtaNewsHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = NAVIGATION_NEWS_ROUTE,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        newsScreen()
    }
}
