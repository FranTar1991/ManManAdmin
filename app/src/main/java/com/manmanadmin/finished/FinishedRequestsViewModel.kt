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

    private val _allRequestsFinished = MutableLiveData<List<RequestRemote?>?>()
    val allRequestsFinished: LiveData<List<RequestRemote?>?>
        get() = _allRequestsFinished


   private val _numberOfFilteredRequests = MutableLiveData<List<RequestRemote?>?>()
    val numberOfFilteredRequests: LiveData<List<RequestRemote?>?>
        get() = _numberOfFilteredRequests

    private val _dateToFilter = MutableLiveData<String>()
    val dateToFilter: LiveData<String>
        get() = _dateToFilter

    private var _itemDeletedCallback = MutableLiveData<Boolean>()
    val itemDeletedCallback: LiveData<Boolean>
    get() = _itemDeletedCallback



     fun fetchNewFinishedRequests(query: Query){
         repo.fetchFinishedRequests(query, _allRequestsFinished)
    }

    fun setAllFinishedRequests(newData: List<RequestRemote?>?){
        _allRequestsFinished.value = newData
    }


    fun setMarkersForFilteredRequests(numberOfFilteredRequests: List<RequestRemote?>?) {
        _numberOfFilteredRequests.value = numberOfFilteredRequests
    }

    fun setNavigateToFinishedRequestWithDetailsFragment(item: RequestRemote?) {
       _navigateToFinishedRequestWithDetailsFragment.value = item
    }


    fun setDateToFilter(date: String){
        _dateToFilter.value = date
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

    fun filterRequestsByDate(filter: String) {
        if(filter.isNotEmpty()){
            val filteredData =  allRequestsFinished.value?.filter { it -> it?.date?.contains(filter) == true } ?: emptyList()
            setAllFinishedRequests(filteredData)
        }
    }

}