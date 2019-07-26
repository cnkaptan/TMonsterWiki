package com.cnkaptan.tmonsterswiki.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterLevelEntity

class MonsterLevelAdapter(private val context: Context, private var monsterLevels:
    List<MonsterLevelEntity> = mutableListOf()) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_ITEM) {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layout_levels_content, parent, false)
            return MonsterLevelItemViewHolder(view)
        } else if (viewType == TYPE_HEADER) {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_layout_levels_header, parent, false)
            return MonsterLevelHeaderViewHolder(view)
        }

        throw RuntimeException("there is no type that matches the type $viewType + make sure your using types correctly")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MonsterLevelItemViewHolder) {
            val monsterLevel = getItem(position)
            holder.tvLevel.text=monsterLevel.level.toString()
            holder.tvHealth.text = monsterLevel.hp.toString()
            holder.tvDamage.text = monsterLevel.dmg.toString()
            holder.tvPhyDefence.text = monsterLevel.phyDef.toString()
            holder.tvMagDefence.text = monsterLevel.magDef.toString()
            holder.tvSpeed.text = monsterLevel.speed.toString()
            holder.tvMove.text = monsterLevel.move.toString()
        }
    }

    private fun getItem(position: Int): MonsterLevelEntity {
        return monsterLevels[position - 1]
    }

    override fun getItemCount(): Int {
        return monsterLevels.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionHeader(position)) TYPE_HEADER else TYPE_ITEM

    }

    private fun isPositionHeader(position: Int): Boolean {
        return position == 0
    }

    fun updateLevels(newList: List<MonsterLevelEntity>) {
        monsterLevels = newList
        notifyDataSetChanged()
    }

    class MonsterLevelItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvLevel: TextView = itemView.findViewById(R.id.tvLevel)
        val tvHealth: TextView = itemView.findViewById(R.id.tvHealth)
        val tvDamage: TextView = itemView.findViewById(R.id.tvDamage)
        val tvMagDefence: TextView = itemView.findViewById(R.id.tvMagDefence)
        val tvPhyDefence: TextView = itemView.findViewById(R.id.tvPhyDefence)
        val tvSpeed: TextView = itemView.findViewById(R.id.tvSpeed)
        val tvMove: TextView = itemView.findViewById(R.id.tvMove)
    }


    class MonsterLevelHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }
}