package com.manmanadmin.servers

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.manmanadmin.utils.MMServer
import com.manmanadmin.utils.RequestRemote

class ServersViewModel(private val repo: ServersRepo) : ViewModel() {

    private val _numberOfServers = MutableLiveData<Int?>()
    val numberOfServers: LiveData<Int?>
    get() = _numberOfServers

    private val _remoteRequest = MutableLiveData<RequestRemote?>()
    val remoteRequest: LiveData<RequestRemote?>
        get() = _remoteRequest

    private val _confirmSendBackToQueue = MutableLiveData<MMServer?>()
    val confirmSendBackToQueue: LiveData<MMServer?>
        get() = _confirmSendBackToQueue

    private val _callWhatsappWithPhoneNumberLiveData = MutableLiveData<String?>()
    val callWhatsappWithPhoneNumberLiveData: LiveData<String?>
    get() = _callWhatsappWithPhoneNumberLiveData

    private val _navigateToTrackingFragment = MutableLiveData<RequestRemote?>()
    val navigateToTrackingFragment: LiveData<RequestRemote?>
    get() = _navigateToTrackingFragment

    private val _showProgressbar = MutableLiveData<Int>()
    val showProgressbar: LiveData<Int>
        get() = _showProgressbar

    fun setNumberOfServers(value: Int?){
        _numberOfServers.value = value
    }

    init {
        setShowProgressbar(View.GONE)
    }

    fun setShowProgressbar(value: Int){
        _showProgressbar.value = value
    }

    fun setRemoteRequestListener(requestReference: DatabaseReference) {
        repo.setRemoteRequestListener(requestReference, _remoteRequest)
    }

    fun setNavigateToTrackingFragment(request: RequestRemote?) {
        _navigateToTrackingFragment.value = request
    }

    fun setCallWhatsappWithPhoneNumber(phoneNumber: String?) {
        _callWhatsappWithPhoneNumberLiveData.value = phoneNumber
    }


    fun setConfirmSendBackToQueue(server: MMServer?){
        _confirmSendBackToQueue.value = server
    }

    fun sendBackToQueue(server: MMServer) {
        repo.sendBackToQueue(server)
    }


}