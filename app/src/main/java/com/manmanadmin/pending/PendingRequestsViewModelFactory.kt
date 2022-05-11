package com.manmanadmin.pending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class PendingRequestsViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PendingRequestsViewModel::class.java)){
            return PendingRequestsViewModel() as T
        }

        throw IllegalArgumentException("unknown model class")
    }
}