package com.mohsenoid.tehran.traffic.road.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mohsenoid.tehran.traffic.R
import com.mohsenoid.tehran.traffic.road.domain.RoadState
import com.mohsenoid.tehran.traffic.ui.ZoomableImage
import com.mohsenoid.tehran.traffic.ui.theme.RoadBackground
import com.mohsenoid.tehran.traffic.ui.theme.TehranTrafficTheme
import kotlinx.coroutines.launch


@Composable
fun RoadScreen(
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
    state: RoadUiState = RoadUiState(),
    onRoadStateToggleExpand: () -> Unit,
    onRoadStateChanged: (state: RoadState) -> Unit,
    onRefreshRoadStateMap: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val message = stringResource(id = R.string.msg_updatemap)
    val actionLabel = stringResource(id = R.string.msg_update)
    LaunchedEffect(key1 = state) {
        scope.launch {
            if (state.isRefreshSnackbarVisible) {
                when (scaffoldState.snackbarHostState.showSnackbar(message, actionLabel, SnackbarDuration.Indefinite)) {
                    SnackbarResult.Dismissed -> Unit
                    SnackbarResult.ActionPerformed -> onRefreshRoadStateMap()
                }
            }
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .background(RoadBackground)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                val bitmap = state.bitmap
                if (bitmap != null) {
                    ZoomableImage(
                        painter = BitmapPainter(bitmap.asImageBitmap()),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(),
                        backgroundColor = RoadBackground,
                        initialScale = 3f
                    )
                }
            }
        }

        RoadStateSelector(
            modifier = Modifier.fillMaxWidth(),
            state = state,
            onRoadStateToggleExpand = onRoadStateToggleExpand,
            onRoadStateChanged = onRoadStateChanged
        )
    }
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
private fun RoadStateSelector(
    modifier: Modifier = Modifier,
    state: RoadUiState = RoadUiState(),
    onRoadStateToggleExpand: () -> Unit,
    onRoadStateChanged: (state: RoadState) -> Unit
) {
    val roadStateNames = stringArrayResource(id = R.array.states).mapIndexed { index, name ->
        RoadStateNames(
            roadState = RoadState.values()[index],
            name = name
        )
    }.sortedBy { it.name }


    ExposedDropdownMenuBox(
        expanded = state.isRoadStateExpanded,
        onExpandedChange = {
            onRoadStateToggleExpand()
        }
    ) {
        TextField(
            modifier = modifier,
            readOnly = true,
            value = roadStateNames.first { it.roadState == state.selectedRoadState }.name,
            onValueChange = { },
            label = { Text(stringResource(id = R.string.state)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = state.isRoadStateExpanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = state.isRoadStateExpanded,
            onDismissRequest = {
                onRoadStateToggleExpand()
            }
        ) {
            roadStateNames.forEachIndexed { index, selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        onRoadStateChanged(selectionOption.roadState)
                    }
                ) {
                    Text(
                        text = selectionOption.name
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoadScreenPreview() {
    TehranTrafficTheme(darkTheme = false) {
        RoadScreen(
            scaffoldState = rememberScaffoldState(),
            state = RoadUiState(isLoading = true),
            onRoadStateToggleExpand = {},
            onRoadStateChanged = {},
            onRefreshRoadStateMap = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RoadScreenDarkPreview() {
    TehranTrafficTheme(darkTheme = true) {
        RoadScreen(
            scaffoldState = rememberScaffoldState(),
            onRoadStateToggleExpand = {},
            onRoadStateChanged = {},
            onRefreshRoadStateMap = {},
        )
    }
}

data class RoadStateNames(val roadState: RoadState, val name: String)