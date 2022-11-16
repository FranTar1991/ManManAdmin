package com.manmanadmin.finished

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

    private val _anyFilter = MutableLiveData<String>()
    val anyFilter: LiveData<String>
        get() = _anyFilter

    private val _phoneNumberFilter = MutableLiveData<String>()
    val phoneNumber: LiveData<String>
    get() = _phoneNumberFilter

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

    fun filterRequestsByAnyFilter(filter: String){
        val actualFilter = filter.split("/")[1]
        val resultList =
        //agent name
        if (filter.contains("a/")){
             getListFilteredByAgentName(actualFilter)
            //details
        } else if (filter.contains("d/")){
             getListFilteredByDetails(actualFilter)
        } else {
            listOf()
        }

        setAllFinishedRequests(resultList)
    }

    private fun getListFilteredByAgentName(agentName: String): List<RequestRemote?> {
        var agentNameList = listOf<RequestRemote?>()

        if (agentName.isNotEmpty()){
            allRequestsFinished.value?.let { list ->
                agentNameList = list.filter { it?.agentName?.contains(agentName) == true }
            }
        }

        return agentNameList
    }
    private fun getListFilteredByDetails(detailKey: String): List<RequestRemote?> {
        var result = listOf<RequestRemote?>()

        if (detailKey.isNotEmpty()){
            allRequestsFinished.value?.let { list ->
                result = list.filter { it?.details?.contains(detailKey) == true }
            }
        }

        return result
    }


    fun getListFilteredByPhoneNumber(phoneNumber: String){
        var result = listOf<RequestRemote?>()


        if (phoneNumber.isNotEmpty()){
            allRequestsFinished.value?.let { list ->
                result = list.filter { it?.userPhone?.replace(" ","") == phoneNumber.replace(" ","") }
            }
        }

        setAllFinishedRequests(result)

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

    fun setAnyFilter(filter: String) {
        _anyFilter.value = filter
    }

    fun setFilterWithPhoneNumber(filter: String){
        _phoneNumberFilter.value = filter
    }

}