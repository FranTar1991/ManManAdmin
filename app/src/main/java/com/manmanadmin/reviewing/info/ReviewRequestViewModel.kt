package com.manmanadmin.reviewing.info

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.manmanadmin.utils.ManManRequest
import com.manmanadmin.utils.MyViewModel
import com.manmanadmin.utils.RequestRemote

class ReviewRequestViewModel(private val repo: ReviewRequestRepository): ViewModel(), MyViewModel {


    private val _requestToReview = MutableLiveData<RequestRemote>()
    val requestToReview: LiveData<RequestRemote>
    get() = _requestToReview

    override var _toolbarTextLiveData = MutableLiveData<String>()

    private val _requestRawInfo= MutableLiveData<ManManRequest?>()
    val requestRawInfo: LiveData<ManManRequest?>
        get() = _requestRawInfo

    private val _navigateToMainFragment= MutableLiveData<Boolean>()
    val navigateToMainFragment: LiveData<Boolean>
        get() = _navigateToMainFragment

    private val _navigateToNextFragment= MutableLiveData<Boolean>()
    val navigateToNextFragment: LiveData<Boolean>
        get() = _navigateToNextFragment

    private val _thisNodeReference= MutableLiveData<DatabaseReference>()
    val thisNodeReference: LiveData<DatabaseReference>
        get() = _thisNodeReference

    fun setNavigateToNextFragment(value: Boolean){
        _navigateToNextFragment.value = value
    }

    fun setRequestRawInfo(request: ManManRequest?) {
       _requestRawInfo.value = request
    }

    fun setRequestListener(reference: DatabaseReference?){
        repo.setRequestListener(reference, _requestToReview)
    }

    override fun setNavigateToMainFragment(value: Boolean) {
        _navigateToMainFragment.value = value
    }

    fun updateRequestInfo(
        reference: DatabaseReference?,
        details: String?,
        title: String?,
        userName: String,
        userPhone: String
    ){
        repo.updateRequestInfo(reference,_navigateToNextFragment, details, title, userName, userPhone)
    }

    fun deleteThisRequest() {
        repo.deleteThisRequest(thisNodeReference.value, _navigateToMainFragment)
    }

    fun setThisNodeReference(thisNodeReference: DatabaseReference) {
        _thisNodeReference.value = thisNodeReference
    }


}