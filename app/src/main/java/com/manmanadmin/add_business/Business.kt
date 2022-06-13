package com.manmanadmin.add_business

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Business(val name: String? = null,
                    val lat: Double? = null,
                    val long: Double? = null,
                    val category: String? = null,
                    val imageUrl: String? = null,
                    val businessPhoneNumber: String? = null): Parcelable