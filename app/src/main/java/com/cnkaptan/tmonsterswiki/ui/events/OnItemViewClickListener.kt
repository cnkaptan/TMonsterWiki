package com.cnkaptan.tmonsterswiki.ui.events

import android.view.View

interface OnItemViewClickListener<T> {
    fun onItemClick(view: View, item:T)

    companion object{
        fun <T> create(listener:(View,T)->Unit): OnItemViewClickListener<T> = object :
            OnItemViewClickListener<T> {
            override fun onItemClick(view: View,item: T) = listener(view,item)
        }
    }
}