package com.cnkaptan.tmonsterswiki

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.util.Log
import com.cnkaptan.tmonsterswiki.di.component.AppComponent
import com.cnkaptan.tmonsterswiki.di.component.DaggerAppComponent
import com.squareup.picasso.LruCache
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso

class AppController : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()


        val requestTransformer = Picasso.RequestTransformer { request ->
            Log.d("image request", request.toString())
            request
        }

        val builder = Picasso.Builder(this)
            .downloader(OkHttp3Downloader(this, Long.MAX_VALUE))
            .indicatorsEnabled(true)
            .loggingEnabled(true)
            .memoryCache(LruCache(getBytesForMemCache(12)))
            .requestTransformer(requestTransformer)

        Picasso.setSingletonInstance(builder.build())
    }

    //returns the given percentage of available memory in bytes
    private fun getBytesForMemCache(percent: Int): Int {
        val mi = ActivityManager.MemoryInfo()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(mi)
        val availableMemory = mi.availMem.toDouble()
        return (percent * availableMemory / 100).toInt()
    }

}