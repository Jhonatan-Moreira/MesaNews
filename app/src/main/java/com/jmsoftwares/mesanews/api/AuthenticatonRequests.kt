package com.jmsoftwares.mesanews.api

import android.content.Context
import androidx.lifecycle.ViewModel
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.HurlStack
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.util.*

class AuthenticatonRequests: ViewModel() {

     fun signinRequest(
        context: Context,
        url: String,
        succesListener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener,
        email: String,
        password: String) {

        var queue: RequestQueue? = Volley.newRequestQueue(context, HurlStack())

        val bodyObject = JSONObject()

        bodyObject.put("email", email)
        bodyObject.put("password", password)

        val jsonObjectRequest: JsonObjectRequest =
            object : JsonObjectRequest(Method.POST, url, bodyObject, succesListener, errorListener) {
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    return headers
                }
            }

        queue?.add(jsonObjectRequest)
    }


     fun signupRequest(
        context: Context,
        url: String,
        succesListener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener,
        name: String,
        email: String,
        password: String) {

        var queue: RequestQueue? = Volley.newRequestQueue(context, HurlStack())

        val bodyObject = JSONObject()

        bodyObject.put("name", name)
        bodyObject.put("email", email)
        bodyObject.put("password", password)

        val jsonObjectRequest: JsonObjectRequest =
            object : JsonObjectRequest(Method.POST, url, bodyObject, succesListener, errorListener) {
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-Type"] = "application/json"
                    return headers
                }
            }

        queue?.add(jsonObjectRequest)
    }
}