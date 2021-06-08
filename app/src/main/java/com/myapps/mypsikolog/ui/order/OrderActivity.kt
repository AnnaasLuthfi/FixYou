package com.myapps.mypsikolog.ui.order

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.auth.User
import com.myapps.mypsikolog.databinding.ActivityOrderBinding
import com.myapps.mypsikolog.models.MyOrder
import com.myapps.mypsikolog.models.Psycholog


class OrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var psycholog: Psycholog
    private lateinit var adapter: OrderAdapter
    private lateinit var user: com.myapps.mypsikolog.models.User

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

        loadOrder()

    }

    private fun loadOrder() {


        val option = FirebaseRecyclerOptions.Builder<MyOrder>()
            .setQuery(reference, MyOrder::class.java)
            .setLifecycleOwner(this)
            .build()

        adapter = OrderAdapter(option)

        binding.orderRecycler.adapter = adapter
        binding.orderRecycler.adapter?.notifyDataSetChanged()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}