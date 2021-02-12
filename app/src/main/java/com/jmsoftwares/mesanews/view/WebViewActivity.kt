package com.jmsoftwares.mesanews.view

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jmsoftwares.mesanews.R


class WebViewActivity : AppCompatActivity() {

    private var url: String = ""
    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.web_view_activity)

        val actionbar: ActionBar = supportActionBar!!
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setHomeButtonEnabled(true)

        url = intent.getStringExtra("url").toString()
        title = intent.getStringExtra("title").toString()
        val webView: WebView = findViewById(R.id.webview)
        val textView: TextView = findViewById(R.id.webview_textView)


        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.builtInZoomControls = true
        webView.webViewClient = MyWebViewClient()

        webView!!.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                textView!!.text = "Page loading : $newProgress%"
                if (newProgress == 100) {
                    textView!!.text = "Page Loaded."
                    textView.visibility = View.GONE
                }
            }
        }
        webView.loadUrl(url)

        var bottomNavigationView = findViewById<BottomNavigationView>(R.id.webview_bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item: MenuItem ->
            val id = item.itemId
            when (id) {
                R.id.menu_share -> {
                    share()

                }
            }
            true
        })
    }

    private fun share() {
        val shareIntent = Intent(Intent.ACTION_SEND)

        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "MesaNews")

        var sAux = """Notícia encontrada no MesaNews!
             $title
             $url 
             """.trimIndent()
        shareIntent.putExtra(Intent.EXTRA_TEXT, sAux)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(shareIntent, "Compartilhar Notícia com..."))
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    private class MyWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            // This is my web site, so do not override; let my WebView load the page
            return false
        }
    }
}