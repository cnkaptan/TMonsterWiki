package com.cnkaptan.tmonsterswiki.ui

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cnkaptan.tmonsterswiki.AppController
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.data.repository.MonsterRepository
import com.cnkaptan.tmonsterswiki.remote.api.MonstersApi
import com.cnkaptan.tmonsterswiki.ui.adapter.MonsterAdapter
import com.cnkaptan.tmonsterswiki.ui.base.BaseActivity
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : BaseActivity() {
    override val TAG: String
        get() = MainActivity::class.java.simpleName

    @Inject
    lateinit var monstersApi: MonstersApi

    @Inject
    lateinit var monsterRepository: MonsterRepository

    private lateinit var rvMonsterList: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as AppController).appComponent.inject(this)

        rvMonsterList = findViewById(R.id.rvMonsterList)
        val lm = LinearLayoutManager(applicationContext)

        val monsterAdapter = MonsterAdapter(applicationContext)
        rvMonsterList.apply {
            setHasFixedSize(true)
            layoutManager = lm
            adapter = monsterAdapter
        }
        rvMonsterList.setHasFixedSize(true)
        rvMonsterList.layoutManager = lm
        rvMonsterList.adapter = monsterAdapter
        disposibleContainer.add(
            monsterRepository.getAllMonsters()
                .map { monstersList -> monstersList.sortedBy { it.name } }
                .flatMapPublisher { Flowable.fromIterable(it) }
                .doOnNext { Log.e(TAG, "${it.name} --> ${it.id} --> ${it.resourceCode}") }
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    monsterAdapter.updateList(it.groupBy(MonsterEntity::rarity).entries.toList())
                }, { error -> Log.e(TAG, error.message, error) })
        )
    }
}