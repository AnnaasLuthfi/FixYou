package com.myapps.mypsikolog.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(

    var userName: String = "",
    var name: String = "",
    var email: String = "",
    var token: String = ""

) : Parcelable