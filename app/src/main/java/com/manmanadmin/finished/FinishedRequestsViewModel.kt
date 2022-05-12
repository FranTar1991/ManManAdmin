package com.manmanadmin.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.manmanadmin.utils.RequestRemote
import com.manmanadmin.utils.ViewModelForAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FinishedRequestsViewModel (): ViewModel() {

    private val _navigateToFinishedRequestWithDetailsFragment = MutableLiveData<RequestRemote?>()
            val navigateToFinishedRequestWithDetailsFragment: LiveData<RequestRemote?>
            get() = _navigateToFinishedRequestWithDetailsFragment

    private val _itemsDeleted = MutableLiveData<Boolean>()
    val itemsDeleted: LiveData<Boolean>
        get() = _itemsDeleted

    private val _allRequestsFinished = MutableLiveData<List<RequestRemote>?>()
    val allRequestsFinished: LiveData<List<RequestRemote>?>
        get() = _allRequestsFinished

    private val _adapterQuery= MutableLiveData<Query>()
    val adapterQuery: LiveData<Query>
        get() = _adapterQuery

   private val _numberOfRequests = MutableLiveData<Int?>()
    val numberOfRequests: LiveData<Int?>
        get() = _numberOfRequests



    fun setNumberOfRequests(numberOfPendingRequests: Int?) {
        _numberOfRequests.value = numberOfPendingRequests
    }

    fun setNavigateToFinishedRequestWithDetailsFragment(item: RequestRemote?) {
       _navigateToFinishedRequestWithDetailsFragment.value = item
    }

    fun setAllRequestsFinished(allFinishedRequests : List<RequestRemote>?){
        _allRequestsFinished.value = allFinishedRequests

    }
    fun removeAllRequests(finishedRequestReference: DatabaseReference) {

        viewModelScope.launch {
            withContext(Dispatchers.IO){
                finishedRequestReference.removeValue()
                _itemsDeleted.postValue(true)
            }
        }

    }

    fun setQuery(mainQuery: Query) {
        _adapterQuery.value = mainQuery
    }

}