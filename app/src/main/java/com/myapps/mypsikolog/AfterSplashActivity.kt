package com.myapps.mypsikolog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class AfterSplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_splash)

        supportActionBar?.hide()


    }
}