package com.manmanadmin.utils

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.manmanadmin.reviewing.checkout.CheckoutFragment
import com.manmanadmin.R
import com.manmanadmin.reviewing.addresses.ReviewAddressFragment
import com.manmanadmin.reviewing.info.ReviewRequestFragment

interface MyViewModel {
    var _toolbarTextLiveData: MutableLiveData<String>
    val toolbarTextLiveData: LiveData<String>
        get() = _toolbarTextLiveData

    fun setNavigateToMainFragment(value: Boolean) {}

    fun setToolBarText(fragment: Fragment, context: Context){
        _toolbarTextLiveData.value =  when(fragment){
            is ReviewRequestFragment -> {context.getString(R.string.add_details)}
            is CheckoutFragment ->{context.getString(R.string.checkout)}
            is ReviewAddressFragment ->{context.getString(R.string.add_address)}
            else -> {""}
        }
    }


}