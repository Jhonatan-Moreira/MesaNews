package com.jmsoftwares.mesanews.api

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.util.*

class NewsRequests {

     fun newsRequest(
        context: Context,
        url: String,
        succesListener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener,
        token: String) {

        var queue: RequestQueue? = Volley.newRequestQueue(context, HurlStack())

        val jsonObjectRequest: JsonObjectRequest =
            object : JsonObjectRequest(Method.GET, url, null, succesListener, errorListener) {
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    headers["Authorization"] = "Bearer $token"
                    return headers
                }
            }

        queue?.add(jsonObjectRequest)
    }
}