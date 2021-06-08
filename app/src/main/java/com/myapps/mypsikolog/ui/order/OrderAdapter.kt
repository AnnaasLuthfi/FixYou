package com.myapps.mypsikolog.ui.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.myapps.mypsikolog.databinding.ListOrderBinding
import com.myapps.mypsikolog.models.MyOrder
import com.myapps.mypsikolog.models.Psycholog

class OrderAdapter(option: FirebaseRecyclerOptions<MyOrder>) :
    FirebaseRecyclerAdapter<MyOrder, OrderAdapter.OrderViewHolder>(option) {

    class OrderViewHolder(private val binding: ListOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: MyOrder) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(order.image)
                    .into(doctorImage)

                doctorName.text = order.doctorName
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val listOrderBinding =
            ListOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(listOrderBinding)
    }


    override fun onBindViewHolder(holder: OrderViewHolder, position: Int, model: MyOrder) {
        holder.bind(model)
    }

}