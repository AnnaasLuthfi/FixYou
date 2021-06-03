package com.myapps.mypsikolog.consult

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.myapps.mypsikolog.adapters.UserAdapter
import com.myapps.mypsikolog.databinding.ActivityConsultBinding
import com.myapps.mypsikolog.listener.UserListener
import com.myapps.mypsikolog.models.User
import com.myapps.mypsikolog.ui.calling.SendingInvitationActivity
import com.myapps.mypsikolog.utils.Constants
import com.myapps.mypsikolog.utils.PreferenceManager
import java.util.*
import kotlin.collections.ArrayList


class ConsultActivity : AppCompatActivity(), UserListener {

    private lateinit var binding: ActivityConsultBinding
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var adapter : UserAdapter
    private val listItem = ArrayList<User>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConsultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarConsult)

        supportActionBar?.title = "Consult"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        preferenceManager = PreferenceManager(this)

        //recyclerView
        binding.consultRecycler.setHasFixedSize(true)
        binding.consultRecycler.layoutManager = LinearLayoutManager(this)
        adapter = UserAdapter(this, listItem, this)
        binding.consultRecycler.adapter = adapter
//        binding.consultRecycler.addItemDecoration(DividerItemDecoration(binding.consultRecycler.context, DividerItemDecoration.VERTICAL))

        getUsers()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun initiateVideoMeeting(user: User) {
        if (user.token == null || user.token.trim().isEmpty()){
            Toast.makeText(this, "${user.userName} is not available for meeting", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(applicationContext, SendingInvitationActivity::class.java)
            intent.putExtra("user", user)
            intent.putExtra("type", "video")
            startActivity(intent)
        }
    }

    override fun initiateAudioMeeting(user: User) {
        if (user.token == null || user.token.trim().isEmpty()){
            Toast.makeText(this, "${user.userName} is not available for meeting", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(applicationContext, SendingInvitationActivity::class.java)
            intent.putExtra("user", user)
            intent.putExtra("type", "audio")
            startActivity(intent)
        }
    }

    private fun getUsers() {
        val database: FirebaseFirestore = FirebaseFirestore.getInstance()
        database.collection(Constants.KEY_PATIENTS)
            .get()
            .addOnCompleteListener { task ->
                val myUserId: String = preferenceManager.getString(Constants.KEY_USER_ID)
                if (task.isSuccessful && task.result != null) {

                    for (documentSnapshot in task.result!!) {
                        if (myUserId == documentSnapshot.id) {
                            continue
                        }
                        val user = User()
                        user.userName = documentSnapshot.getString(Constants.NAME_PATIENTS).toString()
                        user.name = documentSnapshot.getString(Constants.NAME_PATIENTS).toString()
                        user.email = documentSnapshot.getString(Constants.EMAIL_PATIENTS).toString()
                        user.token = documentSnapshot.getString(Constants.KEY_FCM_TOKEN).toString()
                        listItem.add(user)
                    }

                    if (listItem.isEmpty()) {
                        Toast.makeText(this@ConsultActivity, "No Data", Toast.LENGTH_SHORT).show()
                    } else {
                        adapter.notifyDataSetChanged()
                    }

                    Log.d("TEST DATA", "onComplete: ${listItem[0].email}")


                } else {
                    Toast.makeText(this@ConsultActivity, "error", Toast.LENGTH_SHORT).show()
                }
            }
    }

}