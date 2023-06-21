@file:OptIn(ExperimentalMaterial3Api::class)

package net.vinceblas.middaratracker

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.vinceblas.middaratracker.TurnTrackerViewModel.TurnTrackerUiState
import net.vinceblas.middaratracker.ui.theme.MiddaraTrackerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: TurnTrackerViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    
    MiddaraTrackerTheme {
        // A surface container using the 'background' color from the theme
        Scaffold(
            topBar = {
                TopBar(uiState, viewModel)
            },
            floatingActionButton = {
                if (uiState.turnList.isNotEmpty() && !uiState.shuffleInProgress) {
                    FloatingActionButton(
                        onClick =
                        { scope.launch(Dispatchers.Main) { viewModel.newRound() } }

                    ) {
                        Icon(Icons.Filled.Refresh, "Reshuffle")
                    }
                }
            }
        ) { contentPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                TurnCardGrid(uiState)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun TopBar(
    uiState: TurnTrackerUiState,
    viewModel: TurnTrackerViewModel
) {
    TopAppBar(
        title = { Text("Middara Tracker") },
        actions = {
            if (uiState.pickerMode) {
                IconButton(onClick = {
                    viewModel.endPickerMode()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Confirm"
                    )
                }
            } else {
                IconButton(onClick = {
                    viewModel.startPickerMode()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Pick Combatants"
                    )
                }
                if (uiState.selected != null) {

                    IconButton(onClick = {
                        viewModel.nudge(uiState.selected, -1)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Move Back"
                        )
                    }

                    IconButton(onClick = {
                        viewModel.nudge(uiState.selected, 1)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowForward,
                            contentDescription = "Move Forward"
                        )
                    }

                    IconButton(onClick = {
                        viewModel.removeCard(uiState.selected)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete Selected"
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TurnCardGrid(uiState: TurnTrackerUiState, viewModel: TurnTrackerViewModel = viewModel()) {

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // some unfortunate-ness here with width/height stuff.
        // leftover pixels when splitting grid create uneven dimens which adds hairline gaps between images
        val cards = if (uiState.pickerMode) {
            uiState.availableCards
        } else {
            uiState.turnList
        }
        items(cards, key = { item: Combatant -> item.name }) { combatant: Combatant ->
            Box(
                Modifier
                    .clickable {
                        viewModel.selectCard(combatant)
                    }
                    .animateItemPlacement(
                        spring(
                            stiffness = Spring.StiffnessVeryLow,
                            visibilityThreshold = IntOffset.VisibilityThreshold
                        )
                    )
                    .then(
                        if (combatant == uiState.selected) {
                            Modifier.border(6.dp, Color.Cyan)
                        } else if (uiState.pickerMode && uiState.turnList.contains(combatant)) {
                            Modifier.border(4.dp, Color.Blue)
                        } else Modifier
                    )
            ) {
                // 3 x 2, 480x720
                Image(
                    painter = painterResource(combatant.image),
                    contentDescription = combatant.name,
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center
                )

            }
        }
    }
}