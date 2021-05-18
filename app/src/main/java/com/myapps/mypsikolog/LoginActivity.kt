package com.myapps.mypsikolog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.myapps.mypsikolog.databinding.ActivityLoginBinding
import com.myapps.mypsikolog.home.HomeActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
        }

    }
}