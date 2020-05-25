package com.example.android.dwms

import android.app.Application

class App : Application() {

    companion object {
        lateinit var prefs : MyShardPreferences
    }
    /* prefs라는 이름의 MyShardPreferences 하나만 생성할 수 있도록 설정. */

    override fun onCreate() {
        prefs = MyShardPreferences(applicationContext)
        super.onCreate()
    }


}