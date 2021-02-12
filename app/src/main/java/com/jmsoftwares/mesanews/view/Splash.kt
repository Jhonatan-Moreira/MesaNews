package com.jmsoftwares.mesanews.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.jmsoftwares.mesanews.R
import com.jmsoftwares.mesanews.util.UiUtils
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class Splash : AppCompatActivity(), Runnable {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        val uiUtils = UiUtils()
        uiUtils.hideSystemUI(this)
        printHashKey(this)
        val handler = Handler()
        handler.postDelayed(this, 3000)
    }

    override fun run() {
        val intent = Intent(this, Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        this.startActivity(intent)
        this.finish()
    }

    fun printHashKey(pContext: Context) {
        try {
            val info = pContext.packageManager.getPackageInfo(
                pContext.packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                println("Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
        } catch (e: Exception) {
            println(e)
        }
    }
}
