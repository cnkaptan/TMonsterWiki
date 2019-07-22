package com.cnkaptan.tmonsterswiki.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.cnkaptan.tmonsterswiki.data.local.db.IdTypeConverter

@Entity(tableName = BaseEntity.MONSTER_LEVELS_TABLE, primaryKeys = ["monsterId", "level"])
@TypeConverters(IdTypeConverter::class)
data class MonsterLevelEntity(
    val monsterId: Int,
    val level: Int,
    val skillIds: List<Int>,
    val hp: Int,
    val dmg: Int,
    val phyDef: Int,
    val magDef: Int,
    val speed: Int,
    val move: Int,
    val initAp: Int,
    val critical: Int,
    val dodge: Int,
    val hit: Int,
    val specialUpgrade: String
):BaseEntity()
