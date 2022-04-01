package com.haljaa200.groceriesh

import android.app.Application
import android.content.SharedPreferences
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class Application: Application() {
    @Inject lateinit var sharedPreferences: SharedPreferences
}