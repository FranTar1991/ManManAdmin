package com.manmanadmin.main_container

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.manmanadmin.R

class MainContainerViewModel(private val repo: MainContainerRepo, private val app: Application) : ViewModel() {
    private val _callMainActivity = MutableLiveData<Boolean>()
    val callMainActivity: LiveData<Boolean>
        get() = _callMainActivity

    private val _currentBusinessStatus = MediatorLiveData<String?>()
    val currentBusinessStatus: LiveData<String?>
        get() = _currentBusinessStatus

    private val _navigateToAddBusinessFragment = MediatorLiveData<Boolean>()
    val navigateToAddBusinessFragment: LiveData<Boolean>
        get() = _navigateToAddBusinessFragment

    private val _currentShift = MutableLiveData<String>()
    val currentShift : LiveData<String>
        get() = _currentShift

    init {
        repo.getBusinessCurrentStatus(_currentBusinessStatus)
        getCurrentShift()
    }

    fun setCallMainActivity(b: Boolean) {
        _callMainActivity.value = b
    }

    fun setNavigateToAddBusinessFragment(value: Boolean){
        _navigateToAddBusinessFragment.value = value
    }

     private fun getCurrentShift(){
        _currentShift.value = repo.getCurrentShift()
    }

    fun setNewShift(shift: String){
        _currentShift.value = repo.setNewShift(shift)
    }

}