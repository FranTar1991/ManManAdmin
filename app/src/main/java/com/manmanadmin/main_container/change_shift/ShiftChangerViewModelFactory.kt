package com.manmanadmin.main_container.change_shift

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ShiftChangerViewModelFactory(
    private val app: Application,
    private val sharedPreferences: SharedPreferences
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShiftChangerDialogViewModel::class.java)){
            return ShiftChangerDialogViewModel(app, sharedPreferences) as T
        }
        throw IllegalArgumentException("Unknown model class")
    }
}