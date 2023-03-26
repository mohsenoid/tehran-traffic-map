package com.mohsenoid.tehran.traffic.traffic.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.tooling.preview.Preview
import com.mohsenoid.tehran.traffic.ui.ZoomableImage
import com.mohsenoid.tehran.traffic.ui.theme.RoadBackground
import com.mohsenoid.tehran.traffic.ui.theme.TehranTrafficTheme
import com.mohsenoid.tehran.traffic.ui.theme.TrafficBackground

@Composable
fun TrafficScreen(
    state: TrafficUiState = TrafficUiState(),
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(RoadBackground)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                val bitmap = state.bitmap
                if (bitmap != null) {
                    ZoomableImage(
                        painter = BitmapPainter(bitmap.asImageBitmap()),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(),
                        backgroundColor = TrafficBackground,
                        initialScale = 3f
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TrafficScreenPreview() {
    TehranTrafficTheme(darkTheme = false) {
        TrafficScreen()
    }
}

@Preview
@Composable
fun TrafficScreenDarkPreview() {
    TehranTrafficTheme(darkTheme = true) {
        TrafficScreen()
    }
}
