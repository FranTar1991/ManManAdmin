package com.manmanadmin.main_activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.manmanadmin.R
import com.manmanadmin.databinding.ActivityMainBinding
import com.manmanadmin.utils.sendRegistrationToServer
import com.manmanadmin.utils.showAlertDialog

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels { MainActivityViewModelFactory(application) }
    private val signInLauncher= registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this



        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if (firebaseUser == null){
            createSignInIntent(signInLauncher)
        }

        viewModel.registrationToken.observe(this){ token->
            FirebaseAuth.getInstance().currentUser?.uid?.let {
                sendRegistrationToServer(token, it)
            }
        }

    }


    private fun getNewToken(){
        // Get token
        // [START log_reg_token]
        Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            viewModel.setRegistrationToken(token)

        })
        // [END log_reg_token]
    }


    private fun createSignInIntent(signInLauncher: ActivityResultLauncher<Intent>) {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),AuthUI.IdpConfig.PhoneBuilder().build())

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setIsSmartLockEnabled(false)
            .setAvailableProviders(providers)
            .build()
        signInLauncher.launch(signInIntent)
        // [END auth_fui_create_intent]
    }
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == AppCompatActivity.RESULT_OK) {

            getNewToken()
            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            if (response == null){
                finish()
            } else{
                Toast.makeText(this,getString(R.string.error_log_in, response.error?.errorCode),
                    Toast.LENGTH_SHORT).show()
            }
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }
}