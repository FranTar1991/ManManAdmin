package com.manmanadmin.reviewing.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException


@Suppress("unchecked_cast")
class CheckoutFragmentViewModelFactory(private val repository: CheckoutFragmentRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckoutFragmentViewModel::class.java)){
            return CheckoutFragmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown model class")
    }
}