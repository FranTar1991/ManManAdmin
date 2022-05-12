package com.manmanadmin.finished

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.manmanadmin.R
import com.manmanadmin.databinding.FragmentFinishedRequestsBinding
import com.manmanadmin.main_container.ContainerFragmentDirections
import com.manmanadmin.utils.*
import java.util.*

class FinishedRequestsFragment : Fragment() {

    private lateinit var adapter: AdapterForFinishedRequests
    private lateinit var binding: FragmentFinishedRequestsBinding
    private lateinit var viewModel: FinishedRequestsViewModel
    private lateinit var bottomBar: BottomAppBar
    private lateinit var finishedRequestReference: DatabaseReference
    private lateinit var mainQuery: Query

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentFinishedRequestsBinding.inflate(inflater,container,false)

        val factory = FinishedRequestsViewModelFactory()
        viewModel = ViewModelProvider(this,factory).get(FinishedRequestsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        bottomBar = binding.bottomAppBar
        setBottomBarListeners()

        finishedRequestReference = FirebaseDatabase.getInstance().reference.child("data").child("finished_requests")

        val listener = OnFinishedRequestClickListener { view, item ->
            viewModel.setNavigateToFinishedRequestWithDetailsFragment(item)
        }


        viewModel.navigateToFinishedRequestWithDetailsFragment.observe(viewLifecycleOwner){
            it?.let {
                this.findNavController().navigate(ContainerFragmentDirections.actionContainerFragmentToRequestWithAllDetailsFragment(it))
                viewModel.setNavigateToFinishedRequestWithDetailsFragment(null)
            }
        }

        val requestRv = binding.allRequestsRv
        requestRv.layoutManager = activity?.let { WrapContentLinearLayoutManager(it) }
        mainQuery = FirebaseDatabase.getInstance()
            .reference
            .child("data")
            .child("finished_requests")



        viewModel.setQuery(mainQuery)

        viewModel.adapterQuery.observe(viewLifecycleOwner){

            adapter = setFinishedRequestAdapter(it,listener, viewLifecycleOwner,viewModel)
            requestRv.adapter = adapter
        }



        viewModel.itemsDeleted.observe(viewLifecycleOwner){
            if (it){
                showSnackbar(binding.root.rootView, getString(R.string.items_deleted))
            }
        }

        return binding.root
    }

    private fun setFinishedRequestAdapter(query: Query,
                   onManManRequestClickListener: OnFinishedRequestClickListener,
                   viewLifecycleOwner: LifecycleOwner,
                   viewModel: FinishedRequestsViewModel
    ): AdapterForFinishedRequests {


        val options: FirebaseRecyclerOptions<RequestRemote> = FirebaseRecyclerOptions.Builder<RequestRemote>()
            .setQuery(query,RequestRemote::class.java)
            .setLifecycleOwner(viewLifecycleOwner)
            .build()

        return AdapterForFinishedRequests(viewModel, activity,options,  onManManRequestClickListener)

    }

    private fun setBottomBarListeners() {

        setFilter(bottomBar.menu.findItem(R.id.filter_in_menu))

        bottomBar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.delete_all_in_menu -> deleteAllRequests()
                else ->{true}
            }
        }
    }



    private fun setFilter(menuItem: MenuItem): Boolean {
        val filterView = menuItem.actionView as SearchView
        filterView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterRequestsWithText(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterRequestsWithText(newText)
                return false
            }
        })
        return true
    }

    private fun filterRequestsWithText(newText: String?) {

        val newQuery: Query = mainQuery.orderByChild("agentName").startAt(newText).endAt(newText+ "uf8ff")

        viewModel.setQuery(newQuery)
    }

    private fun deleteAllRequests(): Boolean {
        showAlertDialog(getString(R.string.alert),getString(R.string.want_to_delete_all),activity,true,null){
            viewModel.removeAllRequests(finishedRequestReference)
        }?.show()

        return true
    }

}