package com.manmanadmin.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ManManRequest(val user_id: String? = null,
                         val requestId: String? = null,
                         val status: String? = null,
                         val comments: String? = null): Parcelable