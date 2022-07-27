package com.manmanadmin.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.manmanadmin.utils.RequestRemote
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FinishedRequestsViewModel (private val repo: FinishedRequestsRepo): ViewModel() {

    private val _navigateToFinishedRequestWithDetailsFragment = MutableLiveData<RequestRemote?>()
            val navigateToFinishedRequestWithDetailsFragment: LiveData<RequestRemote?>
            get() = _navigateToFinishedRequestWithDetailsFragment

    private val _itemsDeleted = MutableLiveData<Boolean>()
    val itemsDeleted: LiveData<Boolean>
        get() = _itemsDeleted

    private val _allRequestsFinished = MutableLiveData<MutableList<RequestRemote?>?>()
    val allRequestsFinished: LiveData<MutableList<RequestRemote?>?>
        get() = _allRequestsFinished

    private val _newAdapterQuery= MutableLiveData<Query>()
    val newAdapterQuery: LiveData<Query>
        get() = _newAdapterQuery

   private val _numberOfRequests = MutableLiveData<Int?>()
    val numberOfRequests: LiveData<Int?>
        get() = _numberOfRequests

    private var _itemDeletedCallback = MutableLiveData<Boolean>()
    val itemDeletedCallback: LiveData<Boolean>
    get() = _itemDeletedCallback



     fun fetchFinishedRequests(query: Query){
        repo.fetchFinishedRequests(query, _allRequestsFinished)
    }

    fun setNumberOfRequests(numberOfPendingRequests: Int?) {
        _numberOfRequests.value = numberOfPendingRequests
    }

    fun setNavigateToFinishedRequestWithDetailsFragment(item: RequestRemote?) {
       _navigateToFinishedRequestWithDetailsFragment.value = item
    }

    fun setAllRequestsFinished(allFinishedRequests : MutableList<RequestRemote?>?){
        _allRequestsFinished.value = allFinishedRequests

    }

    fun deleteThisItem(
        item: RequestRemote
    ) {
        item.id?.let {
            repo.deleteRequest(it, _itemDeletedCallback)
        }

    }


    fun removeAllRequests(finishedRequestReference: DatabaseReference) {

        viewModelScope.launch {
            withContext(Dispatchers.IO){
                finishedRequestReference.removeValue()
                _itemsDeleted.postValue(true)
            }
        }

    }

    fun setNewQuery(newQuery: Query) {
        _newAdapterQuery.value = newQuery
    }

}