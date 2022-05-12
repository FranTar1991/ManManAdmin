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
import com.manmanadmin.R
import com.manmanadmin.databinding.FragmentReviewInfoBinding
import com.manmanadmin.utils.*


class ReviewRequestFragment : Fragment() {


    private var requestToReview: RequestRemote? = null
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
        setToolbarUpFunction(binding.myToolbar)

        viewModel.requestRawInfo.observe(viewLifecycleOwner){
            it?.let {
                currentRequest = it
                 requestReference = it.user_id?.let { it1 -> getRequestReference(it.requestId!!, it1) }
                viewModel.setRequestListener(requestReference)
                viewModel.setThisNodeReference(getThisNodeReference(it.requestId!!))
            }
        }

        viewModel.requestToReview.observe(viewLifecycleOwner){
            requestToReview = it
        }

        viewModel.navigateToMainFragment.observe(viewLifecycleOwner){
            if (it){
                this.findNavController().navigate(ReviewRequestFragmentDirections.actionReviewRequestFragmentToContainerFragment())
                viewModel.setNavigateToMainFragment(false)
            }
        }
        return binding.root
    }



    private fun setContinueListener() {
        continueBtn.setOnClickListener {

            if (requestToReview?.status == STATUS.Canceled.name){

                showAlertDialog(getString(R.string.alert),getString(R.string.want_to_skip_request),activity,true,null){
                    currentRequest?.requestId?.let {
                        viewModel.deleteThisRequest()
                    }
                }?.show()
            } else if (requestToReview?.status == STATUS.Received.name){
                viewModel.updateRequestInfo(requestReference,
                    binding.detailsEt.text.toString(),
                    binding.titleEt.text.toString(),
                    binding.nameEt.text.toString(),
                    binding.phoneEt.text.toString())
            }


        }
    }

}