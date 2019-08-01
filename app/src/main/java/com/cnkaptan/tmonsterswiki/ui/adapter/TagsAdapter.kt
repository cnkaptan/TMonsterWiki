package com.cnkaptan.tmonsterswiki.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.local.entity.TagEntity
import com.squareup.picasso.Picasso

class TagsAdapter(val context: Context, val tags: List<TagEntity>, val listener: (TagEntity) -> Unit) :
    RecyclerView.Adapter<TagsAdapter.TagsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_tags_content, parent, false)
        return TagsViewHolder(itemView)
    }

    override fun getItemCount() = tags.size

    override fun onBindViewHolder(holder: TagsViewHolder, position: Int) {
        val tagEntity = tags[position]
        val drawResName = tagEntity.getDrawResName()
//        Log.e("TagsAdapter", drawResName)
        val drawableId = context.resources.getIdentifier(drawResName, "drawable", context.packageName)
        if (drawableId > 0) {
            Picasso.get().load(drawableId).into(holder.ivTag)
        } else {
            holder.ivTag.setImageResource(R.drawable.tm_splash_image)
        }

        holder.itemView.setOnClickListener {
            listener(tags[holder.adapterPosition])
        }
    }

    class TagsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivTag: ImageView = itemView.findViewById(R.id.ivTag)
    }
}