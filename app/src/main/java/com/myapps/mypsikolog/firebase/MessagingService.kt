package com.myapps.mypsikolog.firebase

import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.myapps.mypsikolog.ui.calling.IncomingInvitationActivity
import com.myapps.mypsikolog.utils.Constants.Companion.EMAIL_PATIENTS
import com.myapps.mypsikolog.utils.Constants.Companion.NAME_PATIENTS
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_INVITATION
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_INVITATION_RESPONSE
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_INVITER_TOKEN
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_MEETING_ROOM
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_MEETING_TYPE
import com.myapps.mypsikolog.utils.Constants.Companion.REMOTE_MSG_TYPE

class MessagingService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val type: String = remoteMessage.data[REMOTE_MSG_TYPE].toString()

        if (type != null) {
            if (type == REMOTE_MSG_INVITATION) {
                val intent = Intent(applicationContext, IncomingInvitationActivity::class.java)
                intent.putExtra(REMOTE_MSG_MEETING_TYPE, remoteMessage.data[REMOTE_MSG_MEETING_TYPE])
                intent.putExtra(NAME_PATIENTS, remoteMessage.data[NAME_PATIENTS])
                intent.putExtra(EMAIL_PATIENTS, remoteMessage.data[EMAIL_PATIENTS])
                intent.putExtra(REMOTE_MSG_INVITER_TOKEN, remoteMessage.data[REMOTE_MSG_INVITER_TOKEN])
                intent.putExtra(REMOTE_MSG_INVITER_TOKEN, remoteMessage.data[REMOTE_MSG_INVITER_TOKEN])
                intent.putExtra(REMOTE_MSG_MEETING_ROOM, remoteMessage.data[REMOTE_MSG_MEETING_ROOM])       // videocall
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else if (type.equals(REMOTE_MSG_INVITATION_RESPONSE)){
                val intent = Intent(REMOTE_MSG_INVITATION_RESPONSE)
                intent.putExtra(REMOTE_MSG_INVITATION_RESPONSE, remoteMessage.data[REMOTE_MSG_INVITATION_RESPONSE])
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            }
        }

    }
}