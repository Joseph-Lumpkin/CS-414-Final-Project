package com.squibb.android.taskactivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squibb.android.R

class TaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
    }
}