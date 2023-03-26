package com.mohsenoid.tehran.traffic.traffic

import com.mohsenoid.tehran.traffic.traffic.presentation.TrafficViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val trafficModule = module {
    viewModel { TrafficViewModel() }
}