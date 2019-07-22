package com.cnkaptan.tmonsterswiki.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cnkaptan.tmonsterswiki.AppController
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.data.repository.MonsterRepository
import com.cnkaptan.tmonsterswiki.remote.api.MonstersApi
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

    lateinit var rvMonsterList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as AppController).appComponent.inject(this)

        rvMonsterList = findViewById(R.id.rvMonsterList)
        val lm = LinearLayoutManager(applicationContext)
        val monsterAdapter = MonsterAdapter()
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
                .doOnNext { Log.e(TAG, it.toString()) }
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    monsterAdapter.updateList(it)
                    it.groupBy { it.rarity }
                        .entries.forEach {
                        it.value.forEach { monster ->
                            Log.e(TAG, "${it.key} --> ${monster.name}")
                        }

                    }
                }, { error -> Log.e(TAG, error.message, error) })
        )
    }
}


class MonsterAdapter(private val monsters: MutableList<MonsterEntity> = mutableListOf()) :
    RecyclerView.Adapter<MonsterAdapter.MonsterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonsterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_monster, parent, false)
        return MonsterViewHolder(itemView)
    }

    override fun getItemCount() = monsters.size

    override fun onBindViewHolder(holder: MonsterViewHolder, position: Int) {
        holder.tvMonsterName.text = monsters[position].name
    }

    fun updateList(newList: List<MonsterEntity>) {
        monsters.clear()
        monsters.addAll(newList)
        notifyDataSetChanged()
    }

    class MonsterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMonsterName: TextView = itemView.findViewById(R.id.tvMonsterName)
    }
}