package com.myapps.mypsikolog.ui.journal

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.myapps.mypsikolog.R
import com.myapps.mypsikolog.databinding.ActivityJournal2Binding

class Journal2Activity : AppCompatActivity() {

    private lateinit var binding:ActivityJournal2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJournal2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
    }

    fun onRadioButtonClicked(view: View) {
        val isSelected = false

        when(view.id) {
            R.id.rat1Ques1 -> {
                if (isSelected) {
                    binding.rat1Ques1.setTextColor(Color.WHITE)
                    binding.rat2Ques1.setTextColor(Color.BLACK)
                }
            }

            R.id.rat2Ques1 -> {
                if (isSelected) {
                    binding.rat1Ques1.setTextColor(Color.BLACK)
                    binding.rat2Ques1.setTextColor(Color.WHITE)
                }
            }

        }
    }


}