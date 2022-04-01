package com.haljaa200.groceriesh.ui

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Retrofit
import javax.inject.Inject


@HiltViewModel
open class MainViewModel @Inject constructor(app: Application, private val retrofit: Retrofit, private val sharedPreferences: SharedPreferences): AndroidViewModel(app) {

    protected val context get() = getApplication<Application>()

    fun saveString(key: String, value: String) = sharedPreferences.edit().putString(key, value).apply()
    fun saveBoolean(key: String, value: Boolean) = sharedPreferences.edit().putBoolean(key, value).apply()
    fun getString(key: String) =  sharedPreferences.getString(key, "").toString()
    fun getBoolean(key: String) = sharedPreferences.getBoolean(key, false)
}