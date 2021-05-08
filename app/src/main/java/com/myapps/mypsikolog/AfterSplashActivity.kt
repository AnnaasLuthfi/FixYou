package com.myapps.mypsikolog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.myapps.mypsikolog.databinding.ActivityAfterSplashBinding

class AfterSplashActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAfterSplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_splash)

        supportActionBar?.hide()

        binding.btnSignIn.setOnClickListener(this)
        binding.btnSignUp.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btnSignIn -> {
                startActivity(Intent(this@AfterSplashActivity, LoginActivity::class.java))
            }

            R.id.btnSignUp -> {
                startActivity(Intent(this@AfterSplashActivity, RegisterActivity::class.java))
            }
        }
    }
}