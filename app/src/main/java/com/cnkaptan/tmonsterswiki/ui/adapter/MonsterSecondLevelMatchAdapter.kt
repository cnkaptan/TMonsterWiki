package com.cnkaptan.tmonsterswiki.ui.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterLevelEntity

class MonsterSecondLevelMatchAdapter(
    private val context: Context
) : RecyclerView.Adapter<MonsterSecondLevelMatchAdapter.MonsterSecondLevelMatchViewHolder>() {

    private var monsterLevels: MutableList<MonsterLevelEntity> = mutableListOf()
    private var level: Int? = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MonsterSecondLevelMatchViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout_level_match, parent, false)
        return MonsterSecondLevelMatchViewHolder(itemView)
    }

    override fun getItemCount() = 1

    override fun onBindViewHolder(holder: MonsterSecondLevelMatchViewHolder, position: Int) {

        val monsterLevelEntity = monsterLevels[level!!]
        holder.tvHealth.text = monsterLevelEntity.hp.toString()
        holder.tvDamage.text = monsterLevelEntity.dmg.toString()
        holder.tvSpeed.text = monsterLevelEntity.speed.toString()
        holder.tvMove.text = monsterLevelEntity.move.toString()
        holder.tvHit.text = monsterLevelEntity.hit.toString()
        holder.tvCrit.text = monsterLevelEntity.critical.toString()
        holder.tvPhyDefence.text = monsterLevelEntity.phyDef.toString()
        holder.tvMagDefence.text = monsterLevelEntity.magDef.toString()

    }

    fun updateMonsterLevel(monsterList: List<MonsterLevelEntity>) {
        monsterLevels.clear()
        monsterLevels = monsterList.toMutableList()
        notifyDataSetChanged()
    }

    fun updateLevel(getLevel: Int) {
        level = getLevel
        notifyDataSetChanged()
    }

    class MonsterSecondLevelMatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHealth: TextView = itemView.findViewById(R.id.tvMonsterHealthFirst)
        val tvDamage: TextView = itemView.findViewById(R.id.tvMonsterDamageFirst)
        val tvMagDefence: TextView = itemView.findViewById(R.id.tvMonsterMagDefFirst)
        val tvPhyDefence: TextView = itemView.findViewById(R.id.tvMonsterPhyDefFirst)
        val tvSpeed: TextView = itemView.findViewById(R.id.tvMonsterSpeedFirst)
        val tvMove: TextView = itemView.findViewById(R.id.tvMonsterMoveFirst)
        val tvHit: TextView = itemView.findViewById(R.id.tvMonsterHitFirst)
        val tvCrit: TextView = itemView.findViewById(R.id.tvMonsterCritFirst)
    }
}