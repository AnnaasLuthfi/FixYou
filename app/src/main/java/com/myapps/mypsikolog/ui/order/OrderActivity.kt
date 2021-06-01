package com.myapps.mypsikolog.ui.order

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.myapps.mypsikolog.R
import com.myapps.mypsikolog.databinding.ActivityOrderBinding
import org.opencv.android.OpenCVLoader


class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding

    init {
        if (OpenCVLoader.initDebug()){
            Log.d("MainActivity: ", "Opencv is loaded")
        }else{
            Log.d("MaincActivity: ", "Opencv is load")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.openCvButton.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

    }
}