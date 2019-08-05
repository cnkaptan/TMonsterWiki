package com.cnkaptan.tmonsterswiki.ui.custom

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.cnkaptan.tmonsterswiki.R

class LoadingView: DialogFragment() {

    companion object{
        fun newInstance(): LoadingView {
            val dialog = LoadingView()
            val args = Bundle()
            dialog.arguments = args
            return dialog
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (e: IllegalStateException) {
            Log.e("TaxofonLoadingView", "Dialog illegal State Exception")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val args = arguments
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_dialog_loading, container, false)
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return view
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        System.gc()
    }
}