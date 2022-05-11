package com.manmanadmin.utils

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.LocationManager
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.manmanadmin.R
import com.manmanadmin.databinding.ToolBarIncludedLayoutBinding

const val PROFILE_FRAGMENT = "profile_fragment"
const val USER_DELIVERY_ADDRESS = "delivery address"
const val USER_PICK_UP_ADDRESS = "pick up address"
const val CUSTOMER_SERVICE_PHONE_NUMBER = "+50587151530"

fun showSnackbar(view: View, text: String){
    Snackbar.make(view,text, Snackbar.LENGTH_SHORT).show()
}

fun showAlertDialog(
    title: String,
    message: String,
    context: Context?,
    hasCancelButton: Boolean = true,
    icon: Int? = null,
    functionToExecute: (() -> Unit)?
): AlertDialog? {
    val alertDialog: AlertDialog? = context?.let {
        val builder = AlertDialog.Builder(it)
        builder.apply {
            setMessage(message)
            setTitle(title)
            icon?.let { setIcon(it) }
            setPositiveButton(
                R.string.okIt,
                DialogInterface.OnClickListener { dialog, id ->
                    functionToExecute?.let { function -> function() }
                    dialog.cancel()
                })
            if (hasCancelButton){
                setNegativeButton(R.string.cacenlIt){dialog, id ->
                    dialog.cancel()
                }
            }

        }
        builder.create()
    }
    return alertDialog
}

fun checkIfGpsIsOn(context: Context?): Boolean{
    val mLocationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}
fun callGPSPageOnSettings(context: Context?){
    val intent1 =  Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
    context?. startActivity(intent1);
}
fun getBitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor {
    val bitmap = getBitmapFromVectorDrawable(context,vectorResId)
    return  BitmapDescriptorFactory.fromBitmap(bitmap)
}
fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(context, drawableId)
    val bitmap = Bitmap.createBitmap(
        drawable!!.intrinsicWidth,
        drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

fun setToolbarUpFunction(toolbar: Toolbar){
    toolbar.apply {
        setNavigationOnClickListener(View.OnClickListener {
            this.findNavController().popBackStack()
        })
    }
}

fun initMap(mapId: Int, childFragmentManager: FragmentManager, callback: OnMapReadyCallback) {
    val fm = childFragmentManager
    var mapFragment = childFragmentManager.findFragmentById(mapId) as SupportMapFragment?
    if (mapFragment == null) {
        mapFragment = SupportMapFragment()
        val ft = fm.beginTransaction()
        ft.add(mapId, mapFragment, "mapFragment")
        ft.commit()
        fm.executePendingTransactions()
    }
    mapFragment.getMapAsync(callback)
}

fun setToolbarUpFunction(fragment: Fragment,
                         context: Context?,
                         toolbarBinding: ToolBarIncludedLayoutBinding,
                         viewModel: MyViewModel){


    val toolbar = toolbarBinding.myToolbar
    toolbar.apply {
        setNavigationOnClickListener(View.OnClickListener {
            fragment.findNavController().navigateUp()
        })
    }

    context?.let {
        viewModel.setToolBarText(fragment, context)
    }


    toolbarBinding.deleteTransaction.setOnClickListener {
        viewModel.setNavigateToMainFragment(true)
    }
}

fun saveDoubleToSharedPreferences(key: String, value: Float, context: Context
) {
    val sharedPref = context.getSharedPreferences(
        context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
    with (sharedPref.edit()) {
        putFloat(key, value)
        apply()
    }
}

fun saveStringToSharedPreferences(key: String, value: String, context: Context
) {
    val sharedPref = context.getSharedPreferences(
        context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
    with (sharedPref.edit()) {
        putString(key, value)
        apply()
    }
}

fun getThisNodeReference(requestId: String): DatabaseReference {
    return FirebaseDatabase.getInstance().reference.child("all_requests_not_reviewed").child(requestId)
}

 fun getRequestReference(requestId: String, userId: String): DatabaseReference {
    return FirebaseDatabase.getInstance().reference.child("users").child(userId).child("requests").child(requestId)
}