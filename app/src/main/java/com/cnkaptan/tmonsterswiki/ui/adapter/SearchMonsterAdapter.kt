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
import com.cnkaptan.tmonsterswiki.di.module.ApiModule
import com.squareup.picasso.Picasso

class SearchMonsterAdapter(
    private val context: Context,
    private val searchedMonsters: MutableList<MonsterEntity> = mutableListOf(),
    private val listener: (Int, View) -> Unit
) : RecyclerView.Adapter<SearchMonsterAdapter.SearchMonsterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMonsterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_monster, parent, false)
        return SearchMonsterViewHolder(itemView)
    }

    override fun getItemCount() = searchedMonsters.size

    override fun onBindViewHolder(holder: SearchMonsterViewHolder, position: Int) {
        val childMonster = searchedMonsters[position]
        holder.tvMonsterName.text = childMonster.name
        val frameColor = when (childMonster.rarity) {
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

        val resourceName = searchedMonsters[position].getMonsterDrawCode()
        val drawableId = "${ApiModule.BASE_IMAGE_URL}/$resourceName.png"
        Picasso.get()
            .load(drawableId)
            .placeholder(R.drawable.splash_logo)
            .into(holder.ivMonster)

        holder.itemView.setOnClickListener {
            listener(childMonster.id,holder.itemView)
        }

    }

    fun updateList(newFilteredList: List<MonsterEntity>){
        searchedMonsters.clear()
        searchedMonsters.addAll(newFilteredList)
        notifyDataSetChanged()
    }

    class SearchMonsterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMonsterName: TextView = itemView.findViewById(R.id.tvMonsterName)
        val ivMonster: ImageView = itemView.findViewById(R.id.ivMonster)
    }
}