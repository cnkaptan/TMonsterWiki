package com.cnkaptan.tmonsterswiki.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity

class ChildMonsterAdapter(private val childMonsters: List<MonsterEntity>) :
    RecyclerView.Adapter<ChildMonsterAdapter.MonsterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonsterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_monster, parent, false)
        return MonsterViewHolder(itemView)
    }

    override fun getItemCount() = childMonsters.size

    override fun onBindViewHolder(holder: MonsterViewHolder, position: Int) {
        holder.tvMonsterName.text = childMonsters[position].name
    }

    class MonsterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMonsterName: TextView = itemView.findViewById(R.id.tvMonsterName)
    }
}