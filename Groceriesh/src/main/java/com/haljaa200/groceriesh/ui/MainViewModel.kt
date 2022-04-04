package com.haljaa200.groceriesh.ui

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.haljaa200.groceriesh.R
import com.haljaa200.groceriesh.api.RetrofitInstance
import com.haljaa200.groceriesh.models.*
import com.haljaa200.groceriesh.util.Constants
import com.haljaa200.groceriesh.util.Resource
import com.haljaa200.groceriesh.util.Tools.handleRetrofitResponse
import com.haljaa200.groceriesh.util.Vlog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
open class MainViewModel @Inject constructor(app: Application, private val retrofit: Retrofit, private val sharedPreferences: SharedPreferences): AndroidViewModel(app) {

    protected val context get() = getApplication<Application>()

    fun removePref(key: String) = sharedPreferences.edit().remove(key).apply()
    fun saveString(key: String, value: String) = sharedPreferences.edit().putString(key, value).apply()
    fun saveBoolean(key: String, value: Boolean) = sharedPreferences.edit().putBoolean(key, value).apply()
    fun getString(key: String) =  sharedPreferences.getString(key, "").toString()
    fun getBoolean(key: String) = sharedPreferences.getBoolean(key, false)
    fun saveUserData(customer: Customer, token: String) {
        saveBoolean(Constants.LOGGED_IN, true)
        saveString(Constants.USER_FIRST_NAME, customer.first_name)
        saveString(Constants.USER_LAST_NAME, customer.last_name)
        saveString(Constants.USER_ID, customer._id)
        saveString(Constants.USER_EMAIL, customer.email)
        saveString(Constants.USER_PHONE, customer.phone)
        saveString(Constants.USER_DELIVERY_ADDRESS, customer.delivery_address)
        saveString(Constants.USER_LATITUDE, customer.latitude.toString())
        saveString(Constants.USER_LONGITUDE, customer.longitude.toString())
        saveString(Constants.USER_TOKEN, token)
    }
    fun removeUserData() {
        removePref(Constants.LOGGED_IN)
        removePref(Constants.USER_FIRST_NAME)
        removePref(Constants.USER_LAST_NAME)
        removePref(Constants.USER_ID)
        removePref(Constants.USER_EMAIL)
        removePref(Constants.USER_PHONE)
        removePref(Constants.USER_DELIVERY_ADDRESS)
        removePref(Constants.USER_LATITUDE)
        removePref(Constants.USER_LONGITUDE)
        removePref(Constants.USER_TOKEN)
    }

    val loggedIn = MutableLiveData(getBoolean(Constants.LOGGED_IN))
    val loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val registerResponse: MutableLiveData<Resource<RegisterResponse>> = MutableLiveData()

    fun login(loginData: Login) = viewModelScope.launch {
        loginResponse.postValue(Resource.Loading())

        try {
            if(hasInternetConnection()) {
                val response = RetrofitInstance(retrofit).api.login(loginData)
                loginResponse.postValue(handleRetrofitResponse(response))
            } else {
                loginResponse.postValue(Resource.Error(context.getString(R.string.no_internet)))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> loginResponse.postValue(Resource.Error(context.getString(R.string.check_your_internet)))
                else -> loginResponse.postValue(Resource.Error(context.getString(R.string.connectionError)))
            }
        }
    }

    fun register(registerData: Register) = viewModelScope.launch {
        registerResponse.postValue(Resource.Loading())

        try {
            if(hasInternetConnection()) {
                val response = RetrofitInstance(retrofit).api.register(registerData)
                registerResponse.postValue(handleRetrofitResponse(response))
            } else {
                registerResponse.postValue(Resource.Error(context.getString(R.string.no_internet)))
            }
        } catch(t: Throwable) {
            when(t) {
                is IOException -> registerResponse.postValue(Resource.Error(context.getString(R.string.check_your_internet)))
                else -> registerResponse.postValue(Resource.Error(context.getString(R.string.connectionError)))
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnected
    }

}