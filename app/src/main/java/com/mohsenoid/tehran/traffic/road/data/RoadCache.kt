package com.mohsenoid.tehran.traffic.road.data

import android.content.Context
import androidx.core.content.edit
import com.mohsenoid.tehran.traffic.road.data.model.RoadMap
import com.mohsenoid.tehran.traffic.road.domain.RoadState

class RoadCache(private val context: Context) {

    private val sharedPref = context.getSharedPreferences("road", Context.MODE_PRIVATE)

    fun getStateRoadMap(roadState: RoadState): RoadMap? {
        val file = getRoadMapFile(context, roadState)
        if (!file.exists()) return null

        val timestamp = sharedPref.getLong(KEY_ROAD_CACHE_TIME + roadState, -1)
        return RoadMap(file, timestamp)
    }

    fun setStateRoadMap(roadState: RoadState, timeStamp: Long) {
        sharedPref.edit {
            putLong(KEY_ROAD_CACHE_TIME + roadState, timeStamp)
        }
    }

    companion object {
        private const val KEY_ROAD_CACHE_TIME = "road_cache_time_"
    }
}