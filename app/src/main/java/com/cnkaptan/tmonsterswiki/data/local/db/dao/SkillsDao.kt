package com.cnkaptan.tmonsterswiki.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.cnkaptan.tmonsterswiki.data.local.entity.BaseEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.SkillEntity
import io.reactivex.Single

@Dao
interface SkillsDao: BaseDao<SkillEntity>{
    @Query("SELECT * from ${BaseEntity.SKILLS_TABLE} ORDER BY id ASC")
    fun getAllSkills(): Single<List<SkillEntity>>

    @Query("DELETE FROM ${BaseEntity.SKILLS_TABLE}")
    fun nukeBomb()
}