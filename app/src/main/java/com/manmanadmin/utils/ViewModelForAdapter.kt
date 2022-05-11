package com.manmanadmin.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface ViewModelForAdapter {
    val _numberOfPendingRequests: MutableLiveData<Int>
    val numberOfPendingRequests: LiveData<Int>
        get() = _numberOfPendingRequests



    fun setNumberOfPendingRequests(numberOfPendingRequests: Int) {
        _numberOfPendingRequests.value = numberOfPendingRequests
    }
}