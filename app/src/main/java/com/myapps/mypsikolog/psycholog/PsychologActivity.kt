package com.myapps.mypsikolog.psycholog

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*
import com.myapps.mypsikolog.databinding.ActivityPsychologBinding
import com.myapps.mypsikolog.models.Psycholog
import java.util.*

class PsychologActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPsychologBinding
    private lateinit var dbref: DatabaseReference
    private lateinit var adapter: PsychologAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPsychologBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarSearch)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.psychologRecycler.setHasFixedSize(true)
        binding.psychologRecycler.layoutManager = LinearLayoutManager(this)
        dbref = FirebaseDatabase.getInstance().getReference("Psycholog")


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView = binding.searchBar

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.isIconified = false

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query?.isNotEmpty() == true) {
                    val search: String = query.toString()
                    loadFirebaseData(search)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return true
    }

    private fun loadFirebaseData(search: String) {


        val firebaseSearchQuery =
            dbref.orderByChild("name").startAt(search).endAt(search + "\uf0ff")

        val option = FirebaseRecyclerOptions.Builder<Psycholog>()
            .setQuery(firebaseSearchQuery, Psycholog::class.java)
            .setLifecycleOwner(this)
            .build()
        adapter = PsychologAdapter(option)

        binding.psychologRecycler.adapter = adapter
        binding.psychologRecycler.adapter?.notifyDataSetChanged()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}