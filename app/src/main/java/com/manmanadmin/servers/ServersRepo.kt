package com.manmanadmin.servers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.manmanadmin.utils.MMServer
import com.manmanadmin.utils.ManManRequest
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

    fun sendBackToQueue(server: MMServer) {
        val baseReference = FirebaseDatabase.getInstance().reference

        server.serverId?.let {
            baseReference.child("servers")
                .child(server.serverId).get().addOnSuccessListener { server_to_remove ->
                    server_to_remove.ref.removeValue().addOnSuccessListener {
                        copyPasteInQueue(server, baseReference)
                    }
                }
        }

    }

    private fun copyPasteInQueue(server: MMServer, baseReference: DatabaseReference) {
        val request = getCurrentRequest(server)

        val map = mutableMapOf ("comments" to request.comments, "user_id" to request.user_id)
        request.requestId?.let {
            baseReference.child("all_requests")
                .child(it).setValue(map)
        }
    }

    private fun getCurrentRequest(item: MMServer): ManManRequest {
        return ManManRequest(comments = item.currentRequestComment, user_id = item.currentUserId, requestId = item.currentRequestId)
    }


}