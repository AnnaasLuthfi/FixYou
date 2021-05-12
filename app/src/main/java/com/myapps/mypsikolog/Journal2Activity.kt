package com.myapps.mypsikolog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Journal2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal2)
        supportActionBar?.hide()
    }
}