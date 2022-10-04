package com.manmanadmin.reviewing.checkout

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.manmanadmin.R
import com.manmanadmin.utils.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CheckoutFragmentViewModel(private val repository: CheckoutFragmentRepo): ViewModel(), MyViewModel {


    private val _transactionItem = MutableLiveData<RequestLocal>()
    val transactionItem: LiveData<RequestLocal>
    get() = _transactionItem

    private val _priceForThisRequest = MutableLiveData<Double?>()
    val priceForThisRequest: LiveData<Double?>
        get() = _priceForThisRequest

    private val _listOfUpdatedPrices = MutableLiveData<CustomPrices>()
    val listOfUpdatedPrices: LiveData<CustomPrices>
    get() = _listOfUpdatedPrices

    private val _journeyLiveData = MutableLiveData<Journey>()
    val journeyLiveData: LiveData<Journey>
    get() = _journeyLiveData

    private val _navigateToMainFragment = MutableLiveData<Boolean>()
    val navigateToMainFragment: LiveData<Boolean>
    get() = _navigateToMainFragment

    private val _writeToDataBaseStatus = MutableLiveData<GeneralStatus>()
    val writeToDataBaseStatus: LiveData<GeneralStatus>
    get() = _writeToDataBaseStatus
    override var _toolbarTextLiveData = MutableLiveData<String>()

    private val _requestRawInfo= MutableLiveData<ManManRequest?>()
    val requestRawInfo: LiveData<ManManRequest?>
        get() = _requestRawInfo


    fun setRequestRawInfo(request: ManManRequest?) {
        _requestRawInfo.value = request
    }

    override fun setNavigateToMainFragment(value: Boolean){
        _navigateToMainFragment.value = value
    }

    fun setPriceForThisRequest(value: Double?){
        _priceForThisRequest.value = value
    }

    fun setTransactionItem(value: RequestLocal?){
        value?.let { _transactionItem.value = it }
    }

    fun getJourney(transactionItem: RequestLocal, context: Context?){
         val handler = CoroutineExceptionHandler { _, exception ->
            Toast.makeText(context,context?.getString(R.string.internet_error), Toast.LENGTH_LONG).show()
             setPriceForThisRequest(-1.0)
        }

        viewModelScope.launch(handler) {
            withContext(Dispatchers.IO){
                repository.getJourney(_journeyLiveData, transactionItem)
            }

        }

    }

     fun updatePriceList(){
        repository.updatePriceList(_listOfUpdatedPrices)
    }

    fun getPriceForCurrentRequest(journey: Journey, customPrices: CustomPrices) {
        repository.getPriceForCurrentRequest(journey, customPrices, _priceForThisRequest)
    }

    fun updateAndSendRequest(
        reference: DatabaseReference?,
        thisNodeReference: DatabaseReference?,
        transactionItemLocal: RequestLocal
    ){
        repository.updateAndSendRequest(reference, thisNodeReference,transactionItemLocal, _writeToDataBaseStatus)
    }

    fun updateRequest(
        reference: DatabaseReference?,
        thisNodeReference: DatabaseReference?,
        manManRequest: ManManRequest,
        transactionItemLocal: RequestLocal
    ){
        repository.updateRequest(reference, thisNodeReference,manManRequest, transactionItemLocal, _writeToDataBaseStatus)
    }


}