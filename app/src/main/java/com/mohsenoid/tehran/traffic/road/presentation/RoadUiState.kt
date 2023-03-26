package com.mohsenoid.tehran.traffic.road.presentation

import android.graphics.Bitmap
import com.mohsenoid.tehran.traffic.road.domain.RoadState


data class RoadUiState(
    val isLoading: Boolean = false,
    val selectedRoadState: RoadState = RoadState.ALBORZ,
    val isRoadStateExpanded: Boolean = false,
    val bitmap: Bitmap? = null,
    val isRefreshSnackbarVisible: Boolean = false
)
