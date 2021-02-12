package com.jmsoftwares.mesanews.util

class Constants {

     object requestConstants{
        const val baseUrl = "https://mesa-news-api.herokuapp.com/"
        const val signinUrl = "${baseUrl}v1/client/auth/signin"
        const val signupUrl = "${baseUrl}v1/client/auth/signup"
        const val newsUrl = "${baseUrl}v1/client/news?current_page=1&per_page=100&published_at="
        const val highlightsUrl = "${baseUrl}v1/client/news/highlights"
    }
}