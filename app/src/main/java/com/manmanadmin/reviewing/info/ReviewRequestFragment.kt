package com.manmanadmin.reviewing.info

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.manmanadmin.R
import com.manmanadmin.databinding.FragmentReviewInfoBinding
import com.manmanadmin.utils.*


class ReviewRequestFragment : Fragment() {

    private var arrayOfServerNames: Array<String?>? = null
    private var requestToReview: RequestRemote? = null
    private var requestReference: DatabaseReference? = null
    private lateinit var binding: FragmentReviewInfoBinding
    private val args: ReviewRequestFragmentArgs by navArgs()
    private var currentRequest: ManManRequest? = null
    private lateinit var viewModel: ReviewRequestViewModel
    private lateinit var continueBtn: Button
    private lateinit var associatesSpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewInfoBinding.inflate(inflater,container,false)
        val repository = ReviewRequestRepository()
        val factory = ReviewRequestViewModelFactory(repository, requireActivity().application)
        viewModel = ViewModelProvider(this, factory)[ReviewRequestViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.executePendingBindings()
        associatesSpinner = binding.spinner

        continueBtn = binding.continueBtnToAddresses

        setContinueListener()
        setSaveAndExitListener()
        viewModel.setRequestRawInfo(args.request)

        viewModel.navigateToNextFragment.observe(viewLifecycleOwner){
            if (it){
                this.findNavController().navigate(ReviewRequestFragmentDirections.actionReviewRequestFragmentToTransactionAddressesFragment(currentRequest))
                viewModel.setNavigateToNextFragment(false)
            }
        }
        setToolbarUpFunction(binding.myToolbar)

        binding.clickToChatImg.setOnClickListener {
            viewModel.setCallWhatsappWithUserPhoneNumberLivedata(binding.phoneEt.text.toString())
        }

        binding.clickToChatToBusinessImg.setOnClickListener {
            viewModel.setCallWhatsappWithBusinessPhoneNumberLivedata(binding.businessPhoneEt.text.toString())
        }

        viewModel.callWhatsappWithUserPhoneNumberLivedata.observe(viewLifecycleOwner){
            it?.let{
                openWhatsAppWithNumber(it,context)
                viewModel.setCallWhatsappWithUserPhoneNumberLivedata(null)
            }
        }

        viewModel.callWhatsappWithBusinessPhoneNumberLivedata.observe(viewLifecycleOwner){
            it?.let{
                val information = getDataFromTheEditTexts()
                openWhatsAppWithInfo(it,information,context)
                viewModel.setCallWhatsappWithBusinessPhoneNumberLivedata(null)
            }
        }


        viewModel.requestRawInfo.observe(viewLifecycleOwner){
            it?.let {
                currentRequest = it
                 requestReference = it.user_id?.let { it1 -> getRequestReference(it.requestId!!, it1) }
                viewModel.setRequestListener(requestReference, it.comments)
                viewModel.setThisNodeReference(getThisNodeReference(it.requestId!!))
            }
        }

        viewModel.requestToReview.observe(viewLifecycleOwner){
            requestToReview = it
            setSpinnerSelection()
        }

        viewModel.navigateToMainFragment.observe(viewLifecycleOwner){
            if (it){
                this.findNavController().navigate(ReviewRequestFragmentDirections.actionReviewRequestFragmentToContainerFragment())
                viewModel.setNavigateToMainFragment(false)
            }
        }

        viewModel.setServersListener(FirebaseDatabase.getInstance().reference.child("servers"))

        viewModel.serversListener.observe(viewLifecycleOwner){
            it.toTypedArray().let { arrayResult ->
                arrayOfServerNames = arrayResult
                associatesSpinner.adapter = getSpinnerAdapter(arrayResult)

                setSpinnerSelection()
            }
        }

        return binding.root
    }

    private fun setSpinnerSelection(){
        requestToReview?.agentName?.let { name ->
            val namePosition = getNamePosition(name, arrayOfServerNames ?: emptyArray())
            associatesSpinner.setSelection(namePosition)
        }
    }

    private fun getNamePosition(associate: String?, deliveryGuyNames: Array<String?>): Int {
        var namePosition = 0
        for (nameIndex in deliveryGuyNames.indices){
            if (deliveryGuyNames[nameIndex]?.equals(associate) == true){
                namePosition = nameIndex
                break
            }
        }

        Log.i("MySelection", "$namePosition")
        return namePosition

    }

    private fun getSpinnerAdapter(arrayOfNames: Array<String?>): SpinnerAdapter {
        return ArrayAdapter(
            requireContext(),
            R.layout.spinner_item_layout, arrayOfNames
        )
    }

    private fun getDataFromTheEditTexts(): String {
       return getString(R.string.request_string_to_businesses,
        binding.nameEt.text.toString(),
        binding.phoneEt.text.toString(),
           binding.detailsEt.text.toString())
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
                    binding.phoneEt.text.toString(),
                    requestToReview?.comments,
                    associatesSpinner.selectedItem.toString())
            }


        }

    }


    private fun setSaveAndExitListener(){
        binding.saveExitBtn.setOnClickListener {
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
                    binding.phoneEt.text.toString(),
                    requestToReview?.comments,
                    associatesSpinner.selectedItem.toString(),
                    navigateToMainFragment =  true)
            }
        }
    }

}