package com.manmanadmin.reviewing.transaction_checkout

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.manmanadmin.R
import com.manmanadmin.utils.*

class CheckoutFragmentRepo(private val databaseReference: DatabaseReference,
                           private val user: FirebaseUser?,
                           private val context: Context?) {


     fun getJourney(liveData: MutableLiveData<Journey>, transactionItem: RequestLocal?) {


            val userAddressCity = getCity(transactionItem?.userAddress)
            val locationBCity = getCity(transactionItem?.locationBAddress)
            liveData.postValue(Journey(userAddressCity,locationBCity))



    }

    private fun getCity(latLng: LatLng?): String? {
        var address = mutableListOf<Address>()
        var city = context?.getString(R.string.price_not_defined)

        try {
            latLng?.apply {
                    address =  Geocoder(context).getFromLocation(latLng.latitude,latLng.longitude,1)
            }

            city = address[0].locality.replace(" ","")
        }catch (err: IndexOutOfBoundsException){

        }
        Log.i("MyCity","$city")
       return city
    }

    fun updatePriceList(livedata: MutableLiveData<CustomPrices>){
        databaseReference.child("data").
        child("price").
        addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val prices = snapshot.getValue(CustomPrices::class.java)
                livedata.postValue(prices)
            }

            override fun onCancelled(error: DatabaseError) {
                livedata.postValue(null)
            }

        })
    }

    fun getPriceForCurrentRequest(
        journey: Journey,
        customPrices: CustomPrices,
        priceForThisRequest: MutableLiveData<Double?>
    ) {

        val isSameCity = journey.locationBCity == journey.userCity

        val priceFoLocationB: Int? = when(journey.locationBCity){
               LOCALITIES.Diriamba.name -> customPrices.diriamba
               LOCALITIES.Jinotepe.name -> customPrices.jinotepe
               LOCALITIES.ElRosario.name -> customPrices.el_rosario
               LOCALITIES.Guisquiliapa.name -> customPrices.guisquiliapa
               LOCALITIES.LaPazdeCarazo.name -> customPrices.la_paz
               LOCALITIES.SanMarcos.name -> customPrices.san_marcos
               LOCALITIES.SantaTeresa.name -> customPrices.santa_teresa
               LOCALITIES.Dolores.name -> customPrices.dolores
               else -> {0}
           }

        val priceForUser = when (journey.userCity) {
                LOCALITIES.Diriamba.name -> customPrices.diriamba
                LOCALITIES.Jinotepe.name -> customPrices.jinotepe
                LOCALITIES.ElRosario.name -> customPrices.el_rosario
                LOCALITIES.Guisquiliapa.name -> customPrices.guisquiliapa
                LOCALITIES.LaPazdeCarazo.name -> customPrices.la_paz
                LOCALITIES.SanMarcos.name -> customPrices.san_marcos
                LOCALITIES.SantaTeresa.name -> customPrices.santa_teresa
                LOCALITIES.Dolores.name -> customPrices.dolores
                else -> { 0 }
            }


        val priceProcessed: Double =   if (priceFoLocationB == 0 || priceForUser == 0){
            -1.0
        }else if (isSameCity){
            (priceFoLocationB!! + priceForUser!!).toDouble()

        }else if (!isSameCity){

            (priceFoLocationB!! + priceForUser!! * 1.40).toDouble()

        }else{
            -1.0
        }
        priceForThisRequest.postValue(priceProcessed)

    }


    fun updateRequest(
        reference: DatabaseReference?,
        thisNodeReference: DatabaseReference?,
        baseRef: DatabaseReference,
        transactionItemLocal: RequestLocal,
        _navigateToMainFragment: MutableLiveData<GeneralStatus>
    ){
        reference?.setValue(fromLocalToRemote(transactionItemLocal))
            ?.addOnSuccessListener {

                sendTransactionToTheServers(baseRef, thisNodeReference)
                _navigateToMainFragment.postValue(GeneralStatus.success)


            }?.addOnFailureListener {
                _navigateToMainFragment.postValue(GeneralStatus.error)
            }
    }

    private fun sendTransactionToTheServers(baseRef:DatabaseReference, thisNodeReference: DatabaseReference?) {
        thisNodeReference?.get()?.addOnSuccessListener { dataToTransfer ->
            val formattedDataToPaste = getObjectToPaste(dataToTransfer)
            dataToTransfer.key?.let { thisKey -> baseRef.child("all_requests")
                .child(thisKey)
                .setValue(formattedDataToPaste).addOnSuccessListener {
                dataToTransfer.ref.removeValue()
            }}
        }
    }

    private fun getObjectToPaste(dataToTransfer: DataSnapshot): ManManRequest? {
        val userId = dataToTransfer.child("user_id").value.toString()
        val comments = dataToTransfer.child("comments").value.toString()
       return ManManRequest(user_id = userId, comments = comments)
    }

    private fun fromLocalToRemote(item: RequestLocal): RequestRemote {

        val remoteItem = RequestRemote()
        item.let {
            remoteItem.type = it.type
            remoteItem.details = it.details
            remoteItem.locationBAddressLat = it.locationBAddress?.latitude
            remoteItem.locationBAddressLong = it.locationBAddress?.longitude
            remoteItem.price = it.price
            remoteItem.status = it.status
            remoteItem.userAddressLat = it.userAddress?.latitude
            remoteItem.userAddressLong = it.userAddress?.longitude
            remoteItem.title = it.title
            remoteItem.userAddressReference = it.userAddressReference
            remoteItem.locationBAddressReference = it.locationBAddressReference
            remoteItem.date = it.date
            remoteItem.userName = it.userName
            remoteItem.userPhone = it.userPhone
            remoteItem.agentName = it.agentName
            remoteItem.agentPhone = it.agentPhone
        }
        return remoteItem
    }

}