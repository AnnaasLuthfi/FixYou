package com.myapps.mypsikolog.psycholog

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.myapps.mypsikolog.databinding.ListItemPsychologBinding
import com.myapps.mypsikolog.models.Psycholog

class PsychologAdapter(options: FirebaseRecyclerOptions<Psycholog>) :
    FirebaseRecyclerAdapter<Psycholog, PsychologAdapter.PyschologViewHolder>(
        options
    ) {

    public var listPsycholog = ArrayList<Psycholog>()

    class PyschologViewHolder(private val binding: ListItemPsychologBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var psychologExperience: Int = 0
        private var psychologRating: Double = 0.0
        private var chat: Int = 0
        private var video: Int = 0

        fun bind(psycholog: Psycholog) {
            with(binding) {

                //doctor image
                Glide.with(itemView.context)
                    .load(psycholog.image)
                    .into(binding.doctorImage)

                //doctor name
                doctorName.text = psycholog.name

                //doctor experience
                psychologExperience = psycholog.experience
                doctorExperience.text = psychologExperience.toString()

                //doctor rating
                psychologRating = psycholog.rating
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

                //chat price
                chat = psycholog.chatprice
                chatPrice.text = chat.toString()

                //video price
                video = psycholog.videoprice
                videoPrice.text = video.toString()

                bookingButton.setOnClickListener {
                    val intent = Intent(itemView.context, PsychologDetailActivity::class.java)
                    intent.putExtra(PsychologDetailActivity.EXTRA_PSYCHOLOG, psycholog)
                    itemView.context.startActivity(intent)
                }

            }
        }

        private fun getOneStar() {
            binding.doctorRating1.visibility = View.VISIBLE
        }

        private fun getTwoStar() {
            binding.doctorRating1.visibility = View.VISIBLE
            binding.doctorRating2.visibility = View.VISIBLE
        }

        private fun getThreeStar() {
            binding.doctorRating1.visibility = View.VISIBLE
            binding.doctorRating2.visibility = View.VISIBLE
            binding.doctorRating3.visibility = View.VISIBLE
        }

        private fun getFourStar() {
            binding.doctorRating1.visibility = View.VISIBLE
            binding.doctorRating2.visibility = View.VISIBLE
            binding.doctorRating3.visibility = View.VISIBLE
            binding.doctorRating4.visibility = View.VISIBLE
        }

        private fun getFiveStar() {
            binding.doctorRating1.visibility = View.VISIBLE
            binding.doctorRating2.visibility = View.VISIBLE
            binding.doctorRating3.visibility = View.VISIBLE
            binding.doctorRating4.visibility = View.VISIBLE
            binding.doctorRating5.visibility = View.VISIBLE
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PyschologViewHolder {
        val listItemPsychologyBinding: ListItemPsychologBinding =
            ListItemPsychologBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PyschologViewHolder(listItemPsychologyBinding)
    }

    override fun onBindViewHolder(holder: PyschologViewHolder, position: Int, model: Psycholog) {
//        val psycholog = listPsycholog[position]
        holder.bind(model)
    }

//    override fun getItemCount(): Int = listPsycholog.size
}