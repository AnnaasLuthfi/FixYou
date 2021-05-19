package com.myapps.mypsikolog.home

import android.content.SearchRecentSuggestionsProvider

class DoctorSuggestions: SearchRecentSuggestionsProvider() {


    companion object{
        const val AUTHORITY: String = "com.myapps.mypsikolog.home.DoctorSuggestions"
        const val MODE: Int = DATABASE_MODE_QUERIES
    }


    fun DoctorSuggestions(){
        setupSuggestions(AUTHORITY, MODE)
    }

}