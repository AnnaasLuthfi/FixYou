package com.myapps.mypsikolog.ui.order

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.myapps.mypsikolog.databinding.ActivityOrderBinding
import com.myapps.mypsikolog.models.Psycholog


class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var psycholog: Psycholog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "My Order"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //firebase
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Order")
        binding.orderRecycler.setHasFixedSize(true)
        binding.orderRecycler.layoutManager = LinearLayoutManager(this)

        loadOrder(psycholog.name)

    }

    private fun loadOrder(name: String) {
        TODO("Not yet implemented")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}