package com.myapps.mypsikolog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.myapps.mypsikolog.databinding.ActivityAfterSplashBinding

class AfterSplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAfterSplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAfterSplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding?.btnSignIn?.setOnClickListener {
            val intent = Intent(this@AfterSplashActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding?.btnSignUp?.setOnClickListener{
            startActivity(Intent(this@AfterSplashActivity, RegisterActivity::class.java))
        }

    }

}