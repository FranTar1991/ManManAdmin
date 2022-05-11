package com.manmanadmin.finished

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.SnapshotParser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.manmanadmin.R
import com.manmanadmin.databinding.FragmentFinishedRequestsBinding
import com.manmanadmin.utils.*

class FinishedRequestsFragment : Fragment() {

    private lateinit var binding: FragmentFinishedRequestsBinding
    private lateinit var viewModel: FinishedRequestsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentFinishedRequestsBinding.inflate(inflater,container,false)

        val factory = FinishedRequestsViewModelFactory()
        viewModel = ViewModelProvider(this,factory).get(FinishedRequestsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        val query: Query = FirebaseDatabase.getInstance()
            .reference
            .child("data")
            .child("finished_requests")

        val listener = OnFinishedRequestClickListener { view, item ->
            showSnackbar(binding.root.rootView, getString(R.string.waiting_to_be_processed))
        }
        val adapter = setFinishedRequestAdapter(query,listener, viewLifecycleOwner,viewModel)

        val requestRv = binding.allRequestsRv
        requestRv.adapter = adapter
        requestRv.layoutManager = activity?.let { WrapContentLinearLayoutManager(it) }
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

        return AdapterForFinishedRequests(viewModel, options, onManManRequestClickListener)

    }

}