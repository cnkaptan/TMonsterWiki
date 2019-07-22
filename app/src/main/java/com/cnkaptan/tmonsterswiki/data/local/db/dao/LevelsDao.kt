package com.cnkaptan.tmonsterswiki.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.cnkaptan.tmonsterswiki.data.local.entity.BaseEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterLevelEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.SkillEntity
import io.reactivex.Single

@Dao
interface LevelsDao : BaseDao<MonsterLevelEntity>{
    @Query("SELECT * from ${BaseEntity.MONSTER_LEVELS_TABLE} ORDER BY level ASC")
    fun getLevels(): Single<List<MonsterLevelEntity>>

    @Query("DELETE FROM ${BaseEntity.MONSTER_LEVELS_TABLE}")
    fun nukeBomb()
}