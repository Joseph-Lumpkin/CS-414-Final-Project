package com.squibb.android

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.FirebaseApp
import com.squibb.android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    /** Debug logging TAG */
    private val TAG = "MainActivity"

    /** View Binding */
    private lateinit var mBinding: ActivityMainBinding

    /** LoginActivity Result Listener */
    private val mLoginActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // If the login result was OK (logged in successfully)
            if (result.resultCode == Activity.RESULT_OK) {
                // Retrieve the credentials and save the retrieved user account
                val data = result.data
                val idToken = data?.getStringExtra(LoginActivity.dsKEY_ID_TOKEN)
                Log.d(TAG, "$idToken successfully retrieved and returned to $TAG.")
                //TODO Remove reference to the user's email credentials. We don't want to deal with PII
                val email = data?.getStringExtra(LoginActivity.dsKEY_EMAIL)
                Log.d(TAG, "$email successfully logged in and returned to $TAG.")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

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
        println("")
    }
}