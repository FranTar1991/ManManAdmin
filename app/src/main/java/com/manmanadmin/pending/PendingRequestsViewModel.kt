package com.manmanadmin.pending

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.manmanadmin.utils.*

class PendingRequestsViewModel(): ViewModel(), ViewModelForAdapterInterface {

    private val _navigateToReviewRequest = MutableLiveData<ManManRequest?>()
    val navigateToReviewRequest: LiveData<ManManRequest?>
    get() = _navigateToReviewRequest

    private val _itemInserted = MutableLiveData<Boolean>()
    val itemInserted : LiveData<Boolean>
    get() = _itemInserted

    init {
        setItemInsert(true)
    }

    override val _numberOfRequests: MutableLiveData<Int> = MutableLiveData<Int>()


    fun setNavigateToReviewRequestFragment(value: ManManRequest?) {
        _navigateToReviewRequest.value = value
    }

    fun sendRequestToNextStage(thisNodeReference: DatabaseReference?){
       sendRequestToNextQueue(thisNodeReference)
    }

    fun setEmptyRequest(requestLocal: RequestLocal){
        insertNewItem(requestLocal)
    }

    private fun insertNewItem(transactionItemLocal: RequestLocal) {
        Firebase.database.reference.child("users").child(FirebaseAuth.getInstance().currentUser?.uid ?: "").child("requests").push()
            .setValue(fromLocalToRemote(transactionItemLocal)).addOnSuccessListener {
               setItemInsert(true)
            }
    }

    fun setItemInsert(value: Boolean){
        _itemInserted.postValue(value)
    }

    private fun fromLocalToRemote(item: RequestLocal): RequestRemote {

        val remoteItem = RequestRemote()
        item.let {
            remoteItem.type = it.type
            remoteItem.details = it.details
            remoteItem.locationBAddressLat = it.locationBAddress?.latitude
            remoteItem.locationBAddressLong = it.locationBAddress?.longitude
            remoteItem.price = it.price
            remoteItem.status = it.status
            remoteItem.userAddressLat = it.userAddress?.latitude
            remoteItem.userAddressLong = it.userAddress?.longitude
            remoteItem.title = it.title
            remoteItem.userAddressReference = it.userAddressReference
            remoteItem.locationBAddressReference = it.locationBAddressReference
            remoteItem.date = it.date
            remoteItem.userName = it.userName
            remoteItem.userPhone = it.userPhone
            remoteItem.agentName = it.agentName
            remoteItem.agentPhone = it.agentPhone
            remoteItem.businessName = it.businessName
            remoteItem.businessPhoneNumber = it.businessPhoneNumber
            remoteItem.comments = it.title
        }
        return remoteItem
    }


}