@file:OptIn(ExperimentalMaterialApi::class)

package com.ferelin.novaposhtanews.features.news.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@Composable
fun NewsBottomSheet(
    modifier: Modifier = Modifier,
    isSettingsDialogActive: Boolean,
    mdNewsEnabled: Boolean,
    uaNewsEnabled: Boolean,
    autoLoadEnabled: Boolean,
    sortByCategoryEnabled: Boolean,
    ascendingTimestampOrderEnabled: Boolean,
    onMdNewsCheckedChange: (Boolean) -> Unit,
    onUaNewsCheckedChange: (Boolean) -> Unit,
    onAutoLoadCheckedChange: (Boolean) -> Unit,
    onSortByCategoryClick: () -> Unit,
    onAscendingTimestampOrderClick: () -> Unit,
    onSettingsDialogDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = {
            val confirm = it == ModalBottomSheetValue.Expanded
            if (it == ModalBottomSheetValue.Hidden) onSettingsDialogDismiss()
            confirm
        },
        skipHalfExpanded = true,
    )

    val coroutineScope = rememberCoroutineScope()
    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }

    LaunchedEffect(key1 = isSettingsDialogActive) {
        if (isSettingsDialogActive) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    ModalBottomSheetLayout(
        modifier = modifier.fillMaxSize(),
        sheetShape = MaterialTheme.shapes.extraLarge,
        sheetState = sheetState,
        sheetContent = {
            NewsSettingsSection(
                mdNewsEnabled = mdNewsEnabled,
                uaNewsEnabled = uaNewsEnabled,
                autoLoadEnabled = autoLoadEnabled,
                sortByCategoryEnabled = sortByCategoryEnabled,
                ascendingTimestampOrderEnabled = ascendingTimestampOrderEnabled,
                onMdNewsCheckedChange = onMdNewsCheckedChange,
                onUaNewsCheckedChange = onUaNewsCheckedChange,
                onAutoLoadCheckedChange = onAutoLoadCheckedChange,
                onSortByCategoryClick = onSortByCategoryClick,
                onAscendingTimestampOrderClick = onAscendingTimestampOrderClick,
            )
        },
        content = content,
    )
}
