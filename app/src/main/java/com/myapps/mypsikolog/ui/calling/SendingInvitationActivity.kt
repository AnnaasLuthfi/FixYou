package com.myapps.mypsikolog.ui.calling

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.iid.FirebaseInstanceId
import com.myapps.mypsikolog.R
import com.myapps.mypsikolog.databinding.ActivitySendingInvitationBinding
import com.myapps.mypsikolog.models.User
import com.myapps.mypsikolog.network.ApiClient
import com.myapps.mypsikolog.network.ApiService
import com.myapps.mypsikolog.utils.Constants
import com.myapps.mypsikolog.utils.Constants.Companion.EMAIL_PATIENTS
import com.myapps.mypsikolog.utils.Constants.Companion.KEY_USER_ID
import com.myapps.mypsikolog.utils.Constants.Companion.NAME_PATIENTS
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_DATA
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_INVITATION
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_INVITATION_ACCEPTED
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_INVITATION_CANCELLED
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_INVITATION_REJECTED
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_INVITATION_RESPONSE
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_INVITER_TOKEN
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_MEETING_ROOM
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_MEETING_TYPE
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_REGISTRATION_IDS
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_TYPE
import com.myapps.mypsikolog.utils.PreferenceManager
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL
import java.util.*

class SendingInvitationActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySendingInvitationBinding
    private lateinit var preferenceManager: PreferenceManager
    private var inviterToken: String? = null
    private var meetingRoom: String? = null
    private var meetingType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendingInvitationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        preferenceManager = PreferenceManager(applicationContext)


        // munculin tipe Logo Meet
        meetingType = intent.getStringExtra("type")
        if (meetingType != null) {
            if (meetingType == "video") {
                binding.typeLogoMeeting.setImageResource(R.drawable.ic_baseline_videocam_24)
            } else {
                binding.typeLogoMeeting.setImageResource(R.drawable.ic_baseline_phone_24)
            }
        }

        // view data user
        val user = intent.getParcelableExtra<User>("user") as User
        if (user != null) {
            binding.textFirstCharName.text = user.userName.substring(0,1)
            binding.textUsername.text = user.userName
        }

        // rejected button
        binding.buttonRejectedInvitation.setOnClickListener {
            if (user != null) {
                cancelInvitation(user.token)
            }
        }

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    inviterToken = task.result!!.token

                    /////// kirim data ke incoming dan nyesuain berdasarkan token /////////////
                    if (meetingType != null && user != null) {
                        initiateMeeting(meetingType!!, user.token)
                    }

                }

            }

    }

    private fun sendRemoteMessage(remoteMessageBody: String, type: String) {
        ApiClient.getClient()?.create(ApiService::class.java)?.sendRemoteMessage(
            Constants.getRemoteMessageHeaders(), remoteMessageBody)?.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful){
                    if (type == REMOTE_MSG_INVITATION){
                        Toast.makeText(this@SendingInvitationActivity, "Invitation sent successfully", Toast.LENGTH_SHORT).show()
                    } else if (type == REMOTE_MSG_INVITATION_RESPONSE){
                        Toast.makeText(this@SendingInvitationActivity, "Invitation Cancelled", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    Toast.makeText(this@SendingInvitationActivity, response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@SendingInvitationActivity, t.message, Toast.LENGTH_SHORT).show()
                finish()
            }

        })
    }

    private fun initiateMeeting(meetingType: String, receiverToken: String) {
        try {
            val tokens = JSONArray()
            tokens.put(receiverToken)

            val body = JSONObject()
            val data = JSONObject()

            data.put(REMOTE_MSG_TYPE, REMOTE_MSG_INVITATION)
            data.put(REMOTE_MSG_MEETING_TYPE, meetingType)
            data.put(NAME_PATIENTS, preferenceManager.getString(NAME_PATIENTS))
            data.put(EMAIL_PATIENTS, preferenceManager.getString(EMAIL_PATIENTS))
            data.put(REMOTE_MSG_INVITER_TOKEN, inviterToken)

//            videocall
            meetingRoom = preferenceManager.getString(KEY_USER_ID) + "_" + UUID.randomUUID().toString().substring(0,5)
            data.put(REMOTE_MSG_MEETING_ROOM, meetingRoom)

            body.put(REMOTE_MSG_DATA, data)
            body.put(REMOTE_MSG_REGISTRATION_IDS, tokens)

            sendRemoteMessage(body.toString(), REMOTE_MSG_INVITATION)

        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun cancelInvitation(receiverToken: String) {
        try {
            val tokens = JSONArray()
            tokens.put(receiverToken)

            val body = JSONObject()
            val data = JSONObject()

            data.put(REMOTE_MSG_TYPE, REMOTE_MSG_INVITATION_RESPONSE)
            data.put(REMOTE_MSG_INVITATION_RESPONSE, REMOTE_MSG_INVITATION_CANCELLED)

            body.put(REMOTE_MSG_DATA, data)
            body.put(REMOTE_MSG_REGISTRATION_IDS, tokens)

            sendRemoteMessage(body.toString(), REMOTE_MSG_INVITATION_RESPONSE)

        } catch (e: java.lang.Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    //Respon dari Invitation yang diberi dr Incoming dan ketika incoming menyetujui / menolak maka di bagian sending juga kana merespon
    private var invitationResponseReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val type: String = intent?.getStringExtra(REMOTE_MSG_INVITATION_RESPONSE).toString()
            if (type != null) {
                if (type == REMOTE_MSG_INVITATION_ACCEPTED) {
                    try {
                        val serverURL = URL("https://meet.jit.si")

                        val builder = JitsiMeetConferenceOptions.Builder()
                        builder.setServerURL(serverURL)
                        builder.setWelcomePageEnabled(false)
                        builder.setRoom(meetingRoom)
                        if (meetingType.equals("audio")) {
                            builder.setVideoMuted(true)
                        }

                        JitsiMeetActivity.launch(this@SendingInvitationActivity, builder.build())
                        finish()
                    } catch (e: Exception) {
                        Toast.makeText(this@SendingInvitationActivity, e.message, Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else if (type == REMOTE_MSG_INVITATION_REJECTED) {
                    Toast.makeText(context, "Invitation Rejected", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(invitationResponseReceiver, IntentFilter(REMOTE_MSG_INVITATION_RESPONSE))
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(applicationContext).unregisterReceiver(invitationResponseReceiver)
    }


}