package com.ferelin.novaposhtanews.features.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import com.ferelin.novaposhtanews.common.composeui.navigation.NovaPoshtaNewsHost
import com.ferelin.novaposhtanews.common.composeui.theme.NovaPoshtaNewsTheme
import com.ferelin.novaposhtanews.features.news.navigation.NAVIGATION_NEWS_ROUTE

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NovaPoshtaNewsTheme {
                NovaPoshtaNewsHost(
                    navController = rememberNavController(),
                    startDestination = NAVIGATION_NEWS_ROUTE,
                )
            }
        }
    }
}
