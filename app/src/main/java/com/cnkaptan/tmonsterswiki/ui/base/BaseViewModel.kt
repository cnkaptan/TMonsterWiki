package com.cnkaptan.tmonsterswiki.ui.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.cnkaptan.tmonsterswiki.AppController
import com.cnkaptan.tmonsterswiki.di.component.AppComponent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables

open class BaseViewModel(app: Application): AndroidViewModel(app), AppComponent by (app as AppController).appComponent {
    val disposables = CompositeDisposable()
    private val disposableMap = hashMapOf<Any, Disposable>()

    val context: Context get() = getApplication<Application>().applicationContext

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