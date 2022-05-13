package com.manmanadmin.main_container

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class MainContainerViewModelFactory (private val repo: MainContainerRepo, private val app: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainContainerViewModel::class.java)){
            return MainContainerViewModel(repo, app) as T
        }
        throw IllegalArgumentException("Unknown model class")
    }
}