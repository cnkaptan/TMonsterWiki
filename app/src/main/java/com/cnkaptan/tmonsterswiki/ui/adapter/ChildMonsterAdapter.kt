package com.cnkaptan.tmonsterswiki.ui.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.squareup.picasso.Picasso

class ChildMonsterAdapter(private val context: Context,
                          private val rarity: Int,
                          private val childMonsters: List<MonsterEntity>)
    : RecyclerView.Adapter<ChildMonsterAdapter.MonsterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonsterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_monster, parent, false)
        return MonsterViewHolder(itemView)
    }

    override fun getItemCount() = childMonsters.size

    override fun onBindViewHolder(holder: MonsterViewHolder, position: Int) {
        holder.tvMonsterName.text = childMonsters[position].name
        val frameColor = when(rarity){
            1-> R.drawable.common_frame
            2-> R.drawable.epic_frame
            3-> R.drawable.monstrous_frame
            else-> R.drawable.legendary_frame
        }



        holder.ivMonster.setBackgroundResource(frameColor)

        val resourceName = childMonsters[position].resourceCode.toLowerCase()
        val drawableId = context.resources.getIdentifier(resourceName,"drawable",context.packageName)
        if (drawableId > 0){
            Picasso.get()
                .load(drawableId)
                .placeholder(R.drawable.tm_splash_image)
                .into(holder.ivMonster)
        }else{
            holder.ivMonster.setImageResource(R.drawable.tm_splash_image)
        }


    }

    class MonsterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMonsterName: TextView = itemView.findViewById(R.id.tvMonsterName)
        val ivMonster: ImageView = itemView.findViewById(R.id.ivMonster)
    }
}