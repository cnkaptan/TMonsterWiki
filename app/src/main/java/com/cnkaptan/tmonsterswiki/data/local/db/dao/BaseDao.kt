package com.cnkaptan.tmonsterswiki.data.local.db.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cnkaptan.tmonsterswiki.data.local.entity.BaseEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import io.reactivex.Completable
import io.reactivex.Single

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(monster: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(monsters: List<T>): Completable
}