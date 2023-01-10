package com.parisa.app.ui.homePage

import android.content.Context

class SampleSingletonClass {
    private var context: Context? = null
    private var instance: SampleSingletonClass? = null

    private fun SingletonSampleClass(context: Context) {
        this.context = context
    }

    @Synchronized
    fun getInstance(context: Context): SampleSingletonClass? {
        if (instance == null) instance = SampleSingletonClass()
        return instance
    }

    fun onDestroy() {
        if (context != null) {
            context = null
        }
    }
}