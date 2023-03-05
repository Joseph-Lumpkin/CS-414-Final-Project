package com.squibb.android.models

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class User(
    private var mUserId: String // ID of the user
) {

    /** Debug logging tag */
    private val TAG = "User"

    /** Database Reference */
    private val db = Firebase.firestore

    /** Statically defined keywords */
    companion object {
        /** Database Key for the user's app-unique ID */
        const val dsKEY_USER_COLLECTION = "users"

        /** Database Key for the user's app-unique ID */
        const val dsKEY_ID_TOKEN = "idToken"

        /** Database Key for the user's email */
        const val dsKEY_EMAIL = "email"

        /** Database Key for the user's associated groups */
        const val dsKEY_ASSOCIATED_GROUPS = "associatedGroups"
    }

    /** Email of the user */
    private lateinit var mEmail: String

    /** List of Groups associated with this user*/
    private var mGroups = arrayListOf<String>()

    /**
     * Get the User's ID
     *
     * @return userId   - ID of the user.
     */
    fun getUserId(): String {
        return mUserId
    }

    /**
     * Get the User's email
     *
     * @return email    - email of the user.
     */
    fun getEmail(): String {
        return mEmail
    }

    /**
     * Set the User's Email
     *
     * @param email - Email of the user
     */
    fun setEmail(email: String) {
        mEmail = email
    }

    /**
     * Get the list of groups that are
     * associated with this user.
     *
     * @return groups   - Groups associated with this user.
     */
    fun getGroups(): List<String> {
        return mGroups
    }

    /**
     * Add a group to the user's list of associated groups.
     *
     * @param group - ID of the group to be added to this user.
     */
    fun addGroup(group: String) {
        mGroups.add(group)
    }

    /**
     * Remove a specified group from this user's list of associated groups.
     *
     * @param group - ID of the group to be removed.
     */
    fun removeGroup(group: String) {
        mGroups.remove(group)
    }

    /**
     * Create this user in the database.
     * If the user exists, this merges data.
     */
    fun create() {
        val user = hashMapOf(
            dsKEY_EMAIL to mEmail
        )
        val userRef = db.collection(dsKEY_USER_COLLECTION).document(mUserId)
        userRef.set(user, SetOptions.merge())
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully written!")
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

        userRef.update(dsKEY_ASSOCIATED_GROUPS, FieldValue.arrayUnion(""))
    }

    /**
     * Read this user object from the database.
     */
    fun read() {
        val docRef = db.collection("users").document(mUserId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    mEmail = document.get(dsKEY_EMAIL) as String
                    mGroups = document.get(dsKEY_ASSOCIATED_GROUPS) as ArrayList<String>
                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

    /**
     * Update this user object in the database.
     */
    fun update() {

    }

    /**
     * Delete this user object from the database.
     */
    fun delete() {

    }
}