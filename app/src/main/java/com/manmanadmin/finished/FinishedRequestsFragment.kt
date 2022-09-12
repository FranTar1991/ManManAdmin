package com.manmanadmin.finished

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.manmanadmin.R
import com.manmanadmin.databinding.FragmentFinishedRequestsBinding
import com.manmanadmin.finished.adapters.AdapterForFinishedRequests2
import com.manmanadmin.finished.adapters.OnFinishedRequestClickListener2
import com.manmanadmin.main_container.ContainerFragmentDirections
import com.manmanadmin.utils.*

class FinishedRequestsFragment : Fragment() {

    private var adapter2: AdapterForFinishedRequests2? = null


    private lateinit var binding: FragmentFinishedRequestsBinding
    private lateinit var viewModel: FinishedRequestsViewModel
    private lateinit var bottomBar: BottomAppBar
    private lateinit var finishedRequestReference: DatabaseReference
    private var addExpenseDialog: AddExpenseDialogFragment? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentFinishedRequestsBinding.inflate(inflater,container,false)

        finishedRequestReference = FirebaseDatabase.getInstance().reference.child("data").child("finished_requests")
        val repo = FinishedRequestsRepo(finishedRequestReference)

        val factory = FinishedRequestsViewModelFactory(repo)
        viewModel = ViewModelProvider(this,factory)[FinishedRequestsViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        bottomBar = binding.bottomAppBar
        setBottomBarListeners()


        val listener = OnFinishedRequestClickListener2 { view, item ->
            viewModel.setNavigateToFinishedRequestWithDetailsFragment(item)
        }

        addExpenseDialog = AddExpenseDialogFragment(viewModel)


        viewModel.navigateToFinishedRequestWithDetailsFragment.observe(viewLifecycleOwner){
            it?.let {
                this.findNavController().navigate(ContainerFragmentDirections.actionContainerFragmentToRequestWithAllDetailsFragment(it))
                viewModel.setNavigateToFinishedRequestWithDetailsFragment(null)
            }
        }

        val requestRv = binding.allItemsRv
        setAdapterForFinishedRequests(requestRv, listener)

        viewModel.fetchNewFinishedRequests(finishedRequestReference)

        viewModel.itemsDeleted.observe(viewLifecycleOwner){
            if (it){
                showSnackbar(binding.root.rootView, getString(R.string.items_deleted))
            }
        }

        viewModel.transactionAddedLiveData.observe(viewLifecycleOwner){
            addExpenseDialog?.dismiss()
            if (it == GeneralStatus.success){
                showSnackbar(binding.root.rootView,"Se agregó con éxito")
            }else{
                showSnackbar(binding.root.rootView,"No se guardó")
            }
        }


        viewModel.itemDeletedCallback.observe(viewLifecycleOwner){
            if (it){
                showSnackbar(binding.root.rootView,getString(R.string.item_deleted))
            }
        }

        viewModel.dateToFilter.observe(viewLifecycleOwner){ dateRequired ->
            dateRequired?.let {
                viewModel.filterRequestsByDate(it)
            }
        }

        viewModel.allRequestsFinished.observe(viewLifecycleOwner){
            it?.let {
                Toast.makeText(context,"${it.size} elementos encontrados", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    private fun setAdapterForFinishedRequests(
        recyclerView: RecyclerView,
        listener: OnFinishedRequestClickListener2
    ) {

        adapter2 = context?.let { AdapterForFinishedRequests2(it,activity,viewModel, listener) }
        val linerLayoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter2
        recyclerView.layoutManager = linerLayoutManager
    }

    private fun setBottomBarListeners() {

        setFilter(bottomBar.menu.findItem(R.id.filter_in_menu))

        bottomBar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.add_expense_in_menu -> callAddExpenseDialog()
                else ->{true}
            }
        }
    }
    private fun setFilter(menuItem: MenuItem): Boolean {
        val filterView = menuItem.actionView as SearchView
        filterView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterRequestsWithDate(query.toString())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               newText?.let {
                   if (newText.isEmpty()) filterRequestsWithDate(newText)
               }
                return false
            }
        })
        return true
    }
    private fun filterRequestsWithDate(date: String) {

        if (date.isEmpty()){
           viewModel.fetchNewFinishedRequests(finishedRequestReference)
        }else{
            viewModel.setDateToFilter(date)
        }

    }

    private fun callAddExpenseDialog(): Boolean {
        addExpenseDialog?.show(childFragmentManager,"add_expense_dialog")
        return true
    }

}