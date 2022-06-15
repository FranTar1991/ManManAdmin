package com.manmanadmin.add_business

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.model.LatLng
import com.manmanadmin.R
import com.manmanadmin.databinding.FragmentAddBusinessBinding
import com.manmanadmin.utils.BusinessMenu
import com.manmanadmin.utils.checkIfEmpty
import com.manmanadmin.utils.setEmpty
import com.manmanadmin.utils.showSnackbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class AddBusinessFragment : Fragment() {

    private lateinit var binding: FragmentAddBusinessBinding
    private lateinit var nameEt: EditText
    private lateinit var menuLinksEt: EditText
    private lateinit var phoneEt: EditText
    private lateinit var categoryEt: EditText
    private lateinit var imageLogo: ImageView
    private lateinit var latLongEt: EditText

    private lateinit var imageUrlEt: EditText
    private val viewModel: AddBusinessViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddBusinessBinding.inflate(inflater,container,false)

        nameEt = binding.businessName
        phoneEt = binding.businessPhoneEt
        menuLinksEt = binding.menuLinksEt
        categoryEt = binding.categoryEt
        imageLogo = binding.logoImg
        latLongEt = binding.latitudeEt

        imageUrlEt = binding.logoUrlEt

        binding.saveBusinessBtn.setOnClickListener {
            val business = getBusinessInfo()
            business?.let {
                viewModel.saveBusiness(business)
            }

        }

        imageUrlEt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                loadImageWithPicasso(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        viewModel.businessSaved.observe(viewLifecycleOwner){
            if (it){
                emptyEditTexts()
            }
        }



        return binding.root
    }

    private fun emptyEditTexts() {
        nameEt.setEmpty()
        categoryEt.setEmpty()
        latLongEt.setEmpty()
        imageUrlEt.setEmpty()
        phoneEt.setEmpty()
        menuLinksEt.setEmpty()
        imageLogo.setImageBitmap(null)
        showSnackbar(binding.root.rootView, getString(R.string.business_added))

    }

    private fun loadImageWithPicasso(url: String?) {
        if (url?.isNotEmpty() == true){
            Picasso.get().load(url)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.mipmap.ic_launcher)
                .into(imageLogo,  object : Callback {
                    override fun onSuccess() {

                    }

                    override fun onError(exception: Exception?) {
                        Log.i("MyPicassoImage", "exception: $exception" )
                    }

                })
        }

    }

    private fun getBusinessInfo(): Business? {
       if(categoryEt.checkIfEmpty() ||
           phoneEt.checkIfEmpty() ||
        nameEt.checkIfEmpty() ||
        imageUrlEt.checkIfEmpty() ||
        latLongEt.checkIfEmpty()){

           return null
       }

        val businessLocation = getBusinessLatLng()

     return   Business( name = nameEt.text.toString(),
            lat = businessLocation.latitude,
            long=  businessLocation.longitude,
            category = categoryEt.text.toString(),
         imageUrl= imageUrlEt.text.toString(),
         businessPhoneNumber = phoneEt.text.toString(), menu =  getArrayWIthMenuLinks())
    }

    private fun getBusinessLatLng(): LatLng {
        val arrayOfDoubles = latLongEt.text.split(",").map { it.trim().toDouble() }
        return LatLng(arrayOfDoubles[0], arrayOfDoubles[1])
    }

    private fun getArrayWIthMenuLinks(): ArrayList<BusinessMenu> {
        val arrayOfBusinessMenu = arrayListOf<BusinessMenu>()
        val arrayOfStrings = menuLinksEt.text.toString().split(",").map { it.trim() }

        for (link in arrayOfStrings){
            arrayOfBusinessMenu.add(BusinessMenu(link))
        }

        return arrayOfBusinessMenu

    }


}