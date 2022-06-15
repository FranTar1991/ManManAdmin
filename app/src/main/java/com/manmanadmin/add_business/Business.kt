package com.manmanadmin.add_business

import android.os.Parcelable
import com.manmanadmin.utils.BusinessMenu
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
data class Business(var name: String? = null,
                    var lat: Double? = null,
                    var long: Double? = null,
                    var category: String? = null,
                    var imageUrl: String? = null,
                    var businessPhoneNumber: String? = null,
                    var id: Long = Random.nextLong(100000000000),
                    var menu: ArrayList<BusinessMenu>? = null): Parcelable