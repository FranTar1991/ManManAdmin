package com.manmanadmin.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface ViewModelForAdapter {
    val _numberOfRequests: MutableLiveData<Int>
    val numberOfRequests: LiveData<Int>
        get() = _numberOfRequests



    fun setNumberOfRequests(numberOfPendingRequests: Int) {
        _numberOfRequests.value = numberOfPendingRequests
    }


}