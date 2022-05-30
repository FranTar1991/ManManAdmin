package com.manmanadmin.add_business

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase

class AddBusinessViewModel: ViewModel() {

    private val _businessSaved = MutableLiveData<Boolean>()
    val businessSaved: LiveData<Boolean>
    get() = _businessSaved

     fun saveBusiness(business: Business) {
        val reference = FirebaseDatabase.getInstance().reference.child("data").child("businesses")
        reference.push().setValue(business).addOnSuccessListener {
            _businessSaved.postValue(true)
        }
    }

}