package com.myapps.mypsikolog.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.myapps.mypsikolog.databinding.ItemContainerUserBinding
import com.myapps.mypsikolog.listener.UserListener
import com.myapps.mypsikolog.models.User

class UserAdapter(val context: Context)  : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private lateinit var userListener: UserListener
    private lateinit var list: List<User>

    constructor(context: Context, list: List<User>, userListener: UserListener): this(context) {
        this.list = list
        this.userListener = userListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemContainerUserBinding: ItemContainerUserBinding = ItemContainerUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(itemContainerUserBinding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = list[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = list.size

    inner class UserViewHolder(private val binding: ItemContainerUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {

            with(binding) {
                textFirstChar.text = user.userName.substring(0, 1)
                username.text = user.name
                email.text = user.email

                imageButtonVideo.setOnClickListener {
                    userListener.initiateVideoMeeting(user)
                }

                imageButtonAudio.setOnClickListener {
                    userListener.initiateAudioMeeting(user)
                }
            }

        }

    }
}
