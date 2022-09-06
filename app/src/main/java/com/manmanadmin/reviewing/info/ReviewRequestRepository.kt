package com.manmanadmin.reviewing.info

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.manmanadmin.utils.ManManRequest
import com.manmanadmin.utils.RequestRemote
import com.manmanadmin.utils.STATUS
import com.manmanadmin.utils.getRequestReference

class ReviewRequestRepository() {

    fun setRequestListener(
        reference: DatabaseReference?,
        callback: MutableLiveData<RequestRemote>,
        comments: String?
    ){
        reference?.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val remoteRequest = snapshot.getValue(RequestRemote::class.java)
                remoteRequest?.comments = comments
                callback.postValue(remoteRequest)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun getDeliveryGuysOnDuty(
        reference: DatabaseReference?,
        _deliveryGuysOnDutyLiveData: MutableLiveData<MutableList<String?>>
    ) {

        val listOfAssociateNames = mutableListOf<String?>("Opcional")

        reference?.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

               for( snapChild in snapshot.children){
                   listOfAssociateNames.add(snapChild.child("associate").getValue(String::class.java))
               }
            _deliveryGuysOnDutyLiveData.postValue(listOfAssociateNames)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })



    }

    fun updateRequestInfo(
        reference: DatabaseReference?, callback: MutableLiveData<Boolean>, details: String?,
        title: String?, userName: String, userPhone: String, comments: String?, associate: String?
    ){
        reference?.child("details")?.setValue(details)?.addOnSuccessListener {
            reference.child("title").setValue(title).addOnSuccessListener {
                reference.child("userName").setValue(userName).addOnSuccessListener {
                    reference.child("userPhone").setValue(userPhone).addOnSuccessListener {
                        reference.child("comments").setValue(comments).addOnSuccessListener {
                            reference.child("agentName").setValue(associate).addOnSuccessListener {
                                callback.postValue(true)
                            }
                        }
                    }
                }
            }
        }

    }

    fun deleteThisRequest(
        requestReference: DatabaseReference?,
        _navigateToNextFragment: MutableLiveData<Boolean>,
        rawRequestInfo: MutableLiveData<ManManRequest?>
    ) {
        requestReference?.get()?.addOnSuccessListener {
            it.ref.removeValue().addOnSuccessListener {
                changeRequestStatus(rawRequestInfo)
                _navigateToNextFragment.postValue(true)
            }
        }
    }

    private fun changeRequestStatus(rawRequestInfo: MutableLiveData<ManManRequest?>) {
        val info = rawRequestInfo.value
        val requestRef = getRequestReference(info?.requestId ?: "",info?.user_id ?: "")

        requestRef.child("status").get().addOnSuccessListener {snapshot ->
            snapshot.value?.let{
                snapshot.ref.setValue(STATUS.Canceled.name)
            }
        }

    }

}