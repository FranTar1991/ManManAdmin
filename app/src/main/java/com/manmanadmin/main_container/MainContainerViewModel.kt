package com.manmanadmin.main_container

import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainContainerViewModel(private val repo: MainContainerRepo, private val app: Application) : ViewModel() {
    private val _callMainActivity = MutableLiveData<Boolean>()
    val callMainActivity: LiveData<Boolean>
        get() = _callMainActivity

    private val _currentBusinessStatus = MediatorLiveData<String?>()
    val currentBusinessStatus: LiveData<String?>
        get() = _currentBusinessStatus

    init {
        repo.getBusinessCurrentStatus(_currentBusinessStatus)
    }

    fun setCallMainActivity(b: Boolean) {
        _callMainActivity.value = b
    }


}