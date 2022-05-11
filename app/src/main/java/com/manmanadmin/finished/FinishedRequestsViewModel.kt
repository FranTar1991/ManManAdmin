package com.manmanadmin.finished

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.manmanadmin.utils.ViewModelForAdapter

class FinishedRequestsViewModel (): ViewModel(), ViewModelForAdapter {
    override val _numberOfPendingRequests: MutableLiveData<Int> = MutableLiveData<Int>()
}