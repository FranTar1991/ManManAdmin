package com.manmanadmin.servers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class ServersViewModelFactory(private val repo: ServersRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServersViewModel::class.java)){
            return ServersViewModel(repo) as T
        }
        throw IllegalArgumentException("unknown model class")
    }
}