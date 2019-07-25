package com.cnkaptan.tmonsterswiki.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterLevelEntity

class MonsterLevelAdapter(private val context: Context) :
    RecyclerView.Adapter<MonsterLevelAdapter.MonsterLevelViewHolder>() {

    private var monsterLevels: List<MonsterLevelEntity> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonsterLevelViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_levels, parent, false)
        return MonsterLevelViewHolder(itemView)
    }

    override fun getItemCount() = monsterLevels.size

    override fun onBindViewHolder(holder: MonsterLevelViewHolder, position: Int) {
        val monsterLevel = monsterLevels[position]
        holder.tvLevel.text=monsterLevel.level.toString()
        holder.tvHealth.text = monsterLevel.hp.toString()
        holder.tvDamage.text = monsterLevel.dmg.toString()
        holder.tvPhyDefence.text = monsterLevel.phyDef.toString()
        holder.tvMagDefence.text = monsterLevel.magDef.toString()
        holder.tvSpeed.text = monsterLevel.speed.toString()
        holder.tvMove.text = monsterLevel.move.toString()
    }

    fun updateLevels(newList: List<MonsterLevelEntity>) {
        monsterLevels = newList
        notifyDataSetChanged()
    }

    class MonsterLevelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvLevel: TextView = itemView.findViewById(R.id.tvLevel)
        val tvHealth: TextView = itemView.findViewById(R.id.tvHealth)
        val tvDamage: TextView = itemView.findViewById(R.id.tvDamage)
        val tvMagDefence: TextView = itemView.findViewById(R.id.tvMagDefence)
        val tvPhyDefence: TextView = itemView.findViewById(R.id.tvPhyDefence)
        val tvSpeed: TextView = itemView.findViewById(R.id.tvSpeed)
        val tvMove: TextView = itemView.findViewById(R.id.tvMove)
    }
}