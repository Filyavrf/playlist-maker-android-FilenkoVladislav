package com.practicum.myapplication

import android.app.Application
import com.practicum.myapplication.creator.Creator

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Creator.init(this)
    }
}