package com.example.nasamarsrovers.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.nasamarsrovers.R
import com.example.nasamarsrovers.ui.gallery.GalleryState
import com.example.nasamarsrovers.utils.CURIOSITY
import com.example.nasamarsrovers.utils.OPPORTUNITY
import com.example.nasamarsrovers.utils.SPIRIT

@Composable
fun DrawerContent(
    state: GalleryState,
    onRoverSelected: (String) -> Unit,
    onSolPickerClick: () -> Unit,
    onDatePickerClick: () -> Unit,
    onCameraPickerClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        DrawerHeader()

        Divider()

        RoverDrawerItem(
            text = stringResource(R.string.curiosity),
            selected = state.currentRover == CURIOSITY,
            onClick = { onRoverSelected(CURIOSITY) }
        )
        RoverDrawerItem(
            text = stringResource(R.string.opportunity),
            selected = state.currentRover == OPPORTUNITY,
            onClick = { onRoverSelected(OPPORTUNITY) }
        )
        RoverDrawerItem(
            text = stringResource(R.string.spirit),
            selected = state.currentRover == SPIRIT,
            onClick = { onRoverSelected(SPIRIT) }
        )

        Divider()

        DrawerTextItem(
            text = if (!state.isEarthDateUsed) {
                stringResource(
                    R.string.sol,
                    state.currentSol?.toString()
                        ?: stringResource(R.string.value_not_selected)
                )
            } else {
                stringResource(R.string.sol_not_picked)
            },
            onClick = onSolPickerClick
        )

        DrawerTextItem(
            text = if (state.isEarthDateUsed) {
                stringResource(
                    R.string.earth_date,
                    state.currentEarthDate
                        ?: stringResource(R.string.value_not_selected)
                )
            } else {
                stringResource(R.string.earth_date_not_picked)
            },
            onClick = onDatePickerClick
        )

        DrawerTextItem(
            text = stringResource(R.string.camera),
            onClick = onCameraPickerClick
        )
    }
}

@Composable
private fun DrawerHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.subtitle1,
            color = Color.White
        )
        Text(
            text = stringResource(R.string.nav_header_subtitle),
            style = MaterialTheme.typography.body2,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun RoverDrawerItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (selected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(16.dp))
        } else {
            Spacer(Modifier.width(40.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.body1,
            color = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
        )
    }
}

@Composable
private fun DrawerTextItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.body1,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

