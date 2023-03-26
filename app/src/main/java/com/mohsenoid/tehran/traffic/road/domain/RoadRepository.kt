package com.mohsenoid.tehran.traffic.road.domain

import android.graphics.Bitmap

interface RoadRepository {

    suspend fun isCachedRoadMapValid(roadState: RoadState): IsCachedRoadMapValidResult

    suspend fun getRoadMap(roadState: RoadState): Bitmap?
}