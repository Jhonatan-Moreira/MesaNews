package com.jmsoftwares.mesanews.view

import android.app.Activity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.volley.Response
import com.android.volley.VolleyError
import com.jmsoftwares.mesanews.R
import com.jmsoftwares.mesanews.api.NewsRequests
import com.jmsoftwares.mesanews.controller.NewsController
import com.jmsoftwares.mesanews.controller.SharedPreferences
import com.jmsoftwares.mesanews.model.News
import com.jmsoftwares.mesanews.util.Constants
import com.jmsoftwares.mesanews.util.Navigation
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ViewListener
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private var adapter: AdapterNews? = null
    private var recyclerView: RecyclerView? = null
    private var newsControllerController = NewsController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val token: String = intent.getStringExtra("token").toString()

        val fab: View = findViewById(R.id.main_fab)
        fab.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("token",token)
            val navigation = Navigation()
            navigation.goToActivityParams(this, Filter::class.java, bundle)
        }

        reloadNews(token, this)
    }


    private fun getNewsHighlights(token: String) {
        val responseOK: Response.Listener<JSONObject> =
            Response.Listener { response: JSONObject ->
                val array = response.getJSONArray("data")
                carouselView(newsControllerController.newsMain(array))
            }

        val responseError = Response.ErrorListener { error: VolleyError ->
            println("request fail =" + error.message)
        }

        val request = NewsRequests()
        request.newsRequest(
            baseContext,
            Constants.requestConstants.highlightsUrl,
            responseOK,
            responseError,
            token
        )
    }

    private fun getNews(token: String, activity: Activity) {
        val responseOK: Response.Listener<JSONObject> =
            Response.Listener { response: JSONObject ->
                val array = response.getJSONArray("data")
                val shared = SharedPreferences()
                adapter = AdapterNews(newsControllerController.newsMain(array), activity, shared)
                recyclerView = findViewById(R.id.main_recycleView)
                configureRecyclerView()
            }

        val responseError = Response.ErrorListener { error: VolleyError ->
            println("request fail =" + error.message)
        }

        val request = NewsRequests()
        request.newsRequest(
            baseContext,
            Constants.requestConstants.newsUrl,
            responseOK,
            responseError,
            token
        )
    }

    private fun carouselView(news: List<News>) {

        val carouselView = findViewById<CarouselView>(R.id.mainCarouselView)
        val viewListener =
            ViewListener { position ->
                val customView: View = layoutInflater.inflate(R.layout.carousel_layout, null)
                val textView = customView.findViewById<View>(R.id.carouselTextView) as TextView
                val imageView = customView.findViewById<View>(R.id.carouselImageView) as ImageView

                Picasso.get().load(news.get(position).image_url.replace("\\", ""))
                    .resize(700, 400)
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_placeholder)
                    .into(imageView)
                textView.text = news.get(position).title
                carouselView.indicatorGravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
                customView
            }

        carouselView.setImageClickListener { position ->
            val bundle = Bundle()
            bundle.putString("url", news[position].url)
            bundle.putString("title", news[position].title)
            val navigation = Navigation()
            navigation.goToActivityParams(this, WebViewActivity::class.java, bundle)
        }

        carouselView.setViewListener(viewListener)
        carouselView.pageCount = news.size - 1
        carouselView.playCarousel()
    }

    private fun configureRecyclerView() {
        recyclerView?.adapter = adapter
        val layoutManager = StaggeredGridLayoutManager(
            1, StaggeredGridLayoutManager.VERTICAL,
        )
        recyclerView?.layoutManager = layoutManager
        recyclerView?.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }

   private fun reloadNews(token: String, activity: Activity){
        val timer = object: CountDownTimer(Long.MAX_VALUE, 30000) {
            override fun onTick(millisUntilFinished: Long) {
                getNewsHighlights(token)
                getNews(token, activity)
                Toast.makeText(activity, R.string.main_reload, Toast.LENGTH_LONG).show()
            }
            override fun onFinish() {
                // do something
            }
        }
        timer.start()
    }
}