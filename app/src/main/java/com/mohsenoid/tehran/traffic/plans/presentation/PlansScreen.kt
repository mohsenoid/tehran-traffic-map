package com.mohsenoid.tehran.traffic.plans.presentation

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
fun PlansScreen(modifier: Modifier = Modifier) {
    val resources = LocalContext.current.resources
    val inputStream = resources.openRawResource(R.raw.traffic_cam)
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
fun PlansScreenPreview() {
    TehranTrafficTheme(darkTheme = false) {
        PlansScreen()
    }
}

@Preview
@Composable
fun PlansScreenDarkPreview() {
    TehranTrafficTheme(darkTheme = true) {
        PlansScreen()
    }
}
