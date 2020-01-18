package com.cnkaptan.tmonsterswiki.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.core.widget.CompoundButtonCompat
import com.cnkaptan.tmonsterswiki.R

class ImageRadioButton(context: Context, attrs: AttributeSet) :
    AppCompatRadioButton(context, attrs) {
    private var selectableBg: Drawable?
    private var iconSource: Drawable?
    private var icWidth: Float
    private var icHeight: Float
    init {

        val ta = context.obtainStyledAttributes(attrs, R.styleable.ImageRadioButton)
        selectableBg = ta.getDrawable(R.styleable.ImageRadioButton_selectableBackground)
        iconSource = ta.getDrawable(R.styleable.ImageRadioButton_iconSource)
        icWidth = ta.getDimension(R.styleable.ImageRadioButton_iconWidth, -1f)
        icHeight = ta.getDimension(R.styleable.ImageRadioButton_iconHeight, -1f)
        ta.recycle()

        if (selectableBg != null && selectableBg is StateListDrawable) {
            setBackgroundDrawable(selectableBg)
        }

        buttonDrawable = null
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        icWidth = icWidth - paddingLeft - paddingRight
        icHeight = icHeight - paddingTop - paddingBottom
        Log.e("ImageRadio","SizeChanged $w, $h")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val itemWidth = measuredWidth
        val itemHeight = measuredWidth
        Log.e("ImageRadio","Canvas $itemWidth, $itemHeight")
        Log.e("ImageRadio","Icon $icWidth, $icHeight")
        if (icWidth > 0 && icHeight > 0) {
            val left = if (itemWidth > icWidth){
                paddingLeft + (itemWidth - icWidth.toInt())/2
            }else{
                paddingLeft
            }

            val top = if (itemHeight > icHeight){
                paddingTop + (itemHeight - icHeight.toInt())/2
            }else{
                paddingTop
            }

            iconSource?.setBounds(left,top,left + icWidth.toInt(),top +icHeight.toInt())
            iconSource?.draw(canvas!!)
        }
    }
}