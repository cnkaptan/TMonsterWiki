package com.cnkaptan.tmonsterswiki.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.databinding.ItemLayoutMonsterCompareBinding
import com.cnkaptan.tmonsterswiki.ui.events.OnItemClickListener

class MonsterCompareAdapter(val listener: (MonsterEntity) -> Unit) :
    RecyclerView.Adapter<MonsterCompareAdapter.MonsterViewHolder>() {

    var level: Int = 23

    var mMonsterList : List<MonsterEntity> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonsterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MonsterViewHolder(ItemLayoutMonsterCompareBinding.inflate(inflater))
    }

    override fun onBindViewHolder(holder: MonsterViewHolder, position: Int) {
        val monsterEntity = mMonsterList[position]
        holder.bindData(listener, monsterEntity)
    }

    override fun getItemCount(): Int {
       return mMonsterList.size
    }

    inner class MonsterViewHolder(private val binding: ItemLayoutMonsterCompareBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(monsterClickListener: (MonsterEntity) -> Unit, monsterEntity: MonsterEntity) {
            binding.item = monsterEntity
            binding.clickListener = OnItemClickListener.create(monsterClickListener)

            val monsterLevelEntity = monsterEntity.levels[level - 1]

            binding.tvMonsterHealth.text = monsterLevelEntity.hp.toString()
            binding.tvMonsterDamage.text = monsterLevelEntity.dmg.toString()
            binding.tvMonsterCrit.text = monsterLevelEntity.critical.toString()
            binding.tvMonsterHit.text = monsterLevelEntity.hit.toString()
            binding.tvMonsterMagDef.text = monsterLevelEntity.magDef.toString()
            binding.tvMonsterPhyDef.text = monsterLevelEntity.phyDef.toString()
            binding.tvMonsterMove.text = monsterLevelEntity.move.toString()
            binding.tvMonsterSpeed.text = monsterLevelEntity.speed.toString()
        }
    }

    fun updateLevel(getLevel: Int) {
        level = getLevel
        notifyDataSetChanged()
    }

    fun updateList(monsterList: List<MonsterEntity>){
        mMonsterList=monsterList
        notifyDataSetChanged()
    }

}
