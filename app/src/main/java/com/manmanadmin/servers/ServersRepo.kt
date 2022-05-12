package com.manmanadmin.servers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.manmanadmin.utils.RequestRemote

class ServersRepo() {
    fun setRemoteRequestListener(
        requestReference: DatabaseReference,
        _remoteRequest: MutableLiveData<RequestRemote?>
    ) {
       requestReference.get().addOnSuccessListener {
          val requestRemote = it.getValue(RequestRemote::class.java)
           _remoteRequest.postValue(requestRemote)
           Log.i("MyRequestToTrack","${requestRemote}")
       }
    }


}