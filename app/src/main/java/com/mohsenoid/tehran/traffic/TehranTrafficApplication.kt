package com.mohsenoid.tehran.traffic

import android.app.Application
import com.mohsenoid.tehran.traffic.road.roadModule
import com.mohsenoid.tehran.traffic.traffic.trafficModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class TehranTrafficApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initTimberDebugTree()
        initKoin()
    }

    private fun initTimberDebugTree() {
        val debugTree = object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String {
                // adding file name and line number link to debug logs
                return "${super.createStackElementTag(element)}(${element.fileName}:${element.lineNumber}"
            }
        }
        Timber.plant(debugTree)
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@TehranTrafficApplication)
            modules(appModule, trafficModule, roadModule)
        }
    }
}
