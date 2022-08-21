package com.manmanadmin.main_container

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.manmanadmin.R
import com.manmanadmin.databinding.FragmentContainerBinding
import com.manmanadmin.main_activity.MainActivity
import com.manmanadmin.main_container.change_shift.ShiftChangerDialog
import com.manmanadmin.utils.callBusinessStatusChangerDialog
import com.manmanadmin.utils.callShiftChangerDialog
import com.manmanadmin.utils.getDate
import com.manmanadmin.utils.showAlertDialog


class ContainerFragment : Fragment(), ShiftChangerDialog.OnShiftChanged {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: FragmentContainerBinding
    private lateinit var viewModel: MainContainerViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentContainerBinding.inflate(inflater,container,false)
        sharedPreferences = requireActivity().getSharedPreferences("my_prefs", MODE_PRIVATE )
        binding.myToolbar.inflateMenu(R.menu.menu)
        binding.myToolbar.setOnMenuItemClickListener {

            when(it.itemId){
                R.id.log_out_menu -> logUserOut()
                R.id.set_close_open_menu -> callBusinessStatusChangerDialog(requireActivity(), childFragmentManager)
                R.id.add_business_menu ->  {viewModel.setNavigateToAddBusinessFragment(true); true}
                else -> true
            }

        }

        binding.appTitle.setOnClickListener {
            callBusinessStatusChangerDialog(requireActivity(), childFragmentManager)
        }

        binding.shiftChanger.setOnClickListener{
            callShiftChangerDialog(requireActivity(), childFragmentManager, sharedPreferences, this)
        }

        val reference = FirebaseDatabase.getInstance().reference.child("data").child("status_service").child("status")
        val repo = MainContainerRepo(reference, sharedPreferences)
        val factory = MainContainerViewModelFactory(repo, requireNotNull(activity).application)
        viewModel = ViewModelProvider(this, factory)[MainContainerViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        viewModel.callMainActivity.observe(viewLifecycleOwner){
            if (it){
                callMainActivityIntent()
                viewModel.setCallMainActivity(false)
            }
        }


        viewModel.navigateToAddBusinessFragment.observe(viewLifecycleOwner){
            if (it == true){
                this.findNavController().navigate(R.id.addBusinessFragment)
                viewModel.setNavigateToAddBusinessFragment(false)
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
           2 -> getString(R.string.servers)
           3 -> getString(R.string.finished_requests)
         else -> {""}
     }
    }

    override fun onNewShiftSelected(shift: String) {
        viewModel.setNewShift(shift)
    }

}