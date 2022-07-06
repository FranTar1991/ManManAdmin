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
import com.manmanadmin.utils.*

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
        binding.viewModel = viewModel as ViewModelForAdapterInterface
        binding.viewModel2 = viewModel as PendingRequestsViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val query: Query = FirebaseDatabase.getInstance()
            .reference
            .child("all_requests_not_reviewed")

        val listener = OnManManRequestClickListener { _, request ->
            viewModel.setNavigateToReviewRequestFragment(request)
        }
        val adapter = setAdapter(query, listener, viewLifecycleOwner, viewModel)


        binding.fab.setOnClickListener {
            viewModel.setItemInsert(false)
            viewModel.setEmptyRequest(RequestLocal(title = "Admin/", price = -1.0, status = STATUS.Received.name, date = getDate(), userPhone = ""))
        }

        val requestRv = binding.allItemsRv
        requestRv.adapter = adapter
        requestRv.layoutManager = activity?.let { WrapContentLinearLayoutManager(it) }
        viewModel.navigateToReviewRequest.observe(viewLifecycleOwner){clickedRequest ->
            clickedRequest?.let{
              this.findNavController().navigate(ContainerFragmentDirections.actionContainerFragmentToReviewRequestFragment(clickedRequest))
                viewModel.setNavigateToReviewRequestFragment(null)
            }
        }

        return binding.root
    }

}