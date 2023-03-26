package com.mohsenoid.tehran.traffic.road.presentation

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohsenoid.tehran.traffic.road.domain.IsCachedRoadMapValidResult
import com.mohsenoid.tehran.traffic.road.domain.RoadRepository
import com.mohsenoid.tehran.traffic.road.domain.RoadState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RoadViewModel(private val roadRepository: RoadRepository) : ViewModel() {

    private val _uiState: MutableStateFlow<RoadUiState> = MutableStateFlow(RoadUiState())
    val uiState: StateFlow<RoadUiState> by ::_uiState

    init {
        onRoadStateChanged(RoadState.TEHRAN)
    }

    fun onRoadStateToggleExpand() {
        _uiState.update { currentState ->
            currentState.copy(isRoadStateExpanded = !currentState.isRoadStateExpanded)
        }
    }

    fun onRoadStateChanged(roadState: RoadState) {
        _uiState.update { currentState ->
            currentState.copy(isLoading = true, isRoadStateExpanded = false, selectedRoadState = roadState)
        }
        viewModelScope.launch {
            when (val result = roadRepository.isCachedRoadMapValid(roadState)) {
                IsCachedRoadMapValidResult.Unavailable -> fetchRoadStateMap(roadState)
                is IsCachedRoadMapValidResult.Valid -> _uiState.update { currentState ->
                    currentState.copy(isLoading = false, isRefreshSnackbarVisible = false, bitmap = result.bitmap)
                }
                is IsCachedRoadMapValidResult.Invalid -> _uiState.update { currentState ->
                    currentState.copy(isLoading = false, isRefreshSnackbarVisible = true, bitmap = result.bitmap)
                }
            }
        }
    }

    fun onRefreshRoadStateMap() {
        _uiState.update { currentState ->
            currentState.copy(isLoading = true, isRoadStateExpanded = false, isRefreshSnackbarVisible = false)
        }
        viewModelScope.launch {
            fetchRoadStateMap(uiState.value.selectedRoadState)
        }
    }

    private suspend fun fetchRoadStateMap(roadState: RoadState) {
        val bitmap: Bitmap? = roadRepository.getRoadMap(roadState = roadState)
        _uiState.update { currentState ->
            currentState.copy(isLoading = false)
        }
        if (bitmap != null) {
            _uiState.update { currentState ->
                currentState.copy(bitmap = bitmap)
            }
        }
    }
}