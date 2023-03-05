package com.squibb.android.ui.groups

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squibb.android.groupactivities.*
import com.squibb.android.databinding.FragmentGroupsBinding
import com.squibb.android.models.SharedViewModel
import com.squibb.android.models.User

class GroupsFragment : Fragment() {

    /** Debug logging TAG */
    private val TAG = "GroupsFragment"

    private lateinit var mSharedViewModel: SharedViewModel

    private var mBinding: FragmentGroupsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = mBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Setup the SharedViewModel
        mSharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        // Setup the view binding
        val groupsViewModel =
            ViewModelProvider(this).get(GroupsViewModel::class.java)
        mBinding = FragmentGroupsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val textView: TextView = binding.textGroups
        groupsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        // Initialize the view properties
        initViews()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

    /**
     * Initialize the view properties for this fragment.
     */
    private fun initViews() {
        // Set onClickListeners
        mBinding?.bGroupsJoin?.setOnClickListener { launchJoinGroupActivity() }
        mBinding?.bGroupsCreate?.setOnClickListener { launchCreateGroupActivity() }
    }

    /**
     * Launch the Group Join Activity
     */
    private fun launchJoinGroupActivity() {
        val launchJoinGroupIntent = Intent(context, GroupJoinActivity::class.java)
        startActivity(launchJoinGroupIntent)
    }


    /**
     * Launch the Group Creation Activity
     */
    private fun launchCreateGroupActivity() {
        val launchCreateGroupIntent = Intent(context, GroupCreateActivity::class.java)
        val user = mSharedViewModel.mUser.value
        if (user != null) {
            launchCreateGroupIntent.putExtra(User.dsKEY_ID_TOKEN, user.getUserId())
        }
        startActivity(launchCreateGroupIntent)
    }
}