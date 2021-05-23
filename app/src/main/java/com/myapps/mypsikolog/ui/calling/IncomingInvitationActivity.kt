package com.myapps.mypsikolog.ui.calling

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.myapps.mypsikolog.R
import com.myapps.mypsikolog.databinding.ActivityIncomingInvitationBinding
import com.myapps.mypsikolog.network.ApiClient
import com.myapps.mypsikolog.network.ApiService
import com.myapps.mypsikolog.utils.Constants
import com.myapps.mypsikolog.utils.Constants.Companion.NAME_PATIENTS
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_INVITATION_ACCEPTED
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_INVITATION_CANCELLED
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_INVITATION_REJECTED
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_INVITATION_RESPONSE
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_INVITER_TOKEN
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_MEETING_ROOM
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_MEETING_TYPE
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_TYPE
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.net.URL

class IncomingInvitationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIncomingInvitationBinding
    var meetingType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncomingInvitationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        meetingType = intent.getStringExtra(REMOTE_MSG_MEETING_TYPE)

        // merubah logo berdasarkan tipe calling ///
        if (meetingType != null) {
            if (meetingType.equals("video")) {
                binding.typeLogoMeeting.setImageResource(R.drawable.ic_baseline_videocam_24)
            } else {
                binding.typeLogoMeeting.setImageResource(R.drawable.ic_baseline_phone_24)
            }
        }

        ///munculin data username sending atau si penelpon ///////////
        val userName = intent.getStringExtra(NAME_PATIENTS)
        if (userName != null){
            binding.textFirstCharName.text = userName.substring(0,1)
        }

        binding.textUsername.text = String.format("%s", userName, intent.getStringExtra(NAME_PATIENTS))

        // button accepted
        binding.buttonAcceptedInvitation.setOnClickListener {
            sendInvitationResponse(REMOTE_MSG_INVITATION_ACCEPTED, intent.getStringExtra(REMOTE_MSG_INVITER_TOKEN).toString())
        }

        // button rejected
        binding.buttonRejectedInvitation.setOnClickListener {
            sendInvitationResponse(REMOTE_MSG_INVITATION_REJECTED, intent.getStringExtra(REMOTE_MSG_INVITER_TOKEN).toString())
        }
    }


    private fun sendRemoteMessage(remoteMessageBody: String, type: String) {
        ApiClient.getClient()?.create(ApiService::class.java)?.sendRemoteMessage(
            Constants.getRemoteMessageHeaders(), remoteMessageBody)?.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful){
                    if (type == REMOTE_MSG_INVITATION_ACCEPTED) {
                        try {
                            val serverURL = URL("https://meet.jit.si")
                            val builder = JitsiMeetConferenceOptions.Builder()
                            builder.setServerURL(serverURL)
                            builder.setWelcomePageEnabled(false)
                            builder.setRoom(intent.getStringExtra(REMOTE_MSG_MEETING_ROOM))
                            if (meetingType.equals("audio")){
                                builder.setVideoMuted(true)
                            }

                            JitsiMeetActivity.launch(this@IncomingInvitationActivity, builder.build())
                            finish()

                        } catch (e: Exception) {
                            Toast.makeText(this@IncomingInvitationActivity, e.message, Toast.LENGTH_SHORT).show()
                            finish()
                        }

                    }else {
                        Toast.makeText(this@IncomingInvitationActivity, "Invitation Rejected", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } else {
                    Toast.makeText(this@IncomingInvitationActivity, response.message(), Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@IncomingInvitationActivity, t.message, Toast.LENGTH_SHORT).show()
                finish()
            }

        })
    }

    private fun sendInvitationResponse(type: String, receiverToken: String) {
        try {
            val tokens = JSONArray()
            tokens.put(receiverToken)

            val body = JSONObject()
            val data = JSONObject()

            data.put(REMOTE_MSG_TYPE, REMOTE_MSG_INVITATION_RESPONSE)
            data.put(REMOTE_MSG_INVITATION_RESPONSE, type)

            body.put(Constants.REMOTE_MSG_DATA, data)
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens)

            sendRemoteMessage(body.toString(), type)

        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    //Respon dari Invitation yang diberi dr sending dan ketika sending  menolak maka di bagian sending juga kana merespon
    private var invitationResponseReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val type: String = intent.getStringExtra(REMOTE_MSG_INVITATION_RESPONSE)!!
            if (type != null) {
                if (type == REMOTE_MSG_INVITATION_CANCELLED) {
                    Toast.makeText(context, "Invitation Cancelled", Toast.LENGTH_SHORT).show()
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