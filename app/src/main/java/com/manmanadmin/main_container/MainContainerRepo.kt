package com.manmanadmin.main_container

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class MainContainerRepo(private val reference: DatabaseReference, private val sharedPreferences: SharedPreferences) {

    fun getBusinessCurrentStatus(callback: MutableLiveData<String?>){
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                callback.postValue(snapshot.getValue(String::class.java))
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun getCurrentShift(): String{
        return sharedPreferences.getString("turno","nothing") ?: ""
    }

    fun setNewShift(shift: String): String{
        sharedPreferences.edit().apply(){
            putString("turno", shift)
            apply()
        }
        return shift
    }
}