package com.squibb.android

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException
import com.squibb.android.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    /** Debug logging TAG */
    private val TAG = "LoginActivity"

    /** View Binding */
    private lateinit var mBinding: ActivityLoginBinding

    /** Google One Tap Client authorization variables */
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private lateinit var signUpRequest: BeginSignInRequest
    private lateinit var credential: SignInCredential
    private val REQ_ONE_TAP = 2

    /** Statically defined keywords */
    companion object {
        const val dsKEY_ID_TOKEN = "idToken"
        const val dsKEY_EMAIL = "email"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the view binding for nonnullable view referencing
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // Initialize the oneTapClient for our activity
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(BuildConfig.API_KEY)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            // Do not automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(false)
            .build()
        // Google Sign Up Request
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(BuildConfig.API_KEY)
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .build()
        // Wait for the user initiate the login process with google
        mBinding.bLogin.setOnClickListener { signIn() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    val email = credential.id
                    // Got an ID token from Google. Use it to authenticate with your backend.
                    Log.d(TAG, "Got ID token: $idToken")
                    // Got a saved username
                    Log.d(TAG, "Got email: $email")
                    if (email != null) {
                        val finishLoginIntent = Intent()
                        finishLoginIntent.putExtra(dsKEY_ID_TOKEN, idToken)
                        finishLoginIntent.putExtra(dsKEY_EMAIL, email)
                        setResult(Activity.RESULT_OK, finishLoginIntent)
                        finish()
                    }
                } catch (e: ApiException) {
                    println("error")
                }
            }
        }
    }

    /**
     * Display the OneTapClient Sign In UI
     */
    private fun signIn() {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0, null
                    )
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(this) { e ->
                // No saved credentials found. Launch the One Tap sign-up flow.
                signUp()
            }
    }


    /**
     * Display the OneTapClient Sign Up UI
     */
    private fun signUp() {
        //TODO Test the signup flow
        oneTapClient.beginSignIn(signUpRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0, null
                    )
                } catch (e: IntentSender.SendIntentException) {
                    Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(this) { e ->
                // User failed to sign up
                // Do nothing and continue presenting the signed-out UI.
                Log.d(TAG, e.localizedMessage)
            }
    }
}