package com.manmanadmin.tracking_function

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("unchecked_cast")
class TrackingViewModelFactory(private val repo: TrackingRepo):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TrackingViewModel::class.java)){
            return TrackingViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown model class")
    }
}