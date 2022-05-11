package com.manmanadmin.pending

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.SnapshotParser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.manmanadmin.databinding.FragmentPendingRequestsBinding
import com.manmanadmin.main_container.ContainerFragmentDirections
import com.manmanadmin.utils.ManManRequest
import com.manmanadmin.utils.WrapContentLinearLayoutManager

class PendingRequestsFragment : Fragment() {

    private lateinit var binding: FragmentPendingRequestsBinding
    private lateinit var viewModel: PendingRequestsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentPendingRequestsBinding.inflate(inflater,container,false)

        val factory = PendingRequestsViewModelFactory()
        viewModel = ViewModelProvider(this,factory)[PendingRequestsViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setAdapter()

        viewModel.navigateToReviewRequest.observe(viewLifecycleOwner){clickedRequest ->
            clickedRequest?.let{
              this.findNavController().navigate(ContainerFragmentDirections.actionContainerFragmentToReviewRequestFragment(clickedRequest))
                viewModel.setNavigateToReviewRequestFragment(null)
            }
        }

        return binding.root
    }

    private fun setAdapter() {
        val query: Query = FirebaseDatabase.getInstance()
            .reference
            .child("all_requests_not_reviewed")

        val snapshotParser = SnapshotParser<ManManRequest> { snapshot ->
            val requestId = snapshot.key
            val userId = snapshot.child("user_id").getValue(String::class.java)
            val status = snapshot.child("status").getValue(String::class.java)

            ManManRequest(requestId = requestId, user_id = userId, status =  status)
        }

        val options: FirebaseRecyclerOptions<ManManRequest> = FirebaseRecyclerOptions.Builder<ManManRequest>()
            .setQuery(query,snapshotParser)
            .setLifecycleOwner(viewLifecycleOwner)
            .build()

        val adapter = PendingRequestsAdapter(viewModel, options, OnManManRequestClickListener { view, request ->
            viewModel.setNavigateToReviewRequestFragment(request)
        })
        val requestRv = binding.allRequestsRv
        requestRv.adapter = adapter
        requestRv.layoutManager = activity?.let { WrapContentLinearLayoutManager(it) }
        binding.lifecycleOwner = viewLifecycleOwner
    }

}