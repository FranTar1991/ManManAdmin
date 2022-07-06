package com.manmanadmin.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.SnapshotParser
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.manmanadmin.R
import com.manmanadmin.change_business_status.ChangeBusinessStatusDialog
import com.manmanadmin.databinding.ToolBarIncludedLayoutBinding
import java.text.DateFormat
import java.util.*

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

 fun changeBusinessStatus(activity: Activity, childFragmentManager: FragmentManager): Boolean {
    val status_options = activity.resources!!.getStringArray(R.array.busines_status_options) as Array<String>
    ChangeBusinessStatusDialog(status_options).show(childFragmentManager,"business_status_dialog")
    return true
}

 fun getRequestReference(requestId: String, userId: String): DatabaseReference {
    return FirebaseDatabase.getInstance().reference.child("users").
    child(userId).child("requests").child(requestId)
}

 fun setAdapter(query: Query,
                onManManRequestClickListener: OnManManRequestClickListener,
                viewLifecycleOwner: LifecycleOwner,
                viewModel: ViewModelForAdapterInterface): AdapterForRequests {

    val snapshotParser = SnapshotParser<ManManRequest> { snapshot ->
        val requestId = snapshot.key
        val userId = snapshot.child("user_id").getValue(String::class.java)
        val status = snapshot.child("status").getValue(String::class.java)
        val comments = snapshot.child("comments").getValue(String::class.java)
        val isReviewed = snapshot.child("reviewed").getValue(Boolean::class.java)

        ManManRequest(requestId = requestId, user_id = userId, status =  status, comments = comments, isReviewed = isReviewed)
    }



    val options: FirebaseRecyclerOptions<ManManRequest> = FirebaseRecyclerOptions.Builder<ManManRequest>()
        .setQuery(query,snapshotParser)
        .setLifecycleOwner(viewLifecycleOwner)
        .build()

    return AdapterForRequests(viewModel, options, onManManRequestClickListener, query as DatabaseReference)

}

fun getDate(): String{
    return DateFormat.getDateInstance().format(Date())

}

fun sendRegistrationToServer(token: String?, userId: String) {
    val baseReference = FirebaseDatabase.getInstance().reference

    val reference = baseReference.child("data").child("admins").child("FCMToken")
    reference.setValue(token)
}

fun getManManRequestToPaste(dataToTransfer: DataSnapshot): ManManRequest? {
    val userId = dataToTransfer.child("user_id").value.toString()
    val comments = dataToTransfer.child("comments").value.toString()
    return ManManRequest(user_id = userId, comments = comments)
}

fun sendRequestToNextQueue(thisNodeReference: DatabaseReference?) {
    val baseRef = FirebaseDatabase.getInstance().reference

    thisNodeReference?.get()?.addOnSuccessListener { dataToTransfer ->
        val formattedDataToPaste = getManManRequestToPaste(dataToTransfer)
        dataToTransfer.key?.let { thisKey -> baseRef.child("all_requests")
            .child(thisKey)
            .setValue(formattedDataToPaste).addOnSuccessListener {
                dataToTransfer.ref.removeValue()
            }}
    }
}

fun getSumOfMoneyEarnedInRequests(requests: List<RequestRemote>?): Double{
    var sum  = 0.0
        requests?.let {
            for (request in requests){
                sum += request.price ?: 0.0
            }
        }

    return sum
}

fun EditText.checkIfEmpty(): Boolean{
   return if (text.toString() == ""){
        shake(this,context)
        true
    }else {
        false
    }

}

fun EditText.setEmpty(){
    setText("")
}

fun shake(view: View?, context: Context?) {
    // Create shake effect from xml resource
    // Create shake effect from xml resource
    val shake: Animation = AnimationUtils.loadAnimation(context, R.anim.shake_view)
    // Perform animation
    // Perform animation
    view?.startAnimation(shake)
}

 fun openWhatsAppWithNumber(number: String, context: Context?){
    val pm: PackageManager? = context?.packageManager

    try {
        // Raise exception if whatsapp doesn't exist
        val info = pm?.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA)
        val url = "https://wa.me/$number"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context?.startActivity(intent)
    } catch (e: PackageManager.NameNotFoundException) {
        Toast.makeText(context, "Please install whatsapp app", Toast.LENGTH_SHORT)
            .show()
    }
}

fun openWhatsAppWithInfo(number: String, information: String, context: Context?){
    val pm: PackageManager? = context?.packageManager

    try {
        // Raise exception if whatsapp doesn't exist
        val info = pm?.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA)
        val url = "https://wa.me/$number?text=$information"
        Log.i("MyMessage","$url")
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        context?.startActivity(intent)
    } catch (e: PackageManager.NameNotFoundException) {
        Toast.makeText(context, "Please install whatsapp app", Toast.LENGTH_SHORT)
            .show()
    }
}

