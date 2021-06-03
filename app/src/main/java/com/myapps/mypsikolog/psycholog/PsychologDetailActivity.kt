package com.myapps.mypsikolog.psycholog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.myapps.mypsikolog.databinding.ActivityPsychologDetailBinding
import com.myapps.mypsikolog.databinding.BookingpopupBinding
import com.myapps.mypsikolog.models.MyOrder
import com.myapps.mypsikolog.models.Psycholog
import java.util.*


class PsychologDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPsychologDetailBinding
    private lateinit var bindingPopup: BookingpopupBinding
    private var psycholog: Psycholog? = null
    private lateinit var dialog: Dialog
    private lateinit var database: FirebaseDatabase
    private lateinit var request: DatabaseReference
    private lateinit var radioDateButton: RadioButton
    private lateinit var radioTimeButton: RadioButton
    private lateinit var radioPriceButton: RadioButton

    companion object{
        const val EXTRA_PSYCHOLOG = "extra_psycholog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPsychologDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarDetail)
        psycholog = intent.getParcelableExtra(EXTRA_PSYCHOLOG)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = psycholog?.name.toString()
        binding.bookNowBtn.setOnClickListener {
            showPopUp()
        }

        database = FirebaseDatabase.getInstance()
        request = database.getReference("Order")

        dialog = Dialog(this)

        getData()

    }

    private fun getData() {
        var psychologRating: Double
        var psychologExperience: Int

        with(binding){

            //image
            Glide.with(this@PsychologDetailActivity)
                .load(psycholog?.image)
                .into(doctorImage)

            //name
            doctorDetailName.text = psycholog?.name

            //title
            doctorDetailTitle.text = psycholog?.title

            //rating
            psychologRating = psycholog?.rating!!
            when (psychologRating) {
                in 0.0..1.0 -> {
                    getOneStar()
                }
                in 1.1..2.0 -> {
                    getTwoStar()
                }
                in 2.1..3.0 -> {
                    getThreeStar()
                }
                in 3.1..4.0 -> {
                    getFourStar()
                }
                in 4.1..5.0 -> {
                    getFiveStar()
                }
            }

            //patient
            doctorDetailPatient.text = psycholog?.patient

            //experience
            psychologExperience = psycholog?.experience!!
            doctorDetailExperience.text = psychologExperience.toString()

            //rating text
            doctorDetailRatingText.text = psychologRating.toString()

            //about
            doctorDetailAbout.text = psycholog?.about

        }

    }

    private fun showPopUp(){

        val date = arrayOf("Mon", "Tue", "wed", "Thu", "Fri", "Sat", "Sat")

        bindingPopup = BookingpopupBinding.inflate(layoutInflater)
        dialog.setContentView(bindingPopup.root)

        val closeButton = bindingPopup.popupBack
        val buyButton = bindingPopup.buyButton

        closeButton.setOnClickListener {
            dialog.dismiss()
        }
        buyButton.setOnClickListener {

            val myOrder = MyOrder(
                psycholog?.name!!,
                psycholog?.image!!
            )
            request.child(System.currentTimeMillis().toString())
                .setValue(myOrder)
            Toast.makeText(this, "Buy Success", Toast.LENGTH_SHORT).show()
            finish()
        }
        dialog.show()
    }

    private fun getOneStar() {
        binding.doctorDetailRating.visibility = View.VISIBLE
    }

    private fun getTwoStar() {
        binding.doctorDetailRating.visibility = View.VISIBLE
        binding.doctorDetailRating2.visibility = View.VISIBLE
    }

    private fun getThreeStar() {
        binding.doctorDetailRating.visibility = View.VISIBLE
        binding.doctorDetailRating2.visibility = View.VISIBLE
        binding.doctorDetailRating3.visibility = View.VISIBLE
    }

    private fun getFourStar() {
        binding.doctorDetailRating.visibility = View.VISIBLE
        binding.doctorDetailRating2.visibility = View.VISIBLE
        binding.doctorDetailRating3.visibility = View.VISIBLE
        binding.doctorDetailRating4.visibility = View.VISIBLE
    }

    private fun getFiveStar() {
        binding.doctorDetailRating.visibility = View.VISIBLE
        binding.doctorDetailRating2.visibility = View.VISIBLE
        binding.doctorDetailRating3.visibility = View.VISIBLE
        binding.doctorDetailRating4.visibility = View.VISIBLE
        binding.doctorDetailRating5.visibility = View.VISIBLE
    }

}