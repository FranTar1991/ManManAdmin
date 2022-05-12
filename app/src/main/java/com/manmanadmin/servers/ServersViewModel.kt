package com.manmanadmin.servers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.manmanadmin.utils.RequestRemote

class ServersViewModel(private val repo: ServersRepo) : ViewModel() {

    private val _numberOfServers = MutableLiveData<Int?>()
    val numberOfServers: LiveData<Int?>
    get() = _numberOfServers

    private val _remoteRequest = MutableLiveData<RequestRemote?>()
    val remoteRequest: LiveData<RequestRemote?>
        get() = _remoteRequest

    private val _navigateToTrackingFragment = MutableLiveData<RequestRemote?>()
    val navigateToTrackingFragment: LiveData<RequestRemote?>
    get() = _navigateToTrackingFragment

    fun setNumberOfServers(value: Int?){
        _numberOfServers.value = value
    }

    fun setRemoteRequestListener(requestReference: DatabaseReference) {
        repo.setRemoteRequestListener(requestReference, _remoteRequest)
    }

    fun setNavigateToTrackingFragment(request: RequestRemote?) {
        _navigateToTrackingFragment.value = request
    }


}