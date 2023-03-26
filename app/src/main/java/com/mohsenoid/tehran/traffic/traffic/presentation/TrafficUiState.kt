package com.mohsenoid.tehran.traffic.traffic.presentation

import android.graphics.Bitmap

data class TrafficUiState(
    val isLoading: Boolean = false,
    val bitmap: Bitmap? = null
)
