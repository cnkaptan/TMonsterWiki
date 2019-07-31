package com.cnkaptan.tmonsterswiki.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation


class MonsterWithLevelEntity {
    @Embedded
    var monsterEntity: MonsterEntity? = null

    @Relation(parentColumn = "id", entityColumn = "monsterId", entity = MonsterLevelEntity::class)
    var monsterLevels: List<MonsterLevelEntity>? = null
}