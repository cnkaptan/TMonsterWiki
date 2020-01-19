package com.cnkaptan.tmonsterswiki.utils

import android.content.Context

open class Event<out T>(private val content: T){

    var isHandled: Boolean = false
        private set


    fun getContentIfNotHandled(): T?{
        return if (isHandled){
            null
        }else{
            isHandled = true
            content
        }
    }

    fun peekContent() = content

}