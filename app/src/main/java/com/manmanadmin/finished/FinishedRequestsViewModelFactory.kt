package com.manmanadmin.finished

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class FinishedRequestsViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FinishedRequestsViewModel::class.java)){
            return FinishedRequestsViewModel() as T
        }
        throw IllegalArgumentException("unknown model class")
    }

}
