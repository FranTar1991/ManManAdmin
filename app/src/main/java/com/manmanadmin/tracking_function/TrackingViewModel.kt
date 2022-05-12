package com.manmanadmin.tracking_function

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.manmanadmin.R
import com.manmanadmin.location_picker.DEFAULT_LOCATION
import com.manmanadmin.location_picker.DEFAULT_ZOOM
import com.manmanadmin.tracking_function.animation.LatLngInterpolator
import com.manmanadmin.tracking_function.animation.MarkerAnimation
import com.manmanadmin.utils.MMServer
import com.manmanadmin.utils.ManManRequest
import com.manmanadmin.utils.RequestRemote
import com.manmanadmin.utils.getBitmapFromVector


class TrackingViewModel(private val repo: TrackingRepo): ViewModel() {
    private var marker: Marker? = null
    private val _myGoogleMap = MutableLiveData<GoogleMap>()
    val myGoogleMap: LiveData<GoogleMap>
        get() = _myGoogleMap

    private val _updatedRequestLocation = MutableLiveData<LatLng>()
    val updatedRequestLocation: LiveData<LatLng>
    get() = _updatedRequestLocation

    private val _currentTransaction = MutableLiveData<RequestRemote?>()
    val currentTransaction: LiveData<RequestRemote?>
    get() = _currentTransaction

    private val  _serverProcessingTheRequest = MutableLiveData<MMServer?>()
    val  rawTransaction: LiveData<MMServer?>
        get() =  _serverProcessingTheRequest

    fun setMyGoogleMap(map: GoogleMap){
        _myGoogleMap.value = map
    }

    fun setCurrentTransaction(value: RequestRemote?){
        _currentTransaction.value = value
    }

    fun getUpdatedLatLng(transactionId: String){
        repo.getUpdateLatLng(_updatedRequestLocation, transactionId)
    }

    fun updateMapCamera(newLocation: LatLng? = DEFAULT_LOCATION){
        val moveTo = if(newLocation == DEFAULT_LOCATION){
            CameraUpdateFactory
                .newLatLngZoom(newLocation, DEFAULT_ZOOM.toFloat())

        }else{
            CameraUpdateFactory
                .newLatLng(newLocation ?: DEFAULT_LOCATION)
        }

        myGoogleMap.value?.apply {
            moveCamera(moveTo)
        }

    }

    fun setMarker(context: Context){
        marker = _myGoogleMap.value?.addMarker(MarkerOptions()
            .icon(getBitmapFromVector(context, R.drawable.ic_delivery_icon))
            .position(DEFAULT_LOCATION)
            .draggable(false))
    }

    fun updateTheMarkerPosition(newPosition: LatLng = DEFAULT_LOCATION){
//        marker?.position = newPosition
        updateMapCamera(newPosition)
        MarkerAnimation.animateMarkerToGB(marker, newPosition, LatLngInterpolator.Spherical());

    }

    fun setServer(value: MMServer?) {
        _serverProcessingTheRequest.value = value
    }
}