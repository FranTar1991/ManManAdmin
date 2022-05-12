package com.manmanadmin.finished.with_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.manmanadmin.utils.RequestRemote

class RequestWithDetailsViewModel(): ViewModel() {

    private val _finishedRequestWithDetails = MutableLiveData<RequestRemote>()
    val finishedRequestWithDetails: LiveData<RequestRemote>
    get() = _finishedRequestWithDetails


    fun setFinishedRequestWithDetails(value: RequestRemote?){
        _finishedRequestWithDetails.value = value
    }
}