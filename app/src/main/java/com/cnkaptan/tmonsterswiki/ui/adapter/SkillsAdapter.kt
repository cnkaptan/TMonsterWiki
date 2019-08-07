package com.cnkaptan.tmonsterswiki.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.local.entity.SkillEntity
import com.squareup.picasso.Picasso

class SkillsAdapter(
    private val context: Context,
    private val skillList: List<SkillEntity>,
    private val clickListener: (SkillEntity) -> Unit
) : RecyclerView.Adapter<SkillsAdapter.SkillViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_skills_content, parent, false)
        return SkillViewHolder(itemView)
    }

    override fun getItemCount() = skillList.size

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        val skillEntity = skillList[holder.adapterPosition]
//        Log.e("SkillsAdapter", skillEntity.getDrawResName())
        val monsterImageUrl = "http://78.24.221.246:81/build/images/${skillEntity.getDrawResName()}.png"
            Picasso.get()
                .load(monsterImageUrl)
                .placeholder(R.drawable.splash_logo)
                .into(holder.ivSkill)

        holder.itemView.setOnClickListener {
            clickListener(skillEntity)
        }
    }

    class SkillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivSkill = itemView.findViewById(R.id.ivSkill) as ImageView
    }
}