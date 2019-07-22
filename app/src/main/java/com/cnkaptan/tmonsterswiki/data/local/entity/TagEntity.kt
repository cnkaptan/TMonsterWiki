package com.cnkaptan.tmonsterswiki.data.local.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags_table")
data class TagEntity(
    @PrimaryKey
    @NonNull
    var id: Int = 0,
    @NonNull
    var name: String = "",
    @NonNull
    var description: String = "",
    @NonNull
    var resourceCode: String = ""
) : BaseEntity()