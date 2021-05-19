package com.myapps.mypsikolog.ui.journal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.myapps.mypsikolog.databinding.ActivityJournal1Binding

class Journal1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityJournal1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJournal1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.nextBtn.setOnClickListener {
            startActivity(Intent(this@Journal1Activity, Journal2Activity::class.java))
        }

    }
}