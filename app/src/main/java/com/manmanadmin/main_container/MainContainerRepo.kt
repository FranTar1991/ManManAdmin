package com.manmanadmin.main_container

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class MainContainerRepo(private val reference: DatabaseReference) {

    fun getBusinessCurrentStatus(callback: MutableLiveData<String?>){
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                callback.postValue(snapshot.getValue(String::class.java))
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}