package com.manmanadmin.main_activity

import android.annotation.SuppressLint
import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.manmanadmin.utils.GeneralStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainActivityViewModel(private val app: Application): ViewModel() {

    private val _networkCheck= MutableLiveData<GeneralStatus>()
    val networkCheck: LiveData<GeneralStatus>
        get() = _networkCheck

    private val _registrationToken = MutableLiveData<String?>()
    val registrationToken: LiveData<String?>
        get() = _registrationToken


    private val networkRequest: NetworkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        .build()
    private lateinit var connectivityManager: ConnectivityManager
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            _networkCheck.postValue(GeneralStatus.success)
            Log.i("MyStatus","available")
        }

        override fun onUnavailable() {
            super.onUnavailable()
            _networkCheck.postValue(GeneralStatus.error)
            Log.i("MyStatus","Unavailable")
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            _networkCheck.postValue(GeneralStatus.error)
            Log.i("MyStatus","Lost")
        }
    }

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        checkIfIsConnectedToNetwork()
    }

    fun setRegistrationToken(token: String?) {
        _registrationToken.value = token
    }


    @SuppressLint("MissingPermission")
    fun checkIfIsConnectedToNetwork(){
        connectivityManager = app.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        _networkCheck.value = GeneralStatus.loading
    }

}