package com.jmsoftwares.mesanews.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import androidx.lifecycle.ViewModel

class Navigation {

   private fun getClearBackFlags(): Int = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

    fun goToActivityCleaningBack(
        activity: Activity,
        newActivityClass: Class<*>) {
        val intent = Intent(activity, newActivityClass)
        intent.addFlags(getClearBackFlags())
        activity.startActivity(intent)
        activity.finish()
    }


    fun goToActivity(context: Context, newActivityClass: Class<*>) {
        val intent = Intent(context, newActivityClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun goToActivityCleaningBackParams(
        activity: Activity,
        newActivityClass: Class<*>,
        params: Bundle? = null
    ) {
        val intent = Intent(activity, newActivityClass)
        intent.addFlags(getClearBackFlags())
        params?.let {
            intent.putExtras(params)
        }

        activity.startActivity(intent)
        activity.finish()
    }


    fun goToActivityParams(context: Context, newActivityClass: Class<*>, params: Bundle? = null) {
        val intent = Intent(context, newActivityClass)
        params?.let {
            intent.putExtras(params)
        }
        context.startActivity(intent)
    }
}