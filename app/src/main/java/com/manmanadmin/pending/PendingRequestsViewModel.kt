package com.manmanadmin.pending

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.manmanadmin.utils.ManManRequest
import com.manmanadmin.utils.ViewModelForAdapterInterface
import com.manmanadmin.utils.getManManRequestToPaste
import com.manmanadmin.utils.sendRequestToNextQueue

class PendingRequestsViewModel(): ViewModel(), ViewModelForAdapterInterface {

    private val _navigateToReviewRequest = MutableLiveData<ManManRequest?>()
    val navigateToReviewRequest: LiveData<ManManRequest?>
    get() = _navigateToReviewRequest

    override val _numberOfRequests: MutableLiveData<Int> = MutableLiveData<Int>()


    fun setNavigateToReviewRequestFragment(value: ManManRequest?) {
        _navigateToReviewRequest.value = value
    }

    fun sendRequestToNextStage(thisNodeReference: DatabaseReference?){
       sendRequestToNextQueue(thisNodeReference)
    }

}