package com.mohsenoid.tehran.traffic.road.data

import android.content.Context
import com.mohsenoid.tehran.traffic.road.domain.RoadState
import java.io.File

fun getRoadMapId(roadState: RoadState): String {
    return when (roadState) {
        RoadState.MAZANDARAN -> "1"
        RoadState.TEHRAN -> "2"
        RoadState.FARS -> "3"
        RoadState.KHORASAN_RAZAVI -> "4"
        RoadState.AZERBAIJAN_WEST -> "5"
        RoadState.AZERBAIJAN_EAST -> "6"
        RoadState.ARDABIL -> "7"
        RoadState.GILAN -> "8"
        RoadState.ZANJAN -> "9"
        RoadState.KURDISTAN -> "10"
        RoadState.KERMANSHAH -> "11"
        RoadState.HAMADAN -> "12"
        RoadState.QAZVIN -> "13"
        RoadState.ALBORZ -> "14"
        RoadState.GOLESTAN -> "15"
        RoadState.KHORASAN_NORTH -> "16"
        RoadState.SEMNAN -> "17"
        RoadState.QOM -> "18"
        RoadState.MARKAZI -> "19"
        RoadState.LURESTAN -> "20"
        RoadState.ILAM -> "21"
        RoadState.ISFAHAN -> "22"
        RoadState.YAZD -> "23"
        RoadState.KHORASAN_SOUTH -> "24"
        RoadState.CHAHARMAHAL_AND_BAKHTIARI -> "25"
        RoadState.KHUZESTAN -> "26"
        RoadState.KOHGILUYEH_AND_BOYERAHMAD -> "27"
        RoadState.BOUSHEHR -> "28"
        RoadState.KERMAN -> "29"
        RoadState.SISTAN_AND_BALUCHESTAN -> "30"
        RoadState.HORMOZGAN -> "31"
    }
}

fun getRoadMapFile(context: Context, roadState: RoadState): File {
    val id = getRoadMapId(roadState)
    return File(context.filesDir.toString() + "/road" + id + ".png")
}