package com.mohsenoid.tehran.traffic.road.data.service

import android.content.Context
import com.mohsenoid.tehran.traffic.road.data.getRoadMapFile
import com.mohsenoid.tehran.traffic.road.data.getRoadMapId
import com.mohsenoid.tehran.traffic.road.domain.RoadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class RoadService(
    private val context: Context,
    private val apiInterface: ApiInterface
) {

    suspend fun getRoadMap(roadState: RoadState): File? = withContext(Dispatchers.IO) {
        val id = getRoadMapId(roadState)
        val result = apiInterface.getRoadMap(id)
        if (result.isSuccessful) {
            val file = getRoadMapFile(context, roadState)
            val writeResult = result.body()?.writeToDisk(file) ?: false
            return@withContext if (writeResult) file else null
        } else {
            null
        }
    }

    private fun ResponseBody.writeToDisk(file: File): Boolean {
        return try {
            byteStream().use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream, 16 * 1024)
                }
            }
            true
        } catch (e: IOException) {
            false
        }
    }
}