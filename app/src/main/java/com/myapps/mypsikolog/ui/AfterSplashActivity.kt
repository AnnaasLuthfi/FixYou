package com.myapps.mypsikolog.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.myapps.mypsikolog.R
import com.myapps.mypsikolog.databinding.ActivityAfterSplashBinding
import com.myapps.mypsikolog.home.HomeActivity
import com.myapps.mypsikolog.ui.signin.SignInActivity
import com.myapps.mypsikolog.ui.signup.SignUpActivity
import com.myapps.mypsikolog.utils.Constants
import com.myapps.mypsikolog.utils.PreferenceManager

class AfterSplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAfterSplashBinding
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAfterSplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        preferenceManager = PreferenceManager(applicationContext)

        //langsung ke home jika sudah login atau regis
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        }

        binding.btnSignIn.setOnClickListener {
            val intent = Intent(this@AfterSplashActivity, SignInActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignUp.setOnClickListener{
            startActivity(Intent(this@AfterSplashActivity, SignUpActivity::class.java))
        }

        Glide.with(binding.image.context).load(R.drawable.doc_resize).apply(RequestOptions().override(290,290)).into(binding.image)

    }

}