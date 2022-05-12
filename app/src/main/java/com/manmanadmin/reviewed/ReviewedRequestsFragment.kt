package com.manmanadmin.reviewed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.manmanadmin.R
import com.manmanadmin.databinding.FragmentReviewedRequestsBinding
import com.manmanadmin.utils.*

class ReviewedRequestsFragment : Fragment() {

    private lateinit var binding: FragmentReviewedRequestsBinding
    private val viewModel: ReviewedRequestsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReviewedRequestsBinding.inflate(inflater,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val query: Query = FirebaseDatabase.getInstance()
            .reference
            .child("all_requests")

        val listener = OnManManRequestClickListener { view, item ->
            showSnackbar(binding.root.rootView, getString(R.string.waiting_to_be_processed))
        }
        val adapter = setAdapter(query,listener, viewLifecycleOwner,viewModel)

        val requestRv = binding.allRequestsRv
        requestRv.adapter = adapter
        requestRv.layoutManager = activity?.let { WrapContentLinearLayoutManager(it) }

        return binding.root
    }


}