package com.jmsoftwares.mesanews.model

data class News(

    val title: String,
    val description: String,
    val content: String,
    val author: String,
    val published_at: String,
    val highlight: Boolean,
    val url: String,
    val image_url: String
)