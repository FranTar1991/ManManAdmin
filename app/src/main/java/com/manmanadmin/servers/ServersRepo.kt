package com.manmanadmin.servers

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.manmanadmin.utils.ManManRequest
import com.manmanadmin.utils.RequestRemote
import com.manmanadmin.utils.STATUS

class ServersRepo() {
    val baseReference = FirebaseDatabase.getInstance().reference

    fun setRemoteRequestListener(
        requestReference: DatabaseReference,
        _remoteRequest: MutableLiveData<RequestRemote?>
    ) {
       requestReference.get().addOnSuccessListener {
          val requestRemote = it.getValue(RequestRemote::class.java)
           _remoteRequest.postValue(requestRemote)
       }
    }

    fun sendBackToQueue(server: MMServer) {


        server.serverId?.let {
            baseReference.child("servers")
                .child(server.serverId).get().addOnSuccessListener { server_to_remove ->
                    server_to_remove.ref.removeValue().addOnSuccessListener {
                        copyPasteInQueue(server)
                    }
                }
        }

    }

    private fun copyPasteInQueue(server: MMServer) {
        val request = getCurrentRequest(server)

        val map = mutableMapOf ("comments" to request.comments, "user_id" to request.user_id)
        request.requestId?.let {
            baseReference.child("all_requests")
                .child(it).setValue(map).addOnSuccessListener {
                    changeTheRequestStatusToReceived(server)
                }
        }
    }

    private fun changeTheRequestStatusToReceived(server: MMServer){
       val ref = baseReference.child("users").child(server.currentUserId ?: "")
            .child("requests").child(server.currentRequestId ?: "")
            ref.child("status").setValue(STATUS.Received.name).addOnSuccessListener {
                ref.child("agentName").setValue(null).addOnSuccessListener {
                    ref.child("agentPhone").setValue(null)
                }
            }

    }

    private fun getCurrentRequest(item: MMServer): ManManRequest {
        return ManManRequest(comments = item.currentRequestComment, user_id = item.currentUserId, requestId = item.currentRequestId)
    }


}