package com.manmanadmin.location_picker

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.manmanadmin.R
import com.manmanadmin.add_business.Business
import com.manmanadmin.utils.BusinessMenu
import com.manmanadmin.utils.CITIES
import com.manmanadmin.utils.RequestLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random


val DEFAULT_LOCATION = CITIES.Jinotepe.latLng
 const val DEFAULT_ZOOM = 15
 const val ZOOM_BUILDING_LEVEL = 18
private const val TAG: String = "LOCATION CLASS"

class LocationPickerViewModel(private val app: Application):AndroidViewModel(app) {

    private val _myGoogleMap = MutableLiveData<GoogleMap>()
    val myGoogleMap: LiveData<GoogleMap>
        get() = _myGoogleMap

    private val _isLocationPermissionGranted = MutableLiveData<Boolean>()
    val isLocationPermissionGranted: LiveData<Boolean>
        get() = _isLocationPermissionGranted

    private val _businessArrayLiveData = MutableLiveData<MutableList<Business>?>()
    val businessArrayLiveData: LiveData<MutableList<Business>?>
    get() = _businessArrayLiveData

    private val _locationSelected = MutableLiveData<LatLng>()
    val locationSelected: LiveData<LatLng>
        get() = _locationSelected

    private val _navigateToMainF = MutableLiveData<Boolean>()
    val navigateToMainF: LiveData<Boolean>
        get() = _navigateToMainF


    private val _currentTransactionItem = MutableLiveData<RequestLocal?>()
    val currentTransactionItem: LiveData<RequestLocal?>
        get() = _currentTransactionItem

    private val _callingF = MutableLiveData<String?>()
    val callingF: LiveData<String?>
        get() = _callingF

    private val _businessSelected = MutableLiveData<Business?>()
    val businessSelected: LiveData<Business?>
        get() = _businessSelected

    private val _referenceToShow = MutableLiveData<String?>()
    val referenceToShow: LiveData<String?>
        get() = _referenceToShow

    init {
        setBusinessData()
    }

    fun setReferenceToShow(value: String?){
       _referenceToShow.value = value
    }

    fun setCurrentTransactionItem(value: RequestLocal?){
        _currentTransactionItem.value = value
    }

    fun setCallingFragment(value: String?){
        _callingF.value = value
    }

    val onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
            when(parent?.getItemAtPosition(pos)){
                CITIES.Jinotepe.name->{updateMapCamera(CITIES.Jinotepe.latLng, DEFAULT_ZOOM.toFloat())}
                CITIES.Diriamba.name ->{updateMapCamera(CITIES.Diriamba.latLng, DEFAULT_ZOOM.toFloat())}
                CITIES.San_Marcos.name ->{updateMapCamera(CITIES.San_Marcos.latLng, DEFAULT_ZOOM.toFloat())}
                CITIES.El_Rosario.name ->{updateMapCamera(CITIES.El_Rosario.latLng, DEFAULT_ZOOM.toFloat())}
                CITIES.Santa_Teresa.name ->{updateMapCamera(CITIES.Santa_Teresa.latLng,
                    DEFAULT_ZOOM.toFloat())}
                CITIES.La_Paz.name ->{updateMapCamera(CITIES.La_Paz.latLng, DEFAULT_ZOOM.toFloat())}
                CITIES.Dolores.name ->{updateMapCamera(CITIES.Dolores.latLng, DEFAULT_ZOOM.toFloat())}
                CITIES.Guisquiliapa.name->{updateMapCamera(CITIES.Guisquiliapa.latLng, DEFAULT_ZOOM.toFloat())}
                else ->{}
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {

        }

    }

    private fun setBusinessData(){

        viewModelScope.launch {
            withContext(Dispatchers.IO){
                FirebaseDatabase.getInstance().reference.child("data")
                    .child("businesses").get().addOnSuccessListener { snapshot ->
                        _businessArrayLiveData.postValue(getBusinessObject(snapshot))
                    }
            }
        }


    }

    private fun getBusinessObject(dataSnapshot: DataSnapshot): ArrayList<Business> {


        val listOfBusinesses = ArrayList<Business>()

        for (childSnapshot in dataSnapshot.children){
            val businessItem = Business(name = childSnapshot?.child("name")?.getValue(String::class.java),
                lat = childSnapshot?.child("lat")?.getValue(Double::class.java),
                long = childSnapshot?.child("long")?.getValue(Double::class.java),
                category = childSnapshot?.child("category")?.getValue(String::class.java),
                imageUrl = childSnapshot?.child("imageUrl")?.getValue(String::class.java),
                businessPhoneNumber = childSnapshot?.child("businessPhoneNumber")?.getValue(String::class.java),
                id = childSnapshot?.child("id")?.getValue(Long::class.java) ?: Random.nextLong(100000000000),
                menu = null)

         listOfBusinesses.add(businessItem)
        }


        return listOfBusinesses


    }


    fun setIsLocationPermissionGranted(result: Boolean){
        _isLocationPermissionGranted.value = result
    }

    fun setMyGoogleMap(map: GoogleMap){
        _myGoogleMap.value = map
    }

    fun getSpinnerAdapter(): SpinnerAdapter{
        val adapter = ArrayAdapter.createFromResource(
            app,
            R.array.cities_array,
            android.R.layout.simple_spinner_item
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(R.layout.spinner_item_layout)
        }

        return adapter
    }


    fun updateMapCamera(newLocation: LatLng? = DEFAULT_LOCATION, zoom: Float? = null){

        if (zoom == null){
            myGoogleMap.value?.moveCamera(
                CameraUpdateFactory
                    .newLatLng(newLocation ?: DEFAULT_LOCATION))
        }else{
            myGoogleMap.value?.moveCamera(
                CameraUpdateFactory
                    .newLatLngZoom(newLocation ?: DEFAULT_LOCATION,zoom))
        }

    }

    @SuppressLint("MissingPermission")
    fun getDeviceLocation(fusedLocationProviderClient: FusedLocationProviderClient,
                          activity: Activity, repeat: Boolean = false) {

        val locationResult = fusedLocationProviderClient.lastLocation
        locationResult.addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {

               var lastKnownLocation = task.result
                if (lastKnownLocation != null) {

                    setLocationSelected(LatLng(lastKnownLocation.latitude,lastKnownLocation.longitude))
                }else{
                    if(repeat){
                        getDeviceLocation(fusedLocationProviderClient,activity,true)
                    }
                    //updateMapCamera()
                }
            } else {
                Log.d(TAG, "Current location is null. Using defaults.")
                Log.e(TAG, "Exception: %s", task.exception)
                updateMapCamera()

            }
            updateUi()
        }
    }



    fun setMainMarker(context: Context) {
        viewModelScope.launch {
            delay(500)
            myGoogleMap.value?.apply {
                setLocationSelected(cameraPosition.target)
                     setOnCameraMoveListener {
                         setLocationSelected(cameraPosition.target)
                     }
            }
        }
    }

      fun setLocationSelected(position: LatLng) {
        _locationSelected.value = position
    }

    @SuppressLint("MissingPermission")
    fun updateUi() {

        myGoogleMap.value?.isMyLocationEnabled = true
        myGoogleMap.value?.uiSettings?.isMyLocationButtonEnabled = true
    }

    fun setBusinessSelected(businessSelected: Business) {
        _businessSelected.value = businessSelected
    }


}