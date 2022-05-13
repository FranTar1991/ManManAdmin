package com.manmanadmin.change_business_status

import android.app.Application
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manmanadmin.R
import kotlinx.coroutines.launch

class ChangeBusinessStatusViewModel(private val repo: ChangeBusinessStatusRepo, private val app: Application):ViewModel() {

    private val _currentBusinessStatus = MediatorLiveData<String?>()
    val currentBusinessStatus: LiveData<String?>
    get() = _currentBusinessStatus

    private val _progressBarVisibility = MediatorLiveData<Int>()
    val progressBarVisibility: LiveData<Int>
        get() = _progressBarVisibility

    private val _businessStatusChanged = MediatorLiveData<Boolean?>()
    val businessStatusChanged: LiveData<Boolean?>
        get() = _businessStatusChanged

    init {
        repo.getBusinessCurrentStatus(_currentBusinessStatus)
        setProgressBarVisibility(View.GONE)
    }

     fun setProgressBarVisibility(visibility: Int){
        _progressBarVisibility.value = visibility
    }
    fun changeBusinessStatus(newStatus: String){
        _progressBarVisibility.value = View.VISIBLE
        viewModelScope.launch {
            repo.changeBusinessStatus(newStatus, _businessStatusChanged)
        }

    }

    fun getSpinnerAdapter(): SpinnerAdapter {
        val adapter = ArrayAdapter.createFromResource(
            app,
            R.array.busines_status_options,
            android.R.layout.simple_spinner_item
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(R.layout.spinner_item_layout)
        }

        return adapter
    }

}