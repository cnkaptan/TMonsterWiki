package com.cnkaptan.tmonsterswiki.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity

class MonsterAdapter(private val context: Context) : RecyclerView.Adapter<MonsterAdapter.MonsterViewHolder>() {
    private var monstersGroups: List<Map.Entry<Int, List<MonsterEntity>>> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonsterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.monster_recycler, parent, false)
        return MonsterViewHolder(itemView)
    }

    override fun getItemCount() = monstersGroups.size

    override fun onBindViewHolder(holder: MonsterViewHolder, position: Int) {

        val entry = monstersGroups[position]
        val childMonsterAdapter = ChildMonsterAdapter(context, entry.key, entry.value)
        holder.tvTitle.text = "Rarity ${entry.key}"
        holder.rvChild.adapter = childMonsterAdapter
        holder.rvChild.apply {
            layoutManager = GridLayoutManager(context, 4)
            setHasFixedSize(true)
        }

        childMonsterAdapter.notifyDataSetChanged()

    }

    fun updateList(newList: List<Map.Entry<Int, List<MonsterEntity>>>) {
        monstersGroups = newList
        notifyDataSetChanged()
    }

    class MonsterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val rvChild: RecyclerView = itemView.findViewById(R.id.rvChild)
    }
}