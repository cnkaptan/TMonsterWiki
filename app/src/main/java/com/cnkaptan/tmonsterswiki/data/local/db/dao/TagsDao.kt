package com.cnkaptan.tmonsterswiki.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.cnkaptan.tmonsterswiki.data.local.entity.BaseEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.TagEntity
import io.reactivex.Single

@Dao
interface TagsDao: BaseDao<TagEntity>{
    @Query("SELECT * from ${BaseEntity.TAGS_TABLE} ORDER BY tadId ASC")
    fun getAllTags(): Single<List<TagEntity>>

    @Query("SELECT * from ${BaseEntity.TAGS_TABLE} where tadId = :tagId")
    fun getTagById(tagId: Int): Single<TagEntity>

    @Query("DELETE FROM ${BaseEntity.TAGS_TABLE}")
    fun nukeBomb()
}