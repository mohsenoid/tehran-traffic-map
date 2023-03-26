package com.mohsenoid.tehran.traffic.about.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohsenoid.tehran.traffic.BuildConfig
import com.mohsenoid.tehran.traffic.R
import com.mohsenoid.tehran.traffic.ui.theme.TehranTrafficTheme

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.road_logo),
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
            )
        }
        Text(
            text = stringResource(id = R.string.app_about),
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(24.dp)
        )
        Text(
            text = stringResource(id = R.string.app_help_text),
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .weight(1F)
                .padding(16.dp)

        )
        Text(
            text = "Build: " + BuildConfig.VERSION_NAME + " - " + BuildConfig.GIT_SHA + " - " + BuildConfig.BUILD_TYPE + " - " + BuildConfig.BUILD_TIME,
            modifier = Modifier.padding(16.dp),
            color = colorResource(id = R.color.logo),
            style = MaterialTheme.typography.caption
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    TehranTrafficTheme(darkTheme = false) {
        AboutScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun AboutScreenDarkPreview() {
    TehranTrafficTheme(darkTheme = true) {
        AboutScreen()
    }
}