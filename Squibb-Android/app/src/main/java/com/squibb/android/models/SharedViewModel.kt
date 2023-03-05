package com.squibb.android.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    /** ID of the currently logged in user. */
    val userId = MutableLiveData<String>()
}