package com.jmsoftwares.mesanews.view

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.volley.Response
import com.android.volley.VolleyError
import com.google.gson.GsonBuilder
import com.jmsoftwares.mesanews.R
import com.jmsoftwares.mesanews.api.NewsRequests
import com.jmsoftwares.mesanews.controller.NewsController
import com.jmsoftwares.mesanews.controller.SharedPreferences
import com.jmsoftwares.mesanews.model.News
import com.jmsoftwares.mesanews.util.Constants
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class Filter : AppCompatActivity() {

    private var cal = Calendar.getInstance()
    private var adapter: AdapterNews? = null
    private var recyclerView: RecyclerView? = null
    private var buttonfilter_date: Button? = null
    private var buttonfilter_favorite: Button? = null
    private var filter_textview_search: TextView? = null
    private var filter_button_search: ImageButton? = null
    private var filter_search: LinearLayout? = null
    private var newsController = NewsController()
    private lateinit var currentList: List<News>
    private val shared = SharedPreferences()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filter_activity)

        var token = intent.getStringExtra("token").toString()
        setupComponents(token)
    }

    fun setupComponents(token: String) {

        val actionbar: ActionBar = supportActionBar!!
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setHomeButtonEnabled(true)

        buttonfilter_date = findViewById(R.id.filter_button_date)
        buttonfilter_favorite = findViewById(R.id.filter_button_favorite)
        filter_textview_search = findViewById(R.id.filter_textview_search)
        filter_button_search = findViewById(R.id.filter_button_search)
        filter_search = findViewById(R.id.filter_search)

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView(token)
            }

        buttonfilter_date!!.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
               ).show()
        }

        buttonfilter_favorite!!.setOnClickListener {
            getNews(token, false, true, "", this)
        }

        filter_button_search!!.setOnClickListener {
            if(filter_textview_search.toString().isNotEmpty() && currentList.isNotEmpty()) {
                var list = currentList.filterIndexed { index, value ->
                    value.title.contains(filter_textview_search?.text.toString(), true)
                }
                adapter = AdapterNews(list, this, shared)
                recyclerView = findViewById(R.id.filter_recycleView)
                configureRecyclerView()
            }else{
                Toast.makeText(this, R.string.filter_blank_search, Toast.LENGTH_LONG).show()

            }
        }
    }

    private fun getNews(token: String, requestDate: Boolean, favorite: Boolean, date: String, activity: Activity) {
        filter_search?.visibility = View.GONE
        val responseOK: Response.Listener<JSONObject> =
            Response.Listener { response: JSONObject ->
                val array = response.getJSONArray("data")

                currentList = newsController.newsFilter(baseContext, favorite, array)

                if(currentList.isNotEmpty()){
                adapter = AdapterNews(currentList, activity, shared)
                recyclerView = findViewById(R.id.filter_recycleView)
                configureRecyclerView()

                    filter_search?.visibility = View.VISIBLE
                }else{
                    filter_search?.visibility = View.GONE
                }
            }

        val responseError = Response.ErrorListener { error: VolleyError ->
            println("Fail =" + error.message)
            filter_search?.visibility = View.GONE
        }

        val request = NewsRequests()
        if (requestDate) {
            request.newsRequest(
                baseContext,
                Constants.requestConstants.newsUrl + date,
                responseOK,
                responseError,
                token
            )
        } else {
            request.newsRequest(
                baseContext,
                Constants.requestConstants.newsUrl,
                responseOK,
                responseError,
                token
            )
        }
    }

    private fun configureRecyclerView() {
        recyclerView?.adapter = adapter
        val layoutManager = StaggeredGridLayoutManager(
            1, StaggeredGridLayoutManager.VERTICAL,
        )
        recyclerView?.layoutManager = layoutManager
        recyclerView?.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }


    private fun updateDateInView(token: String) {
        val myFormat = "yyyy/MM/dd" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())

        getNews(token, true, false,  sdf.format(cal.time), this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}