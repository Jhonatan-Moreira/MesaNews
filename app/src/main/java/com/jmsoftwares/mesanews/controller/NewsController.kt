package com.jmsoftwares.mesanews.controller

import android.content.Context
import com.google.gson.GsonBuilder
import com.jmsoftwares.mesanews.model.News
import org.json.JSONArray

class NewsController {

     fun newsMain(array: JSONArray): List<News> {
        val gson = GsonBuilder().create()
        val list = gson.fromJson(array.toString(), Array<News>::class.java).toList()
            .distinct()
            .sortedByDescending { it.published_at }

        return list
    }


     fun newsFilter(baseContext: Context ,favorite: Boolean, array: JSONArray): List<News> {
        val gson = GsonBuilder().create()
        val list = gson.fromJson(array.toString(), Array<News>::class.java).toList()
            .distinct()
            .sortedByDescending { it.published_at }

        return if (favorite) {
            val sharedPreferences = SharedPreferences()
            list.filterIndexed { index, value -> sharedPreferences.getFavorite(baseContext, value.url) == true }
        } else {
            list
        }
    }
}