package com.myapps.mypsikolog.consult

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.myapps.mypsikolog.adapters.UserAdapter
import com.myapps.mypsikolog.databinding.ActivityConsultBinding
import com.myapps.mypsikolog.models.User
import com.myapps.mypsikolog.utils.Constants
import com.myapps.mypsikolog.utils.PreferenceManager
import java.util.*
import kotlin.collections.ArrayList


class ConsultActivity : AppCompatActivity() {

    private lateinit var preferenceManager: PreferenceManager
    lateinit var binding: ActivityConsultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConsultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarConsult)

        supportActionBar?.title = "Consult"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        preferenceManager = PreferenceManager(this)

        //recycler
        binding.consultRecycler.setHasFixedSize(true)
        binding.consultRecycler.layoutManager = LinearLayoutManager(this)
        binding.consultRecycler.addItemDecoration(DividerItemDecoration(binding.consultRecycler.context, DividerItemDecoration.VERTICAL))

        getUsers()

    }

    private fun getUsers() {
        val listItem = ArrayList<User>()
        val database: FirebaseFirestore = FirebaseFirestore.getInstance()
        database.collection(Constants.KEY_PATIENTS)
            .get()
            .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot?> {
                override fun onComplete(task: Task<QuerySnapshot?>) {
                    val myUserId: String = preferenceManager.getString(Constants.KEY_USER_ID)
                    if (task.isSuccessful && task.result != null) {

                        for (documentSnapshot in task.result!!) {
                            if (myUserId == documentSnapshot.id) {
                                continue
                            }
                            val user = User()
                            user.firstName = documentSnapshot.getString(Constants.NAME_PATIENTS)!!
                            user.name = documentSnapshot.getString(Constants.NAME_PATIENTS)!!
                            user.email = documentSnapshot.getString(Constants.EMAIL_PATIENTS)!!
                            user.token = documentSnapshot.getString(Constants.KEY_FCM_TOKEN)!!
                            listItem.add(user)
                        }

                        val adapter = UserAdapter(listItem)

                        binding.consultRecycler.adapter = adapter
                        binding.consultRecycler.adapter?.notifyDataSetChanged()

                        if (listItem.isEmpty()) {
                            Toast.makeText(this@ConsultActivity, "gk ada data", Toast.LENGTH_SHORT)
                                .show()
                        }

                        Log.d("TEST DATA", "onComplete: ${listItem[0].email}")


                    } else {
                        Toast.makeText(this@ConsultActivity, "error", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

}