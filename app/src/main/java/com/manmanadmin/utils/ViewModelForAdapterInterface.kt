package com.manmanadmin.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference

interface ViewModelForAdapterInterface {
    val _numberOfRequests: MutableLiveData<Int>
    val numberOfRequests: LiveData<Int>
        get() = _numberOfRequests



    fun setNumberOfRequests(numberOfPendingRequests: Int) {
        _numberOfRequests.value = numberOfPendingRequests
    }
    fun saveNewComments(comments: String, reference: DatabaseReference){
        reference.child("comments").setValue(comments)
    }
}