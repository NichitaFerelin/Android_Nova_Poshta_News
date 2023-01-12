package com.ferelin.novaposhtanews.common.composeui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ferelin.novaposhtanews.R

@Composable
fun AppFloatingActionsButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        modifier = modifier,
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        onClick = onClick,
    ) {
        Icon(
            modifier = Modifier.size(dimensionResource(id = R.dimen.fab_icon_size)),
            painter = painterResource(id = R.drawable.ic_arrow_upward),
            contentDescription = stringResource(id = R.string.description_scroll_to_top),
            tint = MaterialTheme.colorScheme.onPrimary,
        )
    }
}
