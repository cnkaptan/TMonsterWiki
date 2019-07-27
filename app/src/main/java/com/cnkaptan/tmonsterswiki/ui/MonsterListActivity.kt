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

class MonsterListActivity : BaseActivity() {
    override val TAG: String
        get() = MonsterListActivity::class.java.simpleName

    @Inject
    lateinit var monstersApi: MonstersApi

    @Inject
    lateinit var monsterRepository: MonsterRepository

    private lateinit var rvMonsterList: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monster_list)
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
                .flatMapPublisher { Flowable.fromIterable(it) }
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val sortedBy = it.groupBy(MonsterEntity::rarity).entries.toList()
                        .sortedBy { rarirtyList -> -rarirtyList.key }
                    monsterAdapter.updateList(sortedBy)
                }, { error -> Log.e(TAG, error.message, error) })
        )
    }
}