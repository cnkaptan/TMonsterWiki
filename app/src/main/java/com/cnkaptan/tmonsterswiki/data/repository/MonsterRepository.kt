package com.cnkaptan.tmonsterswiki.data.repository

import android.util.Log
import com.cnkaptan.tmonsterswiki.data.local.db.dao.LevelsDao
import com.cnkaptan.tmonsterswiki.data.local.db.dao.MonsterDao
import com.cnkaptan.tmonsterswiki.data.local.db.dao.SkillsDao
import com.cnkaptan.tmonsterswiki.data.local.db.dao.TagsDao
import com.cnkaptan.tmonsterswiki.data.local.entity.*
import com.cnkaptan.tmonsterswiki.remote.api.MonstersApi
import com.cnkaptan.tmonsterswiki.remote.model.MonsterMainResponse
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MonsterRepository @Inject constructor(
    private val monstersApi: MonstersApi,
    private val monsterDao: MonsterDao,
    private val levelsDao: LevelsDao,
    private val tagsDao: TagsDao,
    private val skillsDao: SkillsDao
) {

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

    fun getMonsterLevels(id: Int): Single<List<MonsterLevelEntity>> {
        return monstersApi.fetchMonsterLevelsById(id)
            .flatMap {
                levelsDao.insertList(it)
                    .andThen(levelsDao.getMonsterLevels(id))
            }
            .subscribeOn(Schedulers.io())
    }

    fun downloadInitialInfos(): Completable {
        return monstersApi.fetchFetchMainInfos()
            .flatMapCompletable {
                monsterDao.insertList(it.monsters)
                    .andThen(tagsDao.insertList(it.tags))
                    .andThen(skillsDao.insertList(it.skills))
            }
            .subscribeOn(Schedulers.io())
    }

    fun downloadMonsterLevels(): Completable {
        return monstersApi.fetchFetchMainInfos()
            .toObservable()
            .flatMapIterable { t: MonsterMainResponse -> t.monsters }
            .flatMapCompletable { levelsDao.insertList(it.levels) }
            .subscribeOn(Schedulers.io())
    }

    fun getAllMonsters(): Single<List<MonsterEntity>> {
        return monsterDao.getAllMonsters().map { it.reversed() }
            .flatMapPublisher { Flowable.fromIterable(it) }
            .filter { it.id != 410 && it.id != 318 }
            .doOnNext { Log.e("MonsterRepository",it.id.toString()) }
            .toList()
    }

    fun getMonster(id: Int): Single<MonsterEntity> {
        return monsterDao.findMonster(id)
    }

    fun getTagById(id: Int): Single<TagEntity> {
        return tagsDao.getTagById(id)
    }

    fun getAllTags(): Single<List<TagEntity>> {
        return tagsDao.getAllTags()
    }

    fun getAllSkills(): Single<List<SkillEntity>> {
        return skillsDao.getAllSkills()
    }

    fun getSkillById(skillId: Int): Single<SkillEntity> {
        return skillsDao.getSkillById(skillId)
    }
}