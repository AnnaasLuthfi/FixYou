package com.myapps.mypsikolog.listener

import com.myapps.mypsikolog.models.User

interface UserListener {
    fun initiateVideoMeeting(user: User)

    fun initiateAudioMeeting(user: User)
}