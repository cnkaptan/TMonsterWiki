package com.cnkaptan.tmonsterswiki.utils

import android.graphics.Typeface
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.di.module.ApiModule
import com.squareup.picasso.Picasso

@BindingAdapter("resourceName")
fun ImageView.createImageUrlandShow(resourceName: String){
    val fullUrl = "${ApiModule.BASE_IMAGE_URL}/$resourceName.png"

    Picasso.get()
        .load(fullUrl)
        .placeholder(R.drawable.splash_logo)
        .into(this)
}

@BindingAdapter("monsterFrame")
fun ImageView.putMonsterFrame(rarity: Int){
    val frameColor = when (rarity) {
        1 -> R.drawable.common_frame
        2 -> R.drawable.epic_frame
        3 -> R.drawable.monstrous_frame
        else -> R.drawable.legendary_frame
    }

    setBackgroundResource(frameColor)
}

@BindingAdapter("textFont")
fun TextView.setFont(font: String){
    val typeFace = Typeface.createFromAsset(context.assets, font)
    typeface = typeFace
}