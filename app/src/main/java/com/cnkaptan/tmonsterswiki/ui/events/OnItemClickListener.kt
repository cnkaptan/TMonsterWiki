package com.cnkaptan.tmonsterswiki.ui.events

interface OnItemClickListener<T> {
    fun onItemClick(item:T)

    companion object{
        fun <T> create(listener:(T)->Unit): OnItemClickListener<T> = object :
            OnItemClickListener<T> {
            override fun onItemClick(item: T) = listener(item)
        }
    }
}