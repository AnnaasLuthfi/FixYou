package com.myapps.mypsikolog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Journal1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal1)

        supportActionBar.hide()
    }
}