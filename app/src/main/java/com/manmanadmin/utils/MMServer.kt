package com.manmanadmin.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MMServer( val FCMToken: String? = null,
                     val associate: String? = null,
                     val currentRequestId: String? = null,
                     val currentRequestComment: String? = null,
                     val currentUserId: String? = null,
                     val lastTimeUsed: Long? = null,
                     val phoneNumber: String? = null,
                     val serverId: String? = null,
                     val serverStatus: String? = null  ): Parcelable