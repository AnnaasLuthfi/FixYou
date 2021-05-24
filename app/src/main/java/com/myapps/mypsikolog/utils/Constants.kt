package com.myapps.mypsikolog.utils

class Constants {
    companion object {
        const val KEY_PATIENTS = "patients"
        const val NAME_PATIENTS = "NameUser"
        const val EMAIL_PATIENTS = "Email"
        const val PHONE_PATIENTS = "Phone"
        const val PASSWORD_PATIENTS = "Password"
        const val ADDRESS_PATIENTS = "Address"
        const val AGE_PATIENTS = "Age"
        const val GENDER_PATIENTS = "Gender"

        const val KEY_USER_ID = "userId"
        const val KEY_FCM_TOKEN = "fcmToken"

        const val KEY_PREFERENCE_NAME = "MyPsikologi"
        const val KEY_IS_SIGNED_IN = "isSignedIn"

        const val REMOTE_MSG_AUTHORIZATION = "Authorization"
        const val REMOTE_MSG_CONTENT_TYPE = "Content-Type"

        const val REMOTE_MSG_TYPE = "type"
        const val REMOTE_MSG_INVITATION = "invitation"
        const val REMOTE_MSG_MEETING_TYPE = "meetingType"
        const val REMOTE_MSG_INVITER_TOKEN = "inviterToken"
        const val REMOTE_MSG_DATA = "data"
        const val REMOTE_MSG_REGISTRATION_IDS = "registration_ids"

        const val REMOTE_MSG_INVITATION_RESPONSE = "invitationResponse"

        const val REMOTE_MSG_INVITATION_ACCEPTED = "accepted"
        const val REMOTE_MSG_INVITATION_REJECTED = "rejected"
        const val REMOTE_MSG_INVITATION_CANCELLED = "cancelled"

        const val REMOTE_MSG_MEETING_ROOM = "meetingRoom"

        fun getRemoteMessageHeaders(): HashMap<String, String> {
            val headers = HashMap<String, String>()
            headers[REMOTE_MSG_AUTHORIZATION] = "key=AAAAsvyEyM4:APA91bGcqUZONdlqU9OvOBcxIXA8sdYt9uyiM0tuPNrHolnHF5pcB37AC_Bks6ItnaMt0bMTIU6lUqfrWHl_04-dWcfIXHvyE2aaMVTPoADj88NpGj19H0_RO3rmfWmraceeHH5yevBL"
            headers[REMOTE_MSG_CONTENT_TYPE] = "application/json"
            return headers
        }
    }
}