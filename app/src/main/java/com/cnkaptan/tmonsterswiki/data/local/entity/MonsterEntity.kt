package com.cnkaptan.tmonsterswiki.data.local.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monsters_table")
data class MonsterEntity(
    @PrimaryKey
    @NonNull
    var id: Int = 0,
    @NonNull
    var name: String = "",
    @NonNull
    var rarity: Int = 1,
    @NonNull
    var attackType: Int = 1,
    @NonNull
    var damageType: Int = 1,
    @NonNull
    var intro: String = "",
    @NonNull
    var resourceCode: String = "",
    @NonNull
    var tags: List<Int> = mutableListOf()
) : BaseEntity()