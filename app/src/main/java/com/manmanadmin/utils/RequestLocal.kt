package com.manmanadmin.utils

import android.graphics.Bitmap
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class RequestLocal(var id: String? = null,
                        var type: String?= null,
                        var userName: String? = null,
                        var userPhone: String? = null,
                        var title: String? = null,
                        var details: String?= null,
                        var date: String? = null,
                        var status: STATUS?= STATUS.Received,
                        var price: String?= null,
                        var agentName: String? = null,
                        var agentPhone: String? = null,
                        var userAddress: LatLng? = null,
                        var userAddressReference: String? = null,
                        var locationBAddress: LatLng? = null,
                        var locationBAddressReference: String? = null,
                        var userAddressBitmap: Bitmap? = null,
                        var locationBAddressBitmap: Bitmap? = null): Parcelable