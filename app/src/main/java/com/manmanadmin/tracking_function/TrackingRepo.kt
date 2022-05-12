package com.manmanadmin.tracking_function

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class TrackingRepo(private val databaseReference: DatabaseReference) {

    fun getUpdateLatLng(_liveData: MutableLiveData<LatLng>, transactionId: String){
        Log.i("My reference","$databaseReference")
        databaseReference.child(transactionId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("My reference","$snapshot")

                try {
                    val updatedLat = snapshot.child("trackingLat").value as Double
                    val updatedLon = snapshot.child("trackingLong").value as Double
                    val updatedLatLng = LatLng(updatedLat, updatedLon)
                    _liveData.postValue(updatedLatLng)
                }catch (e: NullPointerException){
                    Log.i("Error","My error: $e")
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}