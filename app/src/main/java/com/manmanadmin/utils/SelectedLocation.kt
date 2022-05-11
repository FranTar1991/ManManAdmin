package com.manmanadmin.utils

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectedLocation(val location: LatLng? = null): Parcelable