package com.manmanadmin.reviewing.checkout

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.manmanadmin.R
import com.manmanadmin.databinding.FragmentCheckoutBinding
import com.manmanadmin.reviewing.transaction_checkout.CheckoutFragmentRepo
import com.manmanadmin.utils.*

class CheckoutFragment : Fragment() {
    private lateinit var binding: FragmentCheckoutBinding
    private lateinit var viewModel: CheckoutFragmentViewModel
    private lateinit var manManRequest: ManManRequest
    private val args: CheckoutFragmentArgs by navArgs()
    private lateinit var journey: Journey
    private lateinit var currentRequest: RequestLocal
    private var requestReference: DatabaseReference? = null
    private var thisNodeReference: DatabaseReference? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCheckoutBinding.inflate(inflater,container,false)


        val databaseReference = Firebase.database.reference
        val checkoutRepo = CheckoutFragmentRepo(databaseReference,FirebaseAuth.getInstance().currentUser, context)
        val factory = CheckoutFragmentViewModelFactory(checkoutRepo)
        viewModel = ViewModelProvider(this,factory)[CheckoutFragmentViewModel::class.java]

        viewModel.setTransactionItem(args.currentTransactionItem)
        viewModel.setRequestRawInfo(args.manManRequest)

        viewModel.transactionItem.observe(viewLifecycleOwner){ transaction ->
              currentRequest = transaction
                dealWithPrice()
        }

        viewModel.journeyLiveData.observe(viewLifecycleOwner){
            it?.let {
                this.journey = it
               viewModel.updatePriceList()
            }
        }

        viewModel.listOfUpdatedPrices.observe(viewLifecycleOwner){
            it?.let {
              viewModel.getPriceForCurrentRequest(journey, it)
            }
        }

        viewModel.requestRawInfo.observe(viewLifecycleOwner){
            it?.let {
                manManRequest = it
                requestReference = it.user_id?.let { it1 -> getRequestReference(it.requestId!!, it1) }
                thisNodeReference = it.requestId?.let { it1 -> getThisNodeReference(it1) }
            }
        }


        binding.updateRequestBtn.setOnClickListener {
            currentRequest.price = if(binding.priceEt.text.toString() != ""){
                binding.priceEt.text.toString().toDouble()
            }else{
                currentRequest.price

            }
            currentRequest.comments = binding.commentsTxt.text.toString()
            viewModel.updateRequest(requestReference,thisNodeReference,manManRequest,currentRequest)
        }

        binding.updateSendRequestBtn.setOnClickListener {
            if(checkDataIsComplete(it as Button, getString(R.string.update_send_request))){
                currentRequest.price = binding.priceEt.text.toString().toDouble()
                currentRequest.comments = binding.commentsTxt.text.toString()
                viewModel.updateAndSendRequest(requestReference,thisNodeReference ,currentRequest)
            }
        }



        viewModel.writeToDataBaseStatus.observe(viewLifecycleOwner){
            when(it){
               GeneralStatus.success-> {
                val dialog =   showAlertDialog(getString(R.string.alert),getString(R.string.request_updated),activity, false){
                       viewModel.setNavigateToMainFragment(true)
                   }

                   dialog?.apply {
                       setCancelable(false)
                       show()
                   }
               }
                else -> {
                    Toast.makeText(context, "something´s wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }


        setToolbarUpFunction(this, context ,binding.includedToolbarLayout, viewModel)

        viewModel.toolbarTextLiveData.observe(viewLifecycleOwner){
            binding.includedToolbarLayout.appTitle.text = it
        }

        viewModel.navigateToMainFragment.observe(viewLifecycleOwner){
            if (it){
                this.findNavController().navigate(CheckoutFragmentDirections.actionCheckoutFragmentToContainerFragment())
                viewModel.setNavigateToMainFragment(false)
            }
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    private fun dealWithPrice() {
        if (currentRequest.price != -1.0){
            viewModel.setPriceForThisRequest(currentRequest.price)
        }else{
            viewModel.getJourney(currentRequest, context)
        }
    }

    private fun checkDataIsComplete(button: Button, text: String): Boolean {
        updateButtonUi(button)
       return if (binding.priceEt.text.toString() == ""){
            showSnackbar(binding.root.rootView,getString(R.string.add_price))
           button.isEnabled = true
            button.text = text
           false
        } else {
            true
       }
    }

    private fun updateButtonUi(it: View) {
        it.isEnabled = false
        (it as Button).text = getString(R.string.sending_request)
    }

}