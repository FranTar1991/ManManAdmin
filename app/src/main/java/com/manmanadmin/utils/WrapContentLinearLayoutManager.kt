package com.manmanadmin.utils

import android.app.Activity
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class WrapContentLinearLayoutManager @JvmOverloads constructor (activity: Activity, orientation: Int = RecyclerView.VERTICAL, reverseLayout: Boolean = false): LinearLayoutManager(activity,orientation, reverseLayout) {
    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {

        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            Log.e("TAG", "meet a IOOBE in RecyclerView")
        }
    }
}