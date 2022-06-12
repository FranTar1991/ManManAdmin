package com.manmanadmin.utils

import android.location.Location
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class RequestRemote(var id: String? = "",
                         var details: String?= null,
                         var userName: String? = null,

                         var userPhone: String? = null,
                         var locationBAddressLong: Double? = null,
                         var locationBAddressLat: Double? = null,

                         var trackingLong: Double = CITIES.Jinotepe.latLng.longitude,
                         var trackingLat: Double = CITIES.Jinotepe.latLng.latitude,

                         var price: Double?= null,
                         var agentName: String? = null,
                         var agentPhone: String? = null,

                         var date: String? = null,
                         var status: String?= null,
                         var title: String? = null,

                         var type: String?= null,
                         var userAddressLong: Double? = null,
                         var userAddressReference: String? = null,
                         var locationBAddressReference: String? = null,
                         var businessName: String? = null,
                         var businessPhoneNumber: String? = null,
                         var comments: String? = null,
                         var userAddressLat: Double? = null, ): Parcelable