package com.cnkaptan.tmonsterswiki.di.module

import android.app.Application
import com.cnkaptan.tmonsterswiki.data.MonsterUpgrade
import com.cnkaptan.tmonsterswiki.data.MonsterUpgradeModel
import com.cnkaptan.tmonsterswiki.data.local.db.MonsterRoomDb
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule{
    @Provides
    @Singleton
    fun provideDb(application: Application): MonsterRoomDb{
        return MonsterRoomDb.getDatabase(application)
    }

    @Provides
    @Singleton
    fun provideMonsterDao(monsterRoomDb: MonsterRoomDb) = monsterRoomDb.monsterDao()

    @Provides
    @Singleton
    fun provideLevelsDao(monsterRoomDb: MonsterRoomDb) = monsterRoomDb.monsterLevelsDao()

    @Provides
    @Singleton
    fun provideSkillsDao(monsterRoomDb: MonsterRoomDb) = monsterRoomDb.skillsDao()

    @Provides
    @Singleton
    fun provideTagsDao(monsterRoomDb: MonsterRoomDb) = monsterRoomDb.tagsDao()

    @Provides
    @Singleton
    fun provideMonsterUpgradeInfo():MonsterUpgrade{
        val monsterUpgrade=MonsterUpgrade()
        return monsterUpgrade
    }

    @Provides
    @Singleton
    fun provideMonsterUpgradeModel():MonsterUpgradeModel{
        val monsterUpgradeModel=MonsterUpgradeModel()
        return monsterUpgradeModel
    }

}