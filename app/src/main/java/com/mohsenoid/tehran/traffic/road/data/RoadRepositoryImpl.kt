package com.mohsenoid.tehran.traffic.road.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.mohsenoid.tehran.traffic.road.data.model.RoadMap
import com.mohsenoid.tehran.traffic.road.data.service.RoadService
import com.mohsenoid.tehran.traffic.road.domain.IsCachedRoadMapValidResult
import com.mohsenoid.tehran.traffic.road.domain.RoadRepository
import com.mohsenoid.tehran.traffic.road.domain.RoadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class RoadRepositoryImpl(
    private val cache: RoadCache,
    private val service: RoadService,
) : RoadRepository {

    override suspend fun isCachedRoadMapValid(roadState: RoadState): IsCachedRoadMapValidResult {
        val cachedRoadMap = getCachedRoadMap(roadState) ?: return IsCachedRoadMapValidResult.Unavailable
        val bitmap = decodeBitmap(cachedRoadMap.file)
        return if (cachedRoadMap.isValid()) {
            IsCachedRoadMapValidResult.Valid(bitmap)
        } else {
            IsCachedRoadMapValidResult.Invalid(bitmap)
        }
    }

    override suspend fun getRoadMap(roadState: RoadState): Bitmap? {
        val cachedRoadMap = getCachedRoadMap(roadState)

        if (cachedRoadMap?.isValid() == true) return decodeBitmap(cachedRoadMap.file)

        val roadMap = service.getRoadMap(roadState) ?: return null

        cache.setStateRoadMap(roadState, System.currentTimeMillis())
        return decodeBitmap(roadMap)
    }

    private fun getCachedRoadMap(roadState: RoadState): RoadMap? {
        return cache.getStateRoadMap(roadState)
    }

    private suspend fun decodeBitmap(file: File): Bitmap {
        return withContext(Dispatchers.IO) {
            val inputStream = file.inputStream()
            BitmapFactory.decodeStream(inputStream)
        }
    }

    companion object {
        private const val VALID_TIME = 5 * 60 * 1000// 5min

        private fun RoadMap.isValid(): Boolean {
            return timeStamp + VALID_TIME > System.currentTimeMillis()
        }
    }
}