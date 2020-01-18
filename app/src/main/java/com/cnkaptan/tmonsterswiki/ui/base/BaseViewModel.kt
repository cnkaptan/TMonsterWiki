package com.cnkaptan.tmonsterswiki.ui.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables

open class BaseViewModel: ViewModel() {
    val disposables = CompositeDisposable()
    private val disposableMap = hashMapOf<Any, Disposable>()

    override fun onCleared() {
        super.onCleared()
        disposableMap.forEach { it.value.dispose() }
        disposableMap.clear()
        disposables.clear()
    }

    operator fun get(key: Any): Disposable = disposableMap.getOrElse(key) { Disposables.disposed() }

    operator fun set(key: Any, value: Disposable) = disposableMap.put(key, value)?.dispose()

    operator fun plusAssign(disposable: Disposable) {
        disposables.add(disposable)
    }

}