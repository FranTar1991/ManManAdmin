package com.manmanadmin.add_business

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.FirebaseDatabase
import com.manmanadmin.utils.showSnackbar

class AddBusinessViewModel: ViewModel() {

    private val _businessSaved = MutableLiveData<Boolean>()
    val businessSaved: LiveData<Boolean>
    get() = _businessSaved

     fun saveBusiness(business: Business) {

         try {
             val reference = FirebaseDatabase.getInstance().reference.child("data")
                 .child("businesses")
             reference.child(getNameWithoutSpaces(business.name)).setValue(business).addOnSuccessListener {
                 _businessSaved.postValue(true)
             }
         } catch (error: DatabaseException){

         }

    }

    private fun getNameWithoutSpaces(name: String?): String {
        return name?.replace(" ","") ?: ""
    }

}