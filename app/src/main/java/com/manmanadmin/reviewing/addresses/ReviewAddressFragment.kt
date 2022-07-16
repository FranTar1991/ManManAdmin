package com.manmanadmin.reviewing.addresses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.database.DatabaseReference
import com.manmanadmin.R
import com.manmanadmin.databinding.FragmentReviewAddressBinding
import com.manmanadmin.utils.*


class ReviewAddressFragment : Fragment() {
    private lateinit var binding: FragmentReviewAddressBinding
    private lateinit var viewModel: ReviewAddressViewModel

    private val args: ReviewAddressFragmentArgs by navArgs()
    private lateinit var addAnotherAddressChk: CheckBox
    private var currentTransactionItem: RequestLocal? = null
    private var requestReference: DatabaseReference? = null
    private var currentRequest: ManManRequest? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewAddressBinding.inflate(inflater,container,false)

        val factory = ReviewAddressViewModelFactory(ReviewAddressRepository())
        viewModel = ViewModelProvider(this, factory).get(ReviewAddressViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.setRequestRawInfo(args.request)

        setMyToolbar()


        viewModel.requestRawInfo.observe(viewLifecycleOwner){
            it?.let {
                currentRequest = it
                requestReference = it.user_id?.let { it1 -> getRequestReference(it.requestId!!, it1) }
                viewModel.setRequestListener(requestReference)
            }
        }

        //This gets called first, if we´re coming from the ReviewRequestInfo fragment, this will be null

            currentTransactionItem =  args.currentTransactionItem
            viewModel.updateTransactionItem(currentTransactionItem)

        viewModel.requestToReview.observe(viewLifecycleOwner){remoteRequest ->

            remoteRequest?.let {
                if (currentTransactionItem == null){
                    currentTransactionItem = viewModel.fromRemoteToLocal(it)
                    viewModel.updateTransactionItem(currentTransactionItem)
                }
            }


        }



        viewModel.userAddressBitmapLiveData.observe(viewLifecycleOwner){
            it?.let{
               binding.userAddressLayout.addressImg.setImageBitmap(it)
            }

        }
        viewModel.locationBAddressBitmapLiveData.observe(viewLifecycleOwner){
            it?.let {
                binding.userAddressLayout.addressImg.setImageBitmap(it)
            }
        }

        binding.userAddressLayout.mainContraintLayout.setOnClickListener {
            this.findNavController().
            navigate(ReviewAddressFragmentDirections.
            actionTransactionAddressesFragmentToMapsFragment(
                SelectedLocation(currentTransactionItem?.userAddress),
                USER_DELIVERY_ADDRESS,
                currentTransactionItem,
                currentRequest))
        }
        binding.locationBAddressLayout.mainContraintLayout.setOnClickListener {
            this.findNavController().
            navigate(ReviewAddressFragmentDirections.
            actionTransactionAddressesFragmentToMapsFragment(SelectedLocation(currentTransactionItem?.locationBAddress),
                USER_PICK_UP_ADDRESS,
                currentTransactionItem,
                currentRequest))
        }

        binding.userAddressReference.addTextChangedListener {
            currentTransactionItem?.userAddressReference = it.toString()
            viewModel.updateTransactionItem(currentTransactionItem)
        }

        binding.continueBtn.setOnClickListener {
            if (currentTransactionItem?.userAddressReference == null){
                showSnackbar(binding.root.rootView, getString(R.string.select_your_location))
                context?.let {
                    binding.userAddressLayout.emptyViewLayout.setBackgroundColor(ContextCompat.getColor(it,R.color.pending_color));
                return@setOnClickListener
                }
            }

            viewModel.setNavigateToCheckoutFragment(true)

        }
        viewModel.navigateToCheckoutFragment.observe(viewLifecycleOwner){
            if(it){
                checkIfUserWantsAnotherAddressAdded()
                this.findNavController()
                    .navigate(ReviewAddressFragmentDirections
                        .actionTransactionAddressesFragmentToCheckoutFragment(currentTransactionItem,currentRequest))
                viewModel.setNavigateToCheckoutFragment(false)
            }
        }

        viewModel.navigateToMainF.observe(viewLifecycleOwner){
            if (it){
                this.findNavController().navigate(ReviewAddressFragmentDirections.actionTransactionAddressesFragmentToContainerFragment())
                viewModel.setNavigateToMainFragment(false)
            }
        }



        viewModel.transactionItem.observe(viewLifecycleOwner){

            setTheUserAddressLayout(it)

            setTheLocationBLayout(it)
            addAnotherAddressChk.isChecked = currentTransactionItem?.locationBAddress != null
        }


        addAnotherAddressChk = binding.addAnotherAddressChk


        return binding.root
    }


    private fun setMyToolbar() {

        setToolbarUpFunction(this, context ,binding.includedToolbarLayout, viewModel)
        viewModel.toolbarTextLiveData.observe(viewLifecycleOwner){
            binding.includedToolbarLayout.appTitle.text = it
        }
    }

    private fun checkIfUserWantsAnotherAddressAdded() {
        if (!addAnotherAddressChk.isChecked){
            currentTransactionItem?.apply{
                locationBAddress = null
                locationBAddressReference = null
            }
        }
    }

    private fun setTheUserAddressLayout(transactionItemLocal: RequestLocal?) {
        val mainLayout = binding.userAddressLayout
        val currentTransactionAddress = currentTransactionItem?.userAddress

        mainLayout.addressTitle.text = getString(R.string.user_location)
        mainLayout.emptyTxtTitle.text = getString(R.string.this_is_required)
        mainLayout.explanationTxt.text = getString(R.string.explain_why_permission)

        transactionItemLocal?.userAddressBitmap?.apply {
                mainLayout.addressImg.setImageBitmap(this)
            }
            mainLayout.addressReference.text = transactionItemLocal?.userAddressReference
            mainLayout.addressBitmapLayout.visibility=  if (currentTransactionAddress != null) {
                mainLayout.emptyViewLayout.visibility = View.GONE
                View.VISIBLE
            } else {
                mainLayout.emptyViewLayout.visibility = View.VISIBLE
                View.GONE
            }
    }


    private fun setTheLocationBLayout(transactionItemLocal: RequestLocal?) {
        val mainLayout = binding.locationBAddressLayout
        val currentTransactionAddress = currentTransactionItem?.locationBAddress

        transactionItemLocal?.locationBAddressBitmap?.apply {
               mainLayout.addressImg.setImageBitmap(this)
           }
            mainLayout.addressReference.text = transactionItemLocal?.locationBAddressReference
            mainLayout.addressBitmapLayout.visibility=   if (currentTransactionAddress != null) {
                mainLayout.emptyViewLayout.visibility = View.GONE
                View.VISIBLE
            } else {
                mainLayout.emptyViewLayout.visibility = View.VISIBLE
                View.GONE
            }

    }

}