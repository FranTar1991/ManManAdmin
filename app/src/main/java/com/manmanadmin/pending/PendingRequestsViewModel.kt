package com.manmanadmin.pending

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.ui.database.ObservableSnapshotArray
import com.manmanadmin.utils.ManManRequest
import com.manmanadmin.utils.ViewModelForAdapter

class PendingRequestsViewModel(): ViewModel(), ViewModelForAdapter {

    private val _navigateToReviewRequest = MutableLiveData<ManManRequest?>()
    val navigateToReviewRequest: LiveData<ManManRequest?>
    get() = _navigateToReviewRequest

    override val _numberOfRequests: MutableLiveData<Int> = MutableLiveData<Int>()


    fun setNavigateToReviewRequestFragment(value: ManManRequest?) {
        _navigateToReviewRequest.value = value
    }


}