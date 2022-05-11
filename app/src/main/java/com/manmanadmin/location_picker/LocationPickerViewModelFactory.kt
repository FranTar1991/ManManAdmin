package com.manchasdelivery.location_picker

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.manmanadmin.location_picker.LocationPickerViewModel

class LocationPickerViewModelFactory (private val application: Application) : ViewModelProvider.Factory {


    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationPickerViewModel::class.java)) {
            return LocationPickerViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}