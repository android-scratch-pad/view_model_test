package com.example.myapplication

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

class TestApplication : Application(), ViewModelStoreOwner {

    val appViewModelStore = ViewModelStore()

    override fun onCreate() {
        super.onCreate()
        APP_INSTANCE = this
    }

    companion object {
        lateinit var APP_INSTANCE: TestApplication
    }

    override fun getViewModelStore(): ViewModelStore {
        return appViewModelStore
    }
}