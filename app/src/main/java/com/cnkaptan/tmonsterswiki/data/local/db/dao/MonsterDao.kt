package com.cnkaptan.tmonsterswiki.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cnkaptan.tmonsterswiki.data.local.entity.BaseEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.SkillEntity
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface MonsterDao: BaseDao<MonsterEntity>{
    @Query("SELECT * from ${BaseEntity.MONSTERS_TABLE} ORDER BY id ASC")
    fun getAllMonsters(): Single<List<MonsterEntity>>

    @Query("DELETE FROM ${BaseEntity.MONSTERS_TABLE}")
    fun nukeBomb()
}