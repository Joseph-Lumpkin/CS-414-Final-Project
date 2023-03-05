package com.squibb.android.models

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Group(
    private var mGroupOwnerId: String
) {

    /** Debug logging tag */
    private val TAG = "Group"

    /** Database Reference */
    private val db = Firebase.firestore

    /** Statically defined keywords */
    companion object {
        /** Database Key for the user's app-unique ID */
        const val dsKEY_GROUPS_COLLECTION = "groups"

        /** Database Key for the group's ID */
        const val dsKEY_ID_GROUP = "groupId"

        /** Database Key for the group's display name */
        const val dsKEY_NAME = "name"

        /** Database Key for the group's join password */
        const val dsKEY_PASS = "pass"

        /** Database Key for the group's user list */
        const val dsKEY_USERS = "users"

        /** Permission level - Member */
        const val dnPERMISSION_MEMBER = 0

        /** Permission level - Admin */
        const val dnPERMISSION_ADMIN = 1

        /** Permission level - Owner */
        const val dnPERMISSION_OWNER = 2
    }

    /** ID of the group */
    private var mGroupId = ""

    /** Display name of the group */
    private lateinit var mName: String

    /** Password to join the group */
    private lateinit var mPass: String

    /** List of Users associated with this group <Email, Permission Level> */
    private lateinit var mUsers: HashMap<String, Int>

    fun getGroupId(): String {
        return mGroupId
    }

    fun getName(): String {
        return mName
    }

    fun setName(name: String) {
        mName = name
    }

    fun getPass(): String {
        return mPass
    }

    fun setPass(pass: String) {
        mPass = pass
    }

    /**
     * Create this group in the database.
     */
    fun create() {
        // Add the group's owner
        mUsers = HashMap()
        mUsers[mGroupOwnerId] = dnPERMISSION_OWNER

        val group = hashMapOf(
            dsKEY_NAME to mName,
            dsKEY_PASS to mPass,
            dsKEY_USERS to mUsers
        )
        // Add a new group document with a generated id.
        db.collection(dsKEY_GROUPS_COLLECTION)
            .add(group)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                mGroupId = documentReference.id
                // Add this group reference to the group owner's user account
                val userRef = db.collection(User.dsKEY_USER_COLLECTION).document(mGroupOwnerId)
                // Atomically add a new region to the "regions" array field.
                userRef.update(User.dsKEY_ASSOCIATED_GROUPS, FieldValue.arrayUnion(mGroupId))
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    /**
     * Update the database entry for this group item.
     */
    private fun update() {
        //TODO add code to update the database entry for this item
    }

    /**
     * Read the users into this group from the database.
     */
    fun readUsers() {
        //TODO add code to read in the users that are associated with this group
    }
}