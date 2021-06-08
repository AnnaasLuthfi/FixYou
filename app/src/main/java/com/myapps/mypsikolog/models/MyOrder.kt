package com.myapps.mypsikolog.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MyOrder(

    var doctorName: String = "",
    var image: String = "",
    var status: String = "0",
    var userEmai: String = ""

): Parcelable