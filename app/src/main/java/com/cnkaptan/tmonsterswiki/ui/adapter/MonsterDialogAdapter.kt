package com.cnkaptan.tmonsterswiki.ui.adapter

import android.content.Context
import android.util.Log
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

class MonsterDialogAdapter(
    private val monsters: List<MonsterEntity>,
    private val context: Context,
    private val listener:(MonsterEntity)->Unit
) : RecyclerView.Adapter<MonsterDialogAdapter.MonsterDialogViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonsterDialogViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout_monster, parent, false)
        return MonsterDialogViewHolder(itemView)
    }

    override fun getItemCount() = monsters.size


    override fun onBindViewHolder(holder: MonsterDialogViewHolder, position: Int) {
        val monster=monsters[position]
        holder.tvMonsterName.text=monster.name

        val resourceName = monster.getMonsterDrawCode()
        val drawableId = "${ApiModule.BASE_IMAGE_URL}/$resourceName.png"

        Picasso.get()
            .load(drawableId)
            .placeholder(R.drawable.splash_logo)
            .into(holder.ivMonster)
        holder.itemView.setOnClickListener {
            Log.e("CheckMonsterr",monster.name)
            listener(monster)
        }
        //holder.tvMonsterName.setTextColor()

    }

    class MonsterDialogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMonsterName: TextView = itemView.findViewById(R.id.tvMonsterName)
        val ivMonster: ImageView = itemView.findViewById(R.id.ivMonster)
    }
}