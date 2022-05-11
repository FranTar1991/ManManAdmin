package com.manmanadmin.reviewing.addresses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ReviewAddressViewModelFactory(private val repo: ReviewAddressRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewAddressViewModel::class.java)){
            return ReviewAddressViewModel(repo) as T
        }
        throw IllegalArgumentException("unknown model class")
    }
}