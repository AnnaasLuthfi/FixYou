package com.myapps.mypsikolog.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MyOrder(

    var doctorName: String = "",
    var date: String = "",
//    var time: String = "",
//    var consultType: String = "",
    var status: String = "0",

): Parcelable