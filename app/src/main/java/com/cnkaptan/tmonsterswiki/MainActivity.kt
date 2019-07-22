package com.cnkaptan.tmonsterswiki

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cnkaptan.tmonsterswiki.di.component.AppComponent
import com.cnkaptan.tmonsterswiki.remote.api.MonstersApi
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var monstersApi: MonstersApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as AppController).appComponent.inject(this)
        monstersApi.fetchMonsterLevelsById(101)
            .flatMapPublisher{Flowable.fromIterable(it)}
            .doOnNext { Log.e("MainActivity", it.toString()) }
            .toList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({response->
            }, {error-> Log.e("MainAcitivity",error.message,error)})
    }
}
