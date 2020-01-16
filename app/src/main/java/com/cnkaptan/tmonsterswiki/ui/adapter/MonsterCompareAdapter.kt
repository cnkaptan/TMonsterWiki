package com.cnkaptan.tmonsterswiki.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.databinding.ItemLayoutMonsterCompareBinding

class MonsterCompareAdapter : ListAdapter<MonsterEntity,MonsterCompareAdapter.MonsterViewHolder>(MonsterDiff()) {

    var level: Int = 23

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonsterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MonsterViewHolder(ItemLayoutMonsterCompareBinding.inflate(inflater))
    }

    override fun onBindViewHolder(holder: MonsterViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }


    inner class MonsterViewHolder(private val binding: ItemLayoutMonsterCompareBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(monsterEntity: MonsterEntity){
            binding.item = monsterEntity

            val monsterLevelEntity = monsterEntity.levels[level-1]

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
}

class MonsterDiff: DiffUtil.ItemCallback<MonsterEntity>(){
    override fun areItemsTheSame(oldItem: MonsterEntity, newItem: MonsterEntity): Boolean {
        return oldItem.monsterId == newItem.monsterId
    }

    override fun areContentsTheSame(oldItem: MonsterEntity, newItem: MonsterEntity): Boolean {
        return oldItem == newItem
    }
}