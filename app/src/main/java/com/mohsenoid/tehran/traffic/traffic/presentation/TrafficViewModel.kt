package com.mohsenoid.tehran.traffic.traffic.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TrafficViewModel : ViewModel() {

    private val _uiState: MutableStateFlow<TrafficUiState> = MutableStateFlow(TrafficUiState())
    val uiState: StateFlow<TrafficUiState> by ::_uiState

}