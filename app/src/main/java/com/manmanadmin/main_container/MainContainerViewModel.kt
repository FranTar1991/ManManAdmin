package com.manmanadmin.main_container

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainContainerViewModel(): ViewModel() {
    private val _callMainActivity = MutableLiveData<Boolean>()
    val callMainActivity: LiveData<Boolean>
        get() = _callMainActivity

    fun setCallMainActivity(b: Boolean) {
        _callMainActivity.value = b
    }
}