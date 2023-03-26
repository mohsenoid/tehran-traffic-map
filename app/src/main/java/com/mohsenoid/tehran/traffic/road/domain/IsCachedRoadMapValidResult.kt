package com.mohsenoid.tehran.traffic.road.domain

import android.graphics.Bitmap

sealed interface IsCachedRoadMapValidResult {
    object Unavailable : IsCachedRoadMapValidResult
    data class Valid(val bitmap: Bitmap) : IsCachedRoadMapValidResult
    data class Invalid(val bitmap: Bitmap) : IsCachedRoadMapValidResult
}