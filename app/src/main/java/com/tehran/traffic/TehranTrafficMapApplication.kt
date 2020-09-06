package com.tehran.traffic

import androidx.multidex.MultiDexApplication
import timber.log.Timber

class TehranTrafficMapApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        initTimberDebugTree()
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
}
