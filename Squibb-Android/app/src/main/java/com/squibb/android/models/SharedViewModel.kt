package com.squibb.android.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    /** Currently Logged in User Object */
    val mUser = MutableLiveData<User>()
}