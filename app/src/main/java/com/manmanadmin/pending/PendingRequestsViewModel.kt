package com.manmanadmin.pending

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.ui.database.ObservableSnapshotArray
import com.manmanadmin.utils.ManManRequest

class PendingRequestsViewModel(): ViewModel() {

    private val _navigateToReviewRequest = MutableLiveData<ManManRequest?>()
    val navigateToReviewRequest: LiveData<ManManRequest?>
    get() = _navigateToReviewRequest

    private val _numberOfPendingRequests = MutableLiveData<Int>()
    val numberOfPendingRequests: LiveData<Int>
        get() = _numberOfPendingRequests

    fun setNavigateToReviewRequestFragment(value: ManManRequest?) {
        _navigateToReviewRequest.value = value
    }

    fun setNumberOfPendingRequests(numberOfPendingRequests: Int) {
        _numberOfPendingRequests.value = numberOfPendingRequests
    }
}