package com.mohsenoid.tehran.traffic.underground.presentation

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.mohsenoid.tehran.traffic.R
import com.mohsenoid.tehran.traffic.ui.ZoomableImage
import com.mohsenoid.tehran.traffic.ui.theme.TehranTrafficTheme

@Composable
fun UndergroundScreen(modifier: Modifier = Modifier) {
    val resources = LocalContext.current.resources
    val inputStream = resources.openRawResource(R.raw.metro_map)
    val bitmap = BitmapFactory.decodeStream(inputStream)
    val painter = BitmapPainter(bitmap.asImageBitmap())
    Column(
        modifier = modifier
    ) {
        ZoomableImage(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
fun UndergroundScreenPreview() {
    TehranTrafficTheme(darkTheme = false) {
        UndergroundScreen()
    }
}

@Preview
@Composable
fun UndergroundScreenDarkPreview() {
    TehranTrafficTheme(darkTheme = true) {
        UndergroundScreen()
    }
}
