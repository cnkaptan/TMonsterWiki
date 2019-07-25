package com.cnkaptan.tmonsterswiki.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterLevelEntity

class MonsterLevelAdapter(private val context: Context,
                          private val monsterLevels: MutableList<MonsterLevelEntity> = mutableListOf())
    : RecyclerView.Adapter<MonsterLevelAdapter.MonsterLevelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonsterLevelViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_levels, parent, false)
        return MonsterLevelViewHolder(itemView)
    }

    override fun getItemCount() = monsterLevels.size

    override fun onBindViewHolder(holder: MonsterLevelViewHolder, position: Int) {
        val monsterLevels = monsterLevels[position]
        holder.tvLevel.text=monsterLevels.level.toString()
        holder.tvHp.text=monsterLevels.hp.toString()
    }

    fun updateLevels(newList: List<MonsterLevelEntity>) {
        monsterLevels.clear()
        monsterLevels.addAll(newList)
        notifyDataSetChanged()
    }

    class MonsterLevelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvLevel: TextView = itemView.findViewById(R.id.tvLevel)
        val tvHp: TextView = itemView.findViewById(R.id.tvHp)
    }
}