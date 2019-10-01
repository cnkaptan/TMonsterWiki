package com.cnkaptan.tmonsterswiki.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.cnkaptan.tmonsterswiki.data.local.entity.BaseEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import io.reactivex.Single

@Dao
interface MonsterDao : BaseDao<MonsterEntity> {
    @Query("SELECT * from ${BaseEntity.MONSTERS_TABLE} ORDER BY monsterId ASC")
    fun getAllMonsters(): Single<List<MonsterEntity>>

    @Query("DELETE FROM ${BaseEntity.MONSTERS_TABLE}")
    fun nukeBomb()

    @Query("SELECT * FROM  ${BaseEntity.MONSTERS_TABLE} WHERE monsterId=:mId")
    fun findMonster(mId: Int): Single<MonsterEntity>
}