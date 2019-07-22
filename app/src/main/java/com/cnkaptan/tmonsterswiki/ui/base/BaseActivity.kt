package com.cnkaptan.tmonsterswiki.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.disposables.DisposableContainer

abstract class BaseActivity : AppCompatActivity() {
    abstract val TAG: String
    lateinit var disposibleContainer: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disposibleContainer = CompositeDisposable()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposibleContainer.clear()
    }
}