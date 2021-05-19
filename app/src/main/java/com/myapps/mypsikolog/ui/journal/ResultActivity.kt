package com.myapps.mypsikolog.ui.journal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.myapps.mypsikolog.R

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        supportActionBar?.hide()
    }
}