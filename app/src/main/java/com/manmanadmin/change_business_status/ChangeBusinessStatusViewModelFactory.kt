package com.manmanadmin.change_business_status

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ChangeBusinessStatusViewModelFactory(private val repo: ChangeBusinessStatusRepo, private val app: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChangeBusinessStatusViewModel::class.java)){
            return ChangeBusinessStatusViewModel(repo, app) as T
        }
        throw IllegalArgumentException("Unknown model class")
    }
}