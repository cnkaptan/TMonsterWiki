package com.cnkaptan.tmonsterswiki.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.local.entity.SkillEntity
import com.cnkaptan.tmonsterswiki.di.module.ApiModule
import com.cnkaptan.tmonsterswiki.ui.ADD
import com.cnkaptan.tmonsterswiki.ui.DONE
import com.cnkaptan.tmonsterswiki.ui.SkillChanges
import com.squareup.picasso.Picasso

class SkillEvoulationAdapter(
    private val skillsList: MutableList<SkillEntity> = mutableListOf(),
    private val clickListener: (SkillEntity) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ROOT) {
            val itemview =
                LayoutInflater.from(parent.context).inflate(R.layout.item_layout_root_skill_tree, parent, false)
            RootViewHolder(itemview)
        } else {
            val itemview =
                LayoutInflater.from(parent.context).inflate(R.layout.item_layout_node_skill_tree, parent, false)
            NodeViewHolder(itemview)
        }
    }

    override fun getItemCount(): Int = skillsList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == ROOT) {
            val rootHolder = holder as RootViewHolder
            val skillEntity = skillsList[position]
            val monsterImageUrl = "${ApiModule.BASE_IMAGE_URL}/${skillEntity.getDrawResName()}.png"
            Picasso.get()
                .load(monsterImageUrl)
                .placeholder(R.drawable.splash_logo)
                .into(rootHolder.ivSkill)

            rootHolder.tvSkillName.text = skillEntity.name
        } else {
            val nodeHolder = holder as NodeViewHolder
            val skillEntity = skillsList[position]
            val monsterImageUrl = "${ApiModule.BASE_IMAGE_URL}/${skillEntity.getDrawResName()}.png"
            Picasso.get()
                .load(monsterImageUrl)
                .placeholder(R.drawable.splash_logo)
                .into(nodeHolder.ivSkill)

            nodeHolder.tvSkillName.text = skillEntity.name
            nodeHolder.ivSeperator.visibility = View.VISIBLE
        }

        holder.itemView.setOnClickListener{
            clickListener(skillsList[holder.adapterPosition])
        }


    }


    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            ROOT
        } else {
            NODE
        }
    }

    class RootViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val ivSkill = itemview.findViewById<ImageView>(R.id.ivSkill)
        val tvSkillName = itemview.findViewById<TextView>(R.id.tvSkillName)
    }

    class NodeViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val ivSkill = itemview.findViewById<ImageView>(R.id.ivSkill)
        val tvSkillName = itemview.findViewById<TextView>(R.id.tvSkillName)
        val ivSeperator = itemview.findViewById<ImageView>(R.id.ivSeperator)
    }

    companion object {
        private const val ROOT = 0
        private const val NODE = 1
    }
}