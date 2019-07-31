package com.cnkaptan.tmonsterswiki.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.squareup.picasso.Picasso

class MonsterTDexAdapter(
    private val context: Context
) : RecyclerView.Adapter<MonsterTDexAdapter.MonsterViewHolder>() {

    private var level: Int? = 0
    private var monsterWithLevels: MutableList<MonsterEntity> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonsterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_monster_dex, parent, false)
        return MonsterViewHolder(itemView)
    }

    override fun getItemCount() = monsterWithLevels.size


    override fun onBindViewHolder(holder: MonsterViewHolder, position: Int) {
        val monsterEntity = monsterWithLevels[position]
        holder.tvMonsterName.text = monsterEntity.name
        holder.tvMonsterHealth.text = monsterEntity.levels.get(level!!).hp.toString()
        holder.tvMonsterDamage.text = monsterEntity.levels.get(level!!).dmg.toString()
        holder.tvMonsterMove.text = monsterEntity.levels.get(level!!).move.toString()

        val frameColor = when (monsterEntity.rarity) {
            1 -> R.drawable.common_frame
            2 -> R.drawable.epic_frame
            3 -> R.drawable.monstrous_frame
            else -> R.drawable.legendary_frame
        }

        holder.ivMonster.setBackgroundResource(frameColor)

        val resourceName = monsterEntity.resourceCode.toLowerCase()
        val drawableId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
        if (drawableId > 0) {
            Picasso.get()
                .load(drawableId)
                .placeholder(R.drawable.tm_splash_image)
                .into(holder.ivMonster)
        } else {
            holder.ivMonster.setImageResource(R.drawable.tm_splash_image)
        }

    }

    fun updateLevel(getLevel: Int) {
        level = getLevel
        notifyDataSetChanged()
    }

    fun updateMonster(monsterList: List<MonsterEntity>) {
        monsterWithLevels.clear()
        monsterWithLevels = monsterList.toMutableList()
        notifyDataSetChanged()
    }

    class MonsterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMonsterName: TextView = itemView.findViewById(R.id.tvMonsterName)
        val tvMonsterHealth: TextView = itemView.findViewById(R.id.tvMonsterHealth)
        val tvMonsterDamage: TextView = itemView.findViewById(R.id.tvMonsterDamage)
        val tvMonsterMove: TextView = itemView.findViewById(R.id.tvMonsterMove)
        val ivMonster: ImageView = itemView.findViewById(R.id.ivMonsterDex)
    }
}