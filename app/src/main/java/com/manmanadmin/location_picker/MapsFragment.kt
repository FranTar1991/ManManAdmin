package com.manmanadmin.location_picker

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.manchasdelivery.location_picker.LocationPickerViewModelFactory
import com.manmanadmin.R
import com.manmanadmin.add_business.Business
import com.manmanadmin.add_business.BusinessInfoAdapter
import com.manmanadmin.databinding.LocationPickerBinding
import com.manmanadmin.utils.*


class MapsFragment : Fragment(){

    private var isFirstCall: Boolean = true
    private var callingFragment: String? = null
    private var currentTransactionItem: RequestLocal? = null
    private lateinit var viewModel: LocationPickerViewModel

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    private lateinit var spinner: Spinner
    private var locationSelected: LatLng? = null
    private var locationPassed: LatLng? = null
    private var myMap: GoogleMap? = null
    private lateinit var binding: LocationPickerBinding
    private lateinit var referenceEt: EditText
    private lateinit var lisOfBusiness: MutableList<Business>


    private val args: MapsFragmentArgs by navArgs()

    @SuppressLint("MissingPermission")
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
           viewModel.setIsLocationPermissionGranted(isGranted)
        }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        myMap = googleMap
        viewModel.setMyGoogleMap(googleMap)
        googleMap.setOnMyLocationButtonClickListener {
            if (!checkIfGpsIsOn(context)) {
                showAlertDialog(
                    getString(R.string.gps_title),
                    getString(R.string.turn_on_gps),
                    requireActivity()){
                    callGPSPageOnSettings(context)
                }?.show()

            } else {
                binding.loadingView.isVisible = true
                fusedLocationProviderClient?.let { viewModel.getDeviceLocation(it,requireActivity(),true) }
            }

            false
        }
        googleMap.setOnCameraMoveStartedListener { spinner.setSelection(0) }

    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding = LocationPickerBinding.inflate(inflater, container, false)
        val application = requireNotNull(this.activity).application
        val viewModelFactory = LocationPickerViewModelFactory(application)
        viewModel =ViewModelProvider(this, viewModelFactory).get(LocationPickerViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = activity
        spinner = binding.citiesSpinner

        referenceEt = binding.referenceEt

        viewModel.setCurrentTransactionItem(args.currentTransactionItem)
        viewModel.currentTransactionItem.observe(viewLifecycleOwner){
            currentTransactionItem = it
        }

        viewModel.businessArrayLiveData.observe(viewLifecycleOwner){
            it?.let {

                lisOfBusiness = it
                Log.i("MyBusinessItem","${lisOfBusiness} ")
                setSearchCoordinateView()

            }
        }

        callingFragment = args.callingFragment
        viewModel.setCallingFragment(args.callingFragment)
        viewModel.callingF.observe(viewLifecycleOwner){
            callingFragment = it

          viewModel.setReferenceToShow(when(callingFragment){
                USER_DELIVERY_ADDRESS ->{currentTransactionItem?.userAddressReference}
                USER_PICK_UP_ADDRESS -> {currentTransactionItem?.locationBAddressReference}
              else -> {"Not defined"}
          }
          )

        }


        binding.saveLocationBtn.setOnClickListener {

            if (referenceEt.text.toString() == ""){
                showSnackbar(binding.root.rootView,getString(R.string.please_enter_reference))
                referenceEt.requestFocus()
                return@setOnClickListener
            }

            locationSelected?.let { location ->
                myMap?.addMarker(MarkerOptions().position(location)
                    .icon(context?.let { ctx -> getBitmapFromVector(ctx, R.drawable.map_marker) }))
            }
           getMapScreenshot()


        }

        locationPassed = args.location?.location
        viewModel.myGoogleMap.observe(viewLifecycleOwner, Observer { map->
            getLocationPermission()
        })

        viewModel.isLocationPermissionGranted.observe(viewLifecycleOwner, Observer { isGranted ->
            if (isGranted) {
                if (locationPassed != null){
                        viewModel.apply {
                            setLocationSelected(locationPassed!!)
                            updateUi()
                        }

                }else{
                    fusedLocationProviderClient?.let {
                        viewModel.getDeviceLocation(it,requireActivity())
                    }
                }


            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
            }
            context?.let { viewModel.setMainMarker(it) }

        })


        viewModel.locationSelected.observe(viewLifecycleOwner, Observer {lastKnownLocation ->
            locationSelected = lastKnownLocation
            binding.loadingView.isVisible = false
            if (isFirstCall){
                viewModel.updateMapCamera(LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude)
                    , ZOOM_BUILDING_LEVEL.toFloat())
                isFirstCall = false
            }else{
                viewModel.updateMapCamera(LatLng(lastKnownLocation.latitude, lastKnownLocation.longitude))
            }

        })


        setToolbarUpFunction(binding.myToolbar)


        return binding.root
    }

    private fun setSearchCoordinateView() {
       val editText = binding.searchEt
       setCustomAdapter(editText)

        binding.coordinatesSV.setStartIconOnClickListener{
            if (editText.text.toString().isEmpty()){
                return@setStartIconOnClickListener
            }

            val coordinateLatLang = getCoordinatesInLatLang(editText.text.toString())

            coordinateLatLang?.let {
                //myMap?.moveCamera(CameraUpdateFactory.newLatLng(coordinateLatLang))
                viewModel.setLocationSelected(coordinateLatLang)
            }
        }
    }

    private fun setCustomAdapter(editText: AutoCompleteTextView) {
        val adapter = BusinessInfoAdapter(requireContext(), R.layout.business_data_item, lisOfBusiness)
        (editText as? AutoCompleteTextView)?.apply {
            setAdapter(adapter)
        }
    }

    private fun getCoordinatesInLatLang(coordinatesString: String?): LatLng? {
        var coordinates: LatLng? = null

        try {
            val array = coordinatesString?.split(",")?.map { it.trim().toDouble() }

            array?.let {
                coordinates =  LatLng(array[0], array[1])
            }
        }catch (exeption: NumberFormatException){
            showSnackbar(binding.root.rootView,getString(R.string.enter_valid_coodinates))
        }


        return coordinates
    }

    private fun getMapScreenshot(){
        myMap?.snapshot { bitmap ->
            when (callingFragment) {
                USER_DELIVERY_ADDRESS -> {
                    currentTransactionItem?.apply {
                        userAddressBitmap = bitmap
                    }
                }
                USER_PICK_UP_ADDRESS -> {
                    currentTransactionItem?.apply {
                        locationBAddressBitmap = bitmap
                    }
                }
            }
            goBackToCallingFragment(callingFragment!!)
        }
    }

    private fun getAddressReference(referenceEt: EditText): String? {
        return referenceEt.text.toString()
    }


    private fun goBackToCallingFragment(callingFragment: String) {

        when (callingFragment) {
            USER_DELIVERY_ADDRESS -> {
                currentTransactionItem?.apply {
                    userAddressReference = getAddressReference(referenceEt)
                    userAddress = locationSelected
                }
            }
            USER_PICK_UP_ADDRESS -> {
                currentTransactionItem?.apply {
                    locationBAddress = locationSelected
                    locationBAddressReference = getAddressReference(referenceEt)
                }
            }
        }



        findNavController().navigate(MapsFragmentDirections.actionMapsFragmentToTransactionAddressesFragment(
            callingFragment =  callingFragment,
            currentTransactionItem = currentTransactionItem,
            request = args.currentManManRequest))
    }


    @SuppressLint("MissingPermission")
    private fun getLocationPermission() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            viewModel.setIsLocationPermissionGranted(true)

        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMap(R.id.map,childFragmentManager,callback)
    }


}