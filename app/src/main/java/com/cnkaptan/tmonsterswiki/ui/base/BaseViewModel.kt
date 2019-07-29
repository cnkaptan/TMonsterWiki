package com.cnkaptan.tmonsterswiki.ui.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel: ViewModel() {
    internal val disposibleContainer = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposibleContainer.clear()
    }
}