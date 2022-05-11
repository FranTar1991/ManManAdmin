package com.manmanadmin.reviewing.info

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.manmanadmin.utils.RequestRemote

class ReviewRequestRepository() {

    fun setRequestListener(reference: DatabaseReference?, callback: MutableLiveData<RequestRemote>){
        reference?.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val remoteRequest = snapshot.getValue(RequestRemote::class.java)
                callback.postValue(remoteRequest)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun updateRequestInfo(
        reference: DatabaseReference?,
        callback: MutableLiveData<Boolean>,
        details: String?,
        title: String?,
        userName: String,
        userPhone: String
    ){
        reference?.child("details")?.setValue(details)?.addOnSuccessListener {
            reference.child("title").setValue(title).addOnSuccessListener {
                reference.child("userName").setValue(userName).addOnSuccessListener {
                    reference.child("userPhone").setValue(userPhone).addOnSuccessListener {
                        callback.postValue(true)
                    }
                }
            }
        }

    }

}