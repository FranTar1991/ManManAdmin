package com.manmanadmin.reviewing.info

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.manmanadmin.utils.ManManRequest
import com.manmanadmin.utils.MyViewModel
import com.manmanadmin.utils.RequestRemote

class ReviewRequestViewModel(private val repo: ReviewRequestRepository, private val app: Application): ViewModel(), MyViewModel {


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

    private val _callWhatsappWithUserPhoneNumberLivedata = MutableLiveData<String?>()
    val callWhatsappWithUserPhoneNumberLivedata: LiveData<String?>
    get() = _callWhatsappWithUserPhoneNumberLivedata

    private val _deliveryGuysOnDutyLiveData = MutableLiveData<MutableList<String?>>()
    val serversListener: LiveData<MutableList<String?>>
        get() = _deliveryGuysOnDutyLiveData


    private val _callWhatsappWithBusinessPhoneNumberLivedata = MutableLiveData<String?>()
    val callWhatsappWithBusinessPhoneNumberLivedata: LiveData<String?>
        get() = _callWhatsappWithBusinessPhoneNumberLivedata

    fun setNavigateToNextFragment(value: Boolean){
        _navigateToNextFragment.value = value
    }


    fun setCallWhatsappWithUserPhoneNumberLivedata(phoneNumber: String?){
        _callWhatsappWithUserPhoneNumberLivedata.value = phoneNumber
    }

    fun setCallWhatsappWithBusinessPhoneNumberLivedata(phoneNumber: String?){
        _callWhatsappWithBusinessPhoneNumberLivedata.value = phoneNumber
    }

    fun setRequestRawInfo(request: ManManRequest?) {
       _requestRawInfo.value = request
    }

    fun setRequestListener(reference: DatabaseReference?, comments: String?){
        repo.setRequestListener(reference, _requestToReview, comments)
    }

    fun setServersListener(reference: DatabaseReference?){
        repo.getDeliveryGuysOnDuty(reference, _deliveryGuysOnDutyLiveData)
    }

    override fun setNavigateToMainFragment(value: Boolean) {
        _navigateToMainFragment.value = value
    }

    fun updateRequestInfo(
        reference: DatabaseReference?,
        details: String?,
        title: String?,
        userName: String,
        userPhone: String,
        comments: String?,
        associate: String? = null,
        navigateToMainFragment: Boolean = false
    ){
        if (navigateToMainFragment){
            repo.updateRequestInfo(reference,_navigateToMainFragment , details, title, userName, userPhone, comments, associate)
        }else{
            repo.updateRequestInfo(reference,_navigateToNextFragment, details, title, userName, userPhone, comments, associate)
        }

    }

    fun deleteThisRequest() {
        repo.deleteThisRequest(thisNodeReference.value, _navigateToMainFragment, _requestRawInfo)
    }

    fun setThisNodeReference(thisNodeReference: DatabaseReference) {
        _thisNodeReference.value = thisNodeReference
    }



}