package com.cnkaptan.tmonsterswiki.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment: Fragment() {
    abstract val TAG: String
    lateinit var disposableContainer: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disposableContainer = CompositeDisposable()
    }

    override fun onStop() {
        super.onStop()
        disposableContainer.clear()
    }
}