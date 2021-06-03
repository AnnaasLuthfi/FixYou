package com.myapps.mypsikolog.ui.diagnoze

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.myapps.mypsikolog.R
import com.myapps.mypsikolog.databinding.ActivityDiagnozeBinding
import com.myapps.mypsikolog.databinding.ActivityOrderBinding
import com.myapps.mypsikolog.ui.order.CameraActivity
import org.opencv.android.OpenCVLoader

class DiagnozeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiagnozeBinding

    init {
        if (OpenCVLoader.initDebug()){
            Log.d("MainActivity: ", "Opencv is loaded")
        }else{
            Log.d("MaincActivity: ", "Opencv is load")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDiagnozeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Diagnoze"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.camera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}