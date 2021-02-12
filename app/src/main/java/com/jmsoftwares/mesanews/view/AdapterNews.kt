package com.jmsoftwares.mesanews.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jmsoftwares.mesanews.R
import com.jmsoftwares.mesanews.controller.SharedPreferences
import com.jmsoftwares.mesanews.model.News
import com.jmsoftwares.mesanews.util.Navigation
import com.squareup.picasso.Picasso



class AdapterNews (private val newsList: List<News>,
                   private val activity: Activity,
                   val sharedPreferences: SharedPreferences
) : RecyclerView.Adapter<AdapterNews.ViewHolder>() {

    override fun getItemCount(): Int {
        return newsList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var image: ImageView = itemView.findViewById(R.id.recycleView_imageView)
        var image_fav: ImageView = itemView.findViewById(R.id.recycleView_image_fav)
        var title: TextView = itemView.findViewById(R.id.recycleView_title)
        var description: TextView = itemView.findViewById(R.id.recycleView_description)
        var layout: RelativeLayout = itemView.findViewById(R.id.recycleView_layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val news = newsList[position]

        holder.description.text = news.description
        holder.title.text = news.title

        if (sharedPreferences.getFavorite(activity, news.url) == true) {
            holder.image_fav.setImageResource(R.drawable.img_heartfull)
        } else {
            holder.image_fav.setImageResource(R.drawable.img_heart)
        }

        Picasso.get().load(news.image_url).placeholder(R.drawable.img_placeholder).error(R.drawable.img_placeholder).into(holder.image)

        holder.layout.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("url", news.url)
            bundle.putString("title", news.title)
            val navigation = Navigation()
            navigation.goToActivityParams(activity, WebViewActivity::class.java, bundle)
        }

        holder.image_fav.setOnClickListener {

            if (sharedPreferences.getFavorite(activity, news.url)!!) {
                holder.image_fav.setImageResource(R.drawable.img_heart)
                sharedPreferences.setFavorite(activity, news.url, false)
            } else {
                holder.image_fav.setImageResource(R.drawable.img_heartfull)
                sharedPreferences.setFavorite(activity, news.url, true)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.recycleview_layout, parent, false)
        return ViewHolder(view)
    }
}
