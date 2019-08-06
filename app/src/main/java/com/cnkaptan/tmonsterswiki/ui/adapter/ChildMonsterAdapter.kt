package com.cnkaptan.tmonsterswiki.ui.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.ui.MonsterDetailActivity
import com.squareup.picasso.Picasso

class ChildMonsterAdapter(
    private val context: Context,
    private val rarity: Int,
    private val childMonsters: List<MonsterEntity>
) : RecyclerView.Adapter<ChildMonsterAdapter.MonsterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonsterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_monster, parent, false)
        return MonsterViewHolder(itemView)
    }

    override fun getItemCount() = childMonsters.size

    override fun onBindViewHolder(holder: MonsterViewHolder, position: Int) {
        val childMonster = childMonsters[position]
        holder.tvMonsterName.text = childMonsters[position].name
        val frameColor = when (rarity) {
            1 -> {
                holder.tvMonsterName.setTextColor(context.resources.getColor(R.color.colorCommonFrame))
                R.drawable.common_frame
            }
            2 ->{
                holder.tvMonsterName.setTextColor(context.resources.getColor(R.color.colorEpicFrame))
                R.drawable.epic_frame
            }
            3 ->{
                holder.tvMonsterName.setTextColor(context.resources.getColor(R.color.colorMonstrousFrame))
                R.drawable.monstrous_frame
            }
            else ->{
                holder.tvMonsterName.setTextColor(context.resources.getColor(R.color.colorLegendaryFrame))
                R.drawable.legendary_frame
            }
        }

        holder.ivMonster.setBackgroundResource(frameColor)

        val resourceName = childMonsters[position].getMonsterDrawCode()
        val drawableId = "http://78.24.221.246:81/build/images/$resourceName.png"
        Log.e("ChildMonster",drawableId)
        Picasso.get()
            .load(drawableId)
            .placeholder(R.drawable.tm_splash_image)
            .into(holder.ivMonster)

        holder.itemView.setOnClickListener {
            val detailIntent = MonsterDetailActivity.newIntent(context, childMonster.id)
            detailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(detailIntent)
        }

    }

    class MonsterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMonsterName: TextView = itemView.findViewById(R.id.tvMonsterName)
        val ivMonster: ImageView = itemView.findViewById(R.id.ivMonster)
    }
}