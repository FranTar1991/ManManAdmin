package com.manmanadmin.finished

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query
import com.manmanadmin.utils.GeneralStatus
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

    private val _transactionAddedLiveData = MutableLiveData<GeneralStatus>()
    val transactionAddedLiveData: LiveData<GeneralStatus>
        get() = _transactionAddedLiveData


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

    fun addTransactionToFirebase(transaction: RequestRemote){
        repo.addTransactionToFirebase(transaction,_transactionAddedLiveData)
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
            val filteredData = try {
                val dates = filter.split("/")
                  getFilteredDataWithTwoDates(dates)
            }catch (e: Exception){
                getFilteredDataDate(filter)
            }

            setAllFinishedRequests(filteredData)
        }
    }

    private fun getFilteredDataWithTwoDates(dates: List<String>): List<RequestRemote?> {

        var indexOfFirst = 0
        var indexOfLast = allRequestsFinished.value?.size?.let { it - 1 } ?: 0


        allRequestsFinished.value?.let { list ->
            indexOfFirst = list.indexOfFirst { it?.date?.contains(dates[0]) == true }
            indexOfLast =   list.indexOfLast { it?.date?.contains(dates[1]) == true }
        }

        return allRequestsFinished.value?.slice(indexOfFirst..indexOfLast) ?: emptyList()
    }
    private fun getFilteredDataDate(date: String): List<RequestRemote?> {
        return allRequestsFinished.value?.filter { it -> it?.date?.contains(date) == true} ?: emptyList()
    }

}