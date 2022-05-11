package com.manmanadmin.reviewing.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.DatabaseReference
import com.manmanadmin.databinding.FragmentReviewInfoBinding

import com.manmanadmin.utils.ManManRequest
import com.manmanadmin.utils.getRequestReference
import com.manmanadmin.utils.setToolbarUpFunction


class ReviewRequestFragment : Fragment() {

    private var requestReference: DatabaseReference? = null
    private lateinit var binding: FragmentReviewInfoBinding
    private val args: ReviewRequestFragmentArgs by navArgs()
    private var currentRequest: ManManRequest? = null
    private lateinit var viewModel: ReviewRequestViewModel
    private lateinit var continueBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewInfoBinding.inflate(inflater,container,false)
        val repository = ReviewRequestRepository()
        val factory = ReviewRequestViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ReviewRequestViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()

        continueBtn = binding.continueBtnToAddresses

        setContinueListener()
        viewModel.setRequestRawInfo(args.request)

        viewModel.navigateToNextFragment.observe(viewLifecycleOwner){
            if (it){
                this.findNavController().navigate(ReviewRequestFragmentDirections.actionReviewRequestFragmentToTransactionAddressesFragment(currentRequest))
                viewModel.setNavigateToNextFragment(false)
            }
        }
        setToolbarUpFunction(this, context ,binding.includedToolbarLayout, viewModel)

        viewModel.toolbarTextLiveData.observe(viewLifecycleOwner){
            binding.includedToolbarLayout.appTitle.text = it
        }


        viewModel.requestRawInfo.observe(viewLifecycleOwner){
            it?.let {
                currentRequest = it
                 requestReference = it.user_id?.let { it1 -> getRequestReference(it.requestId!!, it1) }
                viewModel.setRequestListener(requestReference)
            }
        }

        return binding.root
    }



    private fun setContinueListener() {
        continueBtn.setOnClickListener {
            viewModel.updateRequestInfo(requestReference,binding.detailsEt.text.toString(), binding.titleEt.text.toString(), binding.nameEt.text.toString(), binding.phoneEt.text.toString())
        }
    }

}