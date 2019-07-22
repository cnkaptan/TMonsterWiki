package com.cnkaptan.tmonsterswiki.data.local.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "skill_table")
data class SkillEntity(
    @PrimaryKey
    @NonNull
    var id: Int = 0,
    @NonNull
    var name: String = "",
    @NonNull
    var useCost: String = "",
    @NonNull
    var description: String = "",
    @NonNull
    var params: String = "",
    @NonNull
    var resourceCode: String = ""
) : BaseEntity()