package com.mohsenoid.tehran.traffic.road

import com.mohsenoid.tehran.traffic.road.data.*
import com.mohsenoid.tehran.traffic.road.data.service.ApiInterface
import com.mohsenoid.tehran.traffic.road.data.service.RoadApiClient
import com.mohsenoid.tehran.traffic.road.data.service.RoadService
import com.mohsenoid.tehran.traffic.road.domain.RoadRepository
import com.mohsenoid.tehran.traffic.road.presentation.RoadViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val roadModule = module {

    single {
        RoadApiClient.getClient().create(ApiInterface::class.java)
    }

    single { RoadService(context = get(), apiInterface = get()) }

    single { RoadCache(context = get()) }

    single<RoadRepository> { RoadRepositoryImpl(cache = get(), service = get()) }

    viewModel { RoadViewModel(roadRepository = get()) }
}