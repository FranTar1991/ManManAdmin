package com.manmanadmin.finished

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.manmanadmin.utils.RequestRemote

class FinishedRequestsRepo (private val databaseReference: DatabaseReference) {




    fun fetchFinishedRequests(query: Query, _allFinishedRequestLiveDataCallback: MutableLiveData<List<RequestRemote?>?>) {
        val oldList = mutableListOf<RequestRemote?>()

        query.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                _allFinishedRequestLiveDataCallback.postValue(getNewList(snapshot, oldList))

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                _allFinishedRequestLiveDataCallback.postValue(getUpdatedList(snapshot, oldList))

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                _allFinishedRequestLiveDataCallback.postValue(getUpdatedList(snapshot, oldList, false))
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    private fun getNewList(snapshot: DataSnapshot,
                           my_list: MutableList<RequestRemote?>?): MutableList<RequestRemote?>? {
        val item = getNewItem(snapshot)
        item?.id = snapshot.key

        my_list?.add(0,item)
        return my_list
    }


    private fun getUpdatedList(snapshot: DataSnapshot, oldList: MutableList<RequestRemote?>, isUpdate: Boolean = true): MutableList<RequestRemote?>? {
        val updatedItem = getNewItem(snapshot)
        updatedItem?.id = snapshot.key

        val oldItem = oldList.firstOrNull{it?.id == updatedItem?.id }
        val oldIndex = oldList.indexOf(oldItem)
        oldList.removeAt(oldIndex)

        if (isUpdate){
            oldList.add(oldIndex,updatedItem)
        }

        return oldList
    }

    private fun getNewItem(snapshot: DataSnapshot): RequestRemote? {
        return snapshot.getValue(RequestRemote::class.java)
    }


    fun deleteRequest(
        callId: String,
        itemDeletedCallback: MutableLiveData<Boolean>
    ){
        databaseReference.child(callId).get().addOnSuccessListener {
            it?.let {

                it.ref.removeValue()
                itemDeletedCallback.postValue(true)
            }
        }
    }

}