package com.manmanadmin.main_container

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.manmanadmin.R
import com.manmanadmin.databinding.FragmentContainerBinding
import com.manmanadmin.main_activity.MainActivity
import com.manmanadmin.utils.showAlertDialog


class ContainerFragment : Fragment() {

    private lateinit var binding: FragmentContainerBinding
    private val viewModel: MainContainerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentContainerBinding.inflate(inflater,container,false)

        binding.myToolbar.inflateMenu(R.menu.menu)
        binding.myToolbar.setOnMenuItemClickListener {

            when(it.itemId){
                R.id.log_out_menu -> logUserOut()
                R.id.set_close_open_menu -> changeBusinessStatus()
                else -> true
            }

        }
        viewModel.callMainActivity.observe(viewLifecycleOwner){
            if (it){
                callMainActivityIntent()
                viewModel.setCallMainActivity(false)
            }
        }

        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager
        viewPager.adapter = activity?.let{FragmentAdapter(it)}
        TabLayoutMediator(tabLayout, viewPager){tab, position ->
            setTextToTab(tab, position)
        }.attach()


        return binding.root
    }

    private fun changeBusinessStatus(): Boolean {
        return true
    }

    private fun callMainActivityIntent() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
    }

    private fun logUserOut(): Boolean {

        showAlertDialog(getString(R.string.alert),
            getString( R.string.sure_of_log_out),activity,true,null){
            FirebaseAuth.getInstance().signOut()
            viewModel.setCallMainActivity(true)
        }?.show()

        return true
    }

    private fun setTextToTab(tab: TabLayout.Tab, position: Int) {
     tab.text =  when(position){
           0 -> getString(R.string.not_checked)
           1 -> getString(R.string.already_checked)
           2 -> getString(R.string.procesing)
           3 -> getString(R.string.finished_requests)
         else -> {""}
     }
    }

}