package com.cnkaptan.tmonsterswiki.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cnkaptan.tmonsterswiki.data.local.db.dao.LevelsDao
import com.cnkaptan.tmonsterswiki.data.local.db.dao.MonsterDao
import com.cnkaptan.tmonsterswiki.data.local.db.dao.SkillsDao
import com.cnkaptan.tmonsterswiki.data.local.db.dao.TagsDao
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterLevelEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.SkillEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.TagEntity

@Database(
    entities = [MonsterEntity::class,
        SkillEntity::class,
        TagEntity::class,
        MonsterLevelEntity::class],
    version = 2,
    exportSchema = false
)
abstract class MonsterRoomDb : RoomDatabase() {

    abstract fun monsterDao(): MonsterDao
    abstract fun skillsDao(): SkillsDao
    abstract fun tagsDao(): TagsDao
    abstract fun monsterLevelsDao(): LevelsDao

    companion object {
        @Volatile
        private var INSTANCE: MonsterRoomDb? = null

        fun getDatabase(context: Context): MonsterRoomDb {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MonsterRoomDb::class.java,
                    "Monster_Db"
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return instance

            }
        }
    }
}