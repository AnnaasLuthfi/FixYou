package com.myapps.mypsikolog.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Psycholog(

    var about: String = "",
    var chatprice: Int = 0,
    var experience: Int = 0,
    var name: String = "",
    var patient: String = "",
    var rating: Double = 0.0,
    var title: String = "",
    var username: String = "",
    var videoprice: Int = 0,
    var image: String = ""

): Parcelable