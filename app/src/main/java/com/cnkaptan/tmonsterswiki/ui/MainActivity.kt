package com.cnkaptan.tmonsterswiki.ui

import android.content.Context
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
import com.cnkaptan.tmonsterswiki.ui.adapter.ChildMonsterAdapter
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

    lateinit var titles:MutableList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as AppController).appComponent.inject(this)

        rvMonsterList = findViewById(R.id.rvMonsterList)
        val lm = LinearLayoutManager(applicationContext)
        titles=mutableListOf("Common Monsters", "Legendary Monsters", "Epic Monsters", "defence Monsters")

        val monsterAdapter = MonsterAdapter(titles,applicationContext)
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
                    //monsterAdapter.updateList(it)
                    it.groupBy { it.rarity }
                        .entries.forEach {
                       it.value.forEach { monster ->
                           Log.e(TAG, "${it.key} --> ${monster.name}")
                           monsterAdapter.updateList(it)
                       }
                    }
                }, { error -> Log.e(TAG, error.message, error) })
        )
    }
}


class MonsterAdapter(private val titles: MutableList<String> = mutableListOf()
,private val context: Context) :
    RecyclerView.Adapter<MonsterAdapter.MonsterViewHolder>() {

    private val commonMonsters: MutableList<MonsterEntity> = mutableListOf()
    private val legendaryMonsters: MutableList<MonsterEntity> = mutableListOf()

    var  commonMonstersMaps:Map.Entry<Int,List<MonsterEntity>>?=null
    var  legendaryMonstersMaps:Map.Entry<Int,List<MonsterEntity>>?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonsterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.monster_recycler, parent, false)
        return MonsterViewHolder(itemView)
    }

    override fun getItemCount() = titles.size

    override fun onBindViewHolder(holder: MonsterViewHolder, position: Int) {

        val lm = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        val childMonsterAdapter=ChildMonsterAdapter(commonMonsters)


        if (titles.get(position).equals("Common Monsters")){
            holder.tvTitle.text ="Common Monsters"
            commonMonstersMaps?.value?.let { commonMonsters.addAll(it) }
            val childMonsterAdapter=ChildMonsterAdapter(commonMonsters)
            holder.rvChild.adapter=childMonsterAdapter
        }else if(titles.get(position).equals("Legendary Monsters")){
           holder.tvTitle.text ="Legendary Monsters"
            legendaryMonstersMaps?.value?.let { legendaryMonsters.addAll(it) }
            val childMonsterAdapter=ChildMonsterAdapter(legendaryMonsters)
            holder.rvChild.adapter=childMonsterAdapter
        }

        holder.rvChild.apply {
            layoutManager=lm
            setHasFixedSize(true)
        }
        childMonsterAdapter.notifyDataSetChanged()

    }

    fun updateList(newList: Map.Entry<Int,List<MonsterEntity>>) {
        if(newList.key==1){
            commonMonstersMaps=newList
        }else if(newList.key==2){
            legendaryMonstersMaps=newList
        }
        Log.e("UPDATELISTCHECK", "${commonMonstersMaps?.key} --> ${commonMonstersMaps?.value}")
        notifyDataSetChanged()
    }

    class MonsterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val rvChild:RecyclerView=itemView.findViewById(R.id.rvChild)
    }
}