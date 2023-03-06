package com.squibb.android

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.FirebaseApp
import com.squibb.android.databinding.ActivityMainBinding
import com.squibb.android.models.SharedViewModel
import com.squibb.android.models.User

class MainActivity : AppCompatActivity() {

    /** Debug logging TAG */
    private val TAG = "MainActivity"

    /** View Binding */
    private lateinit var mBinding: ActivityMainBinding

    /** Shared View Model for passing data to fragments */
    private lateinit var mSharedViewModel: SharedViewModel

    /** Application User */
    lateinit var mUser: User

    /** LoginActivity Result Listener */
    private val mLoginActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // If the login result was OK (logged in successfully)
            if (result.resultCode == Activity.RESULT_OK) {
                // Retrieve the credentials and save the retrieved user account
                val data = result.data
                val email = data?.getStringExtra(User.dsKEY_EMAIL)
                if (data != null && email != null) {
                    // Create a user object with the obtained login data
                    Log.d(TAG, "User successfully retrieved and returned to $TAG.")
                    mUser = User(email)
                    // Add the user ID to be shared across fragments
                    mSharedViewModel.mUser.value = mUser
                    mUser.read() // Load remaining data from database
                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                if(!BuildConfig.DEBUG) {
                    // If the users cancelled the login process
                    finish()    // Close the application
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the SharedViewModel
        mSharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        // Initialize the firebase app
        FirebaseApp.initializeApp(applicationContext)
        // Initialize the view binding
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        // Initialize the nav view and controller
        val navView: BottomNavigationView = mBinding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_groups, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Initialize onClickListeners
        mBinding.bMainDebug.setOnClickListener { debug() }

        // Always require the user to sign in
        goToLoginActivity()
    }

    /**
     * Launch the LoginActivity
     * and prompt the user to log in.
     */
    private fun goToLoginActivity() {
        val loginIntent = Intent(this, LoginActivity::class.java)
        mLoginActivityLauncher.launch(loginIntent)
    }

    /**
     * Debug function.
     * Definition and function will change throughout development.
     * Use this function to debug the application.
     */
    private fun debug() {
        Log.d(TAG, mUser.getUserId())
        Log.d(TAG, mUser.getEmail())
        Log.d(TAG, mUser.getGroups().toString())
    }
}