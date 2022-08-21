package com.manmanadmin.main_container.change_shift

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manmanadmin.R
import kotlinx.coroutines.launch

class ShiftChangerDialogViewModel(private val app: Application, private val sharedPreferences: SharedPreferences): ViewModel() {


    fun getSpinnerAdapter(): SpinnerAdapter {
        val adapter = ArrayAdapter.createFromResource(
            app,
            R.array.shift_options,
            android.R.layout.simple_spinner_item
        ).also { arrayAdapter ->
            arrayAdapter.setDropDownViewResource(R.layout.spinner_item_layout)
        }

        return adapter
    }

    fun getSelection(): Int {
        return  app.resources.getStringArray(R.array.shift_options).indexOfFirst { it == getCurrentShift() }
    }

    fun getCurrentShift(): String{

        return sharedPreferences.getString("turno","nothing") ?: ""
    }

}