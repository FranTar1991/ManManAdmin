package com.manmanadmin.reviewing.info

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ReviewRequestViewModelFactory(private val repo: ReviewRequestRepository, private val app: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ReviewRequestViewModel::class.java)){
            return ReviewRequestViewModel(repo, app) as T
        }
        throw IllegalArgumentException("Unknown model class")
    }
}