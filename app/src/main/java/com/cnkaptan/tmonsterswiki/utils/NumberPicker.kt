package com.cnkaptan.tmonsterswiki.utils

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.aigestudio.wheelpicker.WheelPicker
import com.cnkaptan.tmonsterswiki.R
import com.google.android.material.button.MaterialButton

class NumberPicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var selectListener: ((Int) -> Unit)? = null
    private val wheelPicker: WheelPicker
    private val doneButton: MaterialButton

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_number_picker,this,false)
        wheelPicker = view.findViewById(R.id.numberPicker)
        doneButton = view.findViewById(R.id.done_btn)

        wheelPicker.apply {
            isCurved = true
            setupDefaultFlings()
            setAtmospheric(true)
            selectedItemTextColor = Color.BLACK
        }

        doneButton.setOnClickListener {
            if (selectListener != null){
                val itemPosition = wheelPicker.currentItemPosition
                selectListener?.invoke(itemPosition)
            }
        }

        addView(view)
    }


    fun setData(data: List<*>){
        data.map { it.toString() }
        wheelPicker.data = data
    }


    fun setItemSelectedListener(selectListener: (Int)-> Unit){
        this.selectListener = selectListener
    }


    fun setSelectedItemPosition(position: Int){
        Handler().postDelayed({
            wheelPicker.selectedItemPosition = position
        },200)
    }
}

fun WheelPicker.setupDefaultFlings() {
    minFlingY *= 50
    minimumVelocity *= 7
    maximumVelocity = minimumVelocity * 3
}