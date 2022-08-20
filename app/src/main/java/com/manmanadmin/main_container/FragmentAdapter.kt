package com.manmanadmin.main_container

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.manmanadmin.finished.FinishedRequestsFragment
import com.manmanadmin.pending.PendingRequestsFragment
import com.manmanadmin.servers.ServersFragment
import com.manmanadmin.reviewed.ReviewedRequestsFragment

class FragmentAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity){
    override fun getItemCount(): Int {
        return 4
    }


    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> PendingRequestsFragment()
            1 -> ReviewedRequestsFragment()
            2 -> ServersFragment()
            3 -> FinishedRequestsFragment()
            else -> PendingRequestsFragment()
        }
    }

}