package com.jmsoftwares.mesanews.controller

import android.content.Context

class SharedPreferences {

    fun setFavorite(context: Context, key: String, value: Boolean): Boolean? {
        val preferences = context.getSharedPreferences("user_preferences_favorite", Context.MODE_PRIVATE)
        return preferences.edit().putBoolean(key, value).commit()
    }

    fun getFavorite(context: Context, key: String): Boolean? {
        val preferences = context.getSharedPreferences("user_preferences_favorite", Context.MODE_PRIVATE)
        var favorite = false
        if (preferences.contains(key)) {
            favorite = preferences.getBoolean(key, false)
        }
        return favorite
    }

}