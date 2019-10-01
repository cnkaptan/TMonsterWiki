package com.cnkaptan.tmonsterswiki.data.local.entity

import androidx.room.*
import com.cnkaptan.tmonsterswiki.data.local.db.IdTypeConverter
import com.google.gson.annotations.SerializedName

@Entity(tableName = BaseEntity.MONSTER_LEVELS_TABLE, primaryKeys = ["monsterId", "level"])
@TypeConverters(IdTypeConverter::class)
data class MonsterLevelEntity(
    @SerializedName("monster_id")
    val monsterId: Int,
    val level: Int,
    val skillIds: List<Int>,
    val hp: Int,
    val dmg: Int,
    val phyDef: Int,
    val magDef: Int,
    val speed: Int,
    val move: Int,
    val monsterLevelInitAp: Int,
    val critical: Int,
    val dodge: Int,
    val hit: Int,
    val specialUpgrade: String
):BaseEntity()
