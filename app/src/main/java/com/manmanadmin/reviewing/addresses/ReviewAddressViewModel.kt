package com.manmanadmin.reviewing.addresses

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DatabaseReference
import com.manmanadmin.utils.ManManRequest
import com.manmanadmin.utils.MyViewModel
import com.manmanadmin.utils.RequestLocal
import com.manmanadmin.utils.RequestRemote


class ReviewAddressViewModel(private val repo: ReviewAddressRepository): ViewModel(), MyViewModel {

    private val _transactionItem = MutableLiveData<RequestLocal?>()
    val transactionItem: LiveData<RequestLocal?>
    get() = _transactionItem

    private val _userAddressBitmapLiveData = MutableLiveData<Bitmap?>()
    val userAddressBitmapLiveData: LiveData<Bitmap?>
    get() = _userAddressBitmapLiveData

    private val _locationBAddressBitmapLiveData= MutableLiveData<Bitmap?>()
    val locationBAddressBitmapLiveData: LiveData<Bitmap?>
        get() = _locationBAddressBitmapLiveData

    private val _navigateToMainF = MutableLiveData<Boolean>()
    val navigateToMainF: LiveData<Boolean>
        get() = _navigateToMainF

    private val _navigateToCheckoutFragment = MutableLiveData<Boolean>()
    val navigateToCheckoutFragment: LiveData<Boolean>
        get() = _navigateToCheckoutFragment

    private val _requestRawInfo= MutableLiveData<ManManRequest?>()
    val requestRawInfo: LiveData<ManManRequest?>
        get() = _requestRawInfo

    private val _requestToReview = MutableLiveData<RequestRemote?>()
    val requestToReview: LiveData<RequestRemote?>
        get() = _requestToReview

    override var _toolbarTextLiveData = MutableLiveData<String>()

    fun setRequestRawInfo(request: ManManRequest?) {
        _requestRawInfo.value = request
    }

    fun setRequestListener(reference: DatabaseReference?){
        repo.setRequestListener(reference, _requestToReview)
    }

    fun setNavigateToCheckoutFragment(value: Boolean){
        _navigateToCheckoutFragment.value = value
    }

    fun updateTransactionItem(value: RequestLocal?){
        _transactionItem.value = value
        value?.userAddressBitmap?.apply { setUserAddressBitmapLiveData(this) }
        value?.locationBAddressBitmap?.apply { setLocationBAddressBitmapLiveData(this) }
    }


    private fun setUserAddressBitmapLiveData(bitmap: Bitmap){
       _userAddressBitmapLiveData.value = getCroppedImage(bitmap)
    }
    private fun setLocationBAddressBitmapLiveData(bitmap: Bitmap){
        _locationBAddressBitmapLiveData.value =
            getCroppedImage(bitmap)
    }
    private fun getCroppedImage(bitmap: Bitmap): Bitmap? {
        return  Bitmap.createBitmap(bitmap, 0, bitmap.height/3,  bitmap.width,bitmap.height/3)
    }


    override fun setNavigateToMainFragment(value: Boolean){
        _navigateToMainF.value = value
    }

    fun fromRemoteToLocal(transactionItemRemote: RequestRemote): RequestLocal {
        val transactionItemLocal = RequestLocal()
        transactionItemLocal.apply {
            id = transactionItemRemote.id
            title = transactionItemRemote.title
            details = transactionItemRemote.details
            type = transactionItemRemote.type
            userAddress = getLatLng(transactionItemRemote.userAddressLat, transactionItemRemote.userAddressLong)
            locationBAddress = getLatLng(transactionItemRemote.locationBAddressLat, transactionItemRemote.locationBAddressLong)
            price = transactionItemRemote.price
            status = transactionItemRemote.status
            userAddressReference = transactionItemRemote.userAddressReference
            locationBAddressReference = transactionItemRemote.locationBAddressReference
            date = transactionItemRemote.date
            userName = transactionItemRemote.userName
            userPhone = transactionItemRemote.userPhone
            agentName = transactionItemRemote.agentName
            agentPhone = transactionItemRemote.agentPhone
        }


        return transactionItemLocal
    }

    private fun getLatLng(latitude: Double?, longitude: Double?): LatLng? {
        val lat = latitude
        val long = longitude
        var newLatLng: LatLng? = null
        lat?.let { latIn ->
            long?.let { longIn ->
                newLatLng = LatLng(latIn,longIn)
            }
        }

        return newLatLng
    }
}