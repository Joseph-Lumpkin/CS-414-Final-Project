package com.squibb.android.models

class Group(
    private var mGroupId: Int  // ID of the group
) {
    /** Display name of the group */
    private lateinit var mName: String

    /** List of Users associated with this group <Email, Permission Level> */
    private lateinit var mUsers: HashMap<String, Int>

    fun getGroupId(): Int {
        return mGroupId
    }

    fun getName(): String {
        return mName
    }

    fun setName(name: String) {
        mName = name
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