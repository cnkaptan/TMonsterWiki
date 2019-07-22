package com.cnkaptan.tmonsterswiki.data.local.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.cnkaptan.tmonsterswiki.data.local.db.IdTypeConverter

@Entity(tableName = BaseEntity.MONSTERS_TABLE)
@TypeConverters(IdTypeConverter::class)
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