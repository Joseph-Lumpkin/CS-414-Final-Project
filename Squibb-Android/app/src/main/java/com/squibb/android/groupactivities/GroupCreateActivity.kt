package com.squibb.android.groupactivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.squibb.android.databinding.ActivityGroupCreateBinding
import com.squibb.android.models.Group
import com.squibb.android.models.User

class GroupCreateActivity : AppCompatActivity() {

    /** Debug logging TAG */
    private val TAG = "GroupCreateActivity"

    /** View Binding */
    private lateinit var mBinding: ActivityGroupCreateBinding

    /** ID of the user creating this group */
    private lateinit var mOwnerId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the view binding for nonnullable view referencing
        mBinding = ActivityGroupCreateBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        // Get the ID of the person creating this group
        mOwnerId = intent.getStringExtra(User.dsKEY_EMAIL).toString()

        // Initialize the onClickListeners for this activity
        initOnClickListeners()
    }

    /**
     * Initialize the onClickListeners for this Activity.
     */
    private fun initOnClickListeners() {
        mBinding.bGCSubmit.setOnClickListener { createGroup() }
    }

    /**
     * Finishes group creation and creates the group.
     */
    private fun createGroup() {
        val passOne = mBinding.edGCPassOne.text.toString()
        val passTwo = mBinding.edGCPassTwo.text.toString()
        // If the entered passwords match
        if (passOne.equals(passTwo)) {
            // Create a group and save it to the database, exit this fragment
            var group = Group(mOwnerId)
            group.setName(mBinding.edGCName.text.toString())
            group.setPass(mBinding.edGCPassOne.text.toString())
            group.create()
            // Finish out of this group creation activity
            finish()
        } else {
            Toast.makeText(
                this,
                "The passwords do not match.\nPlease try again.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}