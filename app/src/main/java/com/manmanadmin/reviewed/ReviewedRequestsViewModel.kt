package com.manmanadmin.reviewed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.manmanadmin.utils.ViewModelForAdapterInterface

class ReviewedRequestsViewModel(): ViewModel(), ViewModelForAdapterInterface {
    override val _numberOfRequests: MutableLiveData<Int> = MutableLiveData<Int>()


}