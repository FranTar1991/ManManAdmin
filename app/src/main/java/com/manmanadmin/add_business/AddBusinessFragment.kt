package com.manmanadmin.add_business

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.viewModels
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.manmanadmin.R
import com.manmanadmin.databinding.FragmentAddBusinessBinding
import com.manmanadmin.utils.checkIfEmpty
import com.manmanadmin.utils.setEmpty
import com.manmanadmin.utils.showSnackbar
import com.squareup.picasso.Picasso
import java.lang.IndexOutOfBoundsException

class AddBusinessFragment : Fragment() {

    private lateinit var binding: FragmentAddBusinessBinding
    private lateinit var nameEt: EditText
    private lateinit var phoneEt: EditText
    private lateinit var categoryEt: EditText
    private lateinit var imageLogo: ImageView
    private lateinit var latEt: EditText
    private lateinit var longEt: EditText
    private lateinit var imageUrlEt: EditText
    private val viewModel: AddBusinessViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddBusinessBinding.inflate(inflater,container,false)

        nameEt = binding.businessName
        phoneEt = binding.businessPhoneEt
        categoryEt = binding.categoryEt
        imageLogo = binding.logoImg
        latEt = binding.latitudeEt
        longEt = binding.longitudeEt
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
                loadImageWithPicasso(getNewImageUrl(p0.toString()))
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
        latEt.setEmpty()
        longEt.setEmpty()
        imageUrlEt.setEmpty()
        showSnackbar(binding.root.rootView, getString(R.string.business_added))

    }

    private fun loadImageWithPicasso(url: String?) {
        if (url?.isNotEmpty() == true){
            Picasso.get().load(url)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(imageLogo)
        }

    }

    private fun getBusinessInfo(): Business? {
       if(categoryEt.checkIfEmpty() ||
           phoneEt.checkIfEmpty() ||
        nameEt.checkIfEmpty() ||
        imageUrlEt.checkIfEmpty() ||
        latEt.checkIfEmpty() ||
        longEt.checkIfEmpty()){

           return null
       }

     return   Business(nameEt.text.toString(),
            latEt.text.toString().trim().toDouble(),
            longEt.text.toString().trim().toDouble(),
            categoryEt.text.toString(),
            imageUrlEt.text.toString(),
            phoneEt.text.toString())
    }



    private fun getNewImageUrl(url: String): String {
        return    try {
            val p = url.split("/").toTypedArray()
            "https://drive.google.com/uc?export=download&id=" + p[5]
        }catch (error: IndexOutOfBoundsException){
            ""
        }



    }

}