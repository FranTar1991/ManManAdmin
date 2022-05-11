package com.manmanadmin.reviewed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.manmanadmin.utils.ManManRequest
import com.manmanadmin.utils.ViewModelForAdapter

class ReviewedRequestsViewModel(): ViewModel(), ViewModelForAdapter {
    override val _numberOfPendingRequests: MutableLiveData<Int> = MutableLiveData<Int>()


}