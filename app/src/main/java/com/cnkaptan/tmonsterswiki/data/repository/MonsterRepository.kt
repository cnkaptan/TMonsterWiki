package com.cnkaptan.tmonsterswiki.data.repository

import com.cnkaptan.tmonsterswiki.data.local.db.dao.LevelsDao
import com.cnkaptan.tmonsterswiki.data.local.db.dao.MonsterDao
import com.cnkaptan.tmonsterswiki.data.local.db.dao.SkillsDao
import com.cnkaptan.tmonsterswiki.data.local.db.dao.TagsDao
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterLevelEntity
import com.cnkaptan.tmonsterswiki.remote.api.MonstersApi
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MonsterRepository @Inject constructor(private val monstersApi: MonstersApi,
                                            private val monsterDao: MonsterDao,
                                            private val levelsDao: LevelsDao,
                                            private val tagsDao: TagsDao,
                                            private val skillsDao: SkillsDao) {

    fun insertMonster(monsterList: List<MonsterEntity>): Single<List<MonsterEntity>> {
        return monsterDao.insertList(monsterList)
            .subscribeOn(Schedulers.io())
            .andThen(monsterDao.getAllMonsters())
    }

    fun insertMonsterLevels(levels: List<MonsterLevelEntity>): Single<List<MonsterLevelEntity>> {
        return levelsDao.insertList(levels)
            .subscribeOn(Schedulers.io())
            .andThen(levelsDao.getLevels())
    }

    fun getMonsterLevel(id: Int):Single<List<MonsterLevelEntity>>{
        return  monstersApi.fetchMonsterLevelsById(id)
                .flatMap { levelsDao.insertList(it)
                    .andThen(levelsDao.getMonsterLevels(id))
                }
                .subscribeOn(Schedulers.io())
    }

    fun downloadInitialInfos(): Completable{
        return monstersApi.fetchFetchMainInfos()
            .flatMapCompletable {
                monsterDao.insertList(it.monsters)
                    .andThen(tagsDao.insertList(it.tags))
                    .andThen(skillsDao.insertList(it.skills))
            }
            .subscribeOn(Schedulers.io())
    }

    fun getAllMonsters(): Single<List<MonsterEntity>> {
        return monsterDao.getAllMonsters()
    }

    fun getMonster(id:Int):Single<MonsterEntity>{
        return monsterDao.findMonster(id)
    }
}