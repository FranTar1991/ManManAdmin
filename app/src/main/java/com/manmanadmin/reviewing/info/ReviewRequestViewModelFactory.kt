package com.manmanadmin.reviewing.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ReviewRequestViewModelFactory(private val repo: ReviewRequestRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ReviewRequestViewModel::class.java)){
            return ReviewRequestViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown model class")
    }
}