package com.myapps.mypsikolog.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.myapps.mypsikolog.databinding.ItemContainerUserBinding
import com.myapps.mypsikolog.models.User

class UserAdapter(private val list: ArrayList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(private val binding: ItemContainerUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {

            with(binding) {
                textFirstChar.text = user.firstName.substring(0, 1)
                username.text = user.name
                email.text = user.email
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemContainerUserBinding: ItemContainerUserBinding =
            ItemContainerUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(itemContainerUserBinding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = list[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = list.size
}