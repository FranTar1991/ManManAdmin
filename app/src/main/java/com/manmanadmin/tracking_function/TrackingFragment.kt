package com.manmanadmin.tracking_function

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.manmanadmin.R
import com.manmanadmin.databinding.FragmentTrackingBinding
import com.manmanadmin.utils.initMap
import com.manmanadmin.utils.setToolbarUpFunction


class TrackingFragment : Fragment() {
    private lateinit var myMap: GoogleMap
    private lateinit var viewModel: TrackingViewModel
    private val args: TrackingFragmentArgs by navArgs()

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        myMap = googleMap
        viewModel.setMyGoogleMap(googleMap)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       val binding = FragmentTrackingBinding.inflate(layoutInflater,container,false)
        setToolbarUpFunction(binding.toolBarDetails)
        val transactionItemPassed = args.remoteRequest
        val mmServer = args.server

        val databaseReference = Firebase.database.reference.child("users").child(mmServer?.currentUserId!!).child("requests")
        val repo = TrackingRepo(databaseReference)
        val factory = TrackingViewModelFactory(repo)
        viewModel = ViewModelProvider(this,factory)[TrackingViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.setServer(mmServer)
        viewModel.myGoogleMap.observe(viewLifecycleOwner, Observer { map->
            viewModel.updateMapCamera()
            context?.let { viewModel.setMarker(it) }
        })

        viewModel.updatedRequestLocation.observe(viewLifecycleOwner){newLocation ->
            context?.let { viewModel.updateTheMarkerPosition(newLocation) }
        }

        viewModel.rawTransaction.observe(viewLifecycleOwner){ server ->
            server?.currentRequestId?.let {
                viewModel.getUpdatedLatLng(it)
            }
        }

        viewModel.setCurrentTransaction(transactionItemPassed)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap(R.id.tracking_map,childFragmentManager,callback)
    }

}