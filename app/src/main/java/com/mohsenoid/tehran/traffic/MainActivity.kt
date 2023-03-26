package com.mohsenoid.tehran.traffic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mohsenoid.tehran.traffic.about.presentation.AboutScreen
import com.mohsenoid.tehran.traffic.brt.presentation.BrtScreen
import com.mohsenoid.tehran.traffic.plans.presentation.PlansScreen
import com.mohsenoid.tehran.traffic.road.presentation.RoadScreen
import com.mohsenoid.tehran.traffic.road.presentation.RoadViewModel
import com.mohsenoid.tehran.traffic.ui.theme.TehranTrafficTheme
import com.mohsenoid.tehran.traffic.underground.presentation.UndergroundScreen
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TehranTrafficTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        scaffoldState = scaffoldState,
                        content = { padding ->
                            NavHost(
                                navController = navController,
                                startDestination = ROAD_ROUTE,
                            ) {
//                                composable(
//                                    route = TRAFFIC_ROUTE
//                                ) {
//
//                                    val viewModel = koinViewModel<TrafficViewModel>()
//                                    val state by viewModel.uiState.collectAsState()
//
//                                    TrafficScreen(
//                                        state = state,
//                                        modifier = Modifier
//                                            .fillMaxSize()
//                                            .padding(padding),
//                                    )
//                                }

                                composable(
                                    route = ROAD_ROUTE
                                ) {
                                    val viewModel = koinViewModel<RoadViewModel>()
                                    val state by viewModel.uiState.collectAsState()

                                    RoadScreen(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(padding),
                                        scaffoldState=scaffoldState,
                                        state = state,
                                        onRoadStateToggleExpand = viewModel::onRoadStateToggleExpand,
                                        onRoadStateChanged = viewModel::onRoadStateChanged,
                                        onRefreshRoadStateMap = viewModel::onRefreshRoadStateMap,
                                    )
                                }

                                composable(
                                    route = PLANS_ROUTE
                                ) {
                                    PlansScreen(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(padding),
                                    )
                                }

                                composable(
                                    route = BRT_ROUTE
                                ) {
                                    BrtScreen(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(padding),
                                    )
                                }

                                composable(
                                    route = UNDERGROUND_ROUTE
                                ) {
                                    UndergroundScreen(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(padding),
                                    )
                                }

                                composable(
                                    route = ABOUT_ROUTE
                                ) {
                                    AboutScreen(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(padding),
                                    )
                                }
                            }
                        },
                        bottomBar = {
                            BottomBar { index ->
                                val route = when (index) {
                                    0 -> TRAFFIC_ROUTE
                                    1 -> ROAD_ROUTE
                                    2 -> PLANS_ROUTE
                                    3 -> BRT_ROUTE
                                    4 -> UNDERGROUND_ROUTE
                                    5 -> ABOUT_ROUTE
                                    else -> error("invalid bottom bar index")
                                }

                                navController.navigate(route)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BottomBar(onItemClicked: (index: Int) -> Unit) {
    val selectedIndex = remember { mutableStateOf(0) }
    BottomNavigation(elevation = 10.dp) {

//        BottomNavigationItem(icon = {
//            Icon(
//                painter = painterResource(id = R.drawable.ic_tab_traffic),
//                stringResource(id = R.string.mnu_traffic)
//            )
//        },
//            label = { Text(text = stringResource(id = R.string.mnu_traffic_short)) },
//            selected = (selectedIndex.value == 0),
//            onClick = {
//                selectedIndex.value = 0
//                onItemClicked(0)
//            })

        BottomNavigationItem(icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_tab_road),
                stringResource(id = R.string.mnu_road)
            )
        },
            label = { Text(text = stringResource(id = R.string.mnu_road_short)) },
            selected = (selectedIndex.value == 1),
            onClick = {
                selectedIndex.value = 1
                onItemClicked(1)
            })

        BottomNavigationItem(icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_tab_plans),
                stringResource(id = R.string.mnu_plans)
            )
        },
            label = { Text(text = stringResource(id = R.string.mnu_plans_short)) },
            selected = (selectedIndex.value == 2),
            onClick = {
                selectedIndex.value = 2
                onItemClicked(2)
            })

        BottomNavigationItem(icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_tab_brt),
                stringResource(id = R.string.mnu_brt)
            )
        },
            label = { Text(text = stringResource(id = R.string.mnu_brt_short)) },
            selected = (selectedIndex.value == 3),
            onClick = {
                selectedIndex.value = 3
                onItemClicked(3)
            })

        BottomNavigationItem(icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_tab_underground),
                stringResource(id = R.string.mnu_underground)
            )
        },
            label = { Text(text = stringResource(id = R.string.mnu_underground_short)) },
            selected = (selectedIndex.value == 4),
            onClick = {
                selectedIndex.value = 4
                onItemClicked(4)
            })

        BottomNavigationItem(icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_tab_about),
                stringResource(id = R.string.mnu_about)
            )
        },
            label = { Text(text = stringResource(id = R.string.mnu_about_short)) },
            selected = (selectedIndex.value == 5),
            onClick = {
                selectedIndex.value = 5
                onItemClicked(5)
            })
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    TehranTrafficTheme(darkTheme = false) {
        BottomBar { }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarDarkPreview() {
    TehranTrafficTheme(darkTheme = true) {
        BottomBar { }
    }
}

const val TRAFFIC_ROUTE = "traffic"
const val ROAD_ROUTE = "road"
const val PLANS_ROUTE = "plans"
const val BRT_ROUTE = "brt"
const val UNDERGROUND_ROUTE = "underground"
const val ABOUT_ROUTE = "about"
