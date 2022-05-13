package com.manmanadmin.change_business_status

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChangeBusinessStatusRepo(private val reference: DatabaseReference) {


    suspend fun changeBusinessStatus(status: String?, _businessStatusChanged: MediatorLiveData<Boolean?>){

        withContext(Dispatchers.IO){
            reference.setValue(status).addOnSuccessListener {
                _businessStatusChanged.postValue(true)
            }
        }
    }

    fun getBusinessCurrentStatus(callback: MutableLiveData<String?>){
        reference.get().addOnSuccessListener {
            callback.postValue(it.getValue(String::class.java))
        }
    }
}