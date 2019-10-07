package com.cnkaptan.tmonsterswiki.data.repository

import android.util.Log
import com.cnkaptan.tmonsterswiki.data.MonsterUpgrade
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

    companion object{
        const val TAG = "MonsterRepository"
    }

    fun insertMonsters(monsterList: List<MonsterEntity>): Single<List<MonsterEntity>> {
        return monsterDao.insertList(monsterList)
            .subscribeOn(Schedulers.io())
            .andThen(monsterDao.getAllMonsters())
    }

    fun insertSingleMonster(monster: MonsterEntity): Completable {
        return monsterDao.insertOrUpdate(monster)
            .subscribeOn(Schedulers.io())
    }

    fun insertMonsterLevels(levels: List<MonsterLevelEntity>): Single<List<MonsterLevelEntity>> {
        return levelsDao.insertList(levels)
            .subscribeOn(Schedulers.io())
            .andThen(levelsDao.getLevels())
    }

    fun getInitialAppData(): Completable {
        return getLocalMonsters()
            .filter { t: List<MonsterEntity> -> t.isEmpty() }
            .flatMapCompletable {
                Log.e(TAG, it.toString())
                (monstersApi.fetchFetchMainInfos()
                    .doOnSuccess { (Log.e(TAG, "Check")) }
                    .doOnSuccess { Log.e(TAG,"Monsters = ${it.monsters.size}") }
                    .flatMapCompletable { response ->
                        insertMonstersFromResponse(response)
                            .andThen(tagsDao.insertList(response.tags))
                            .andThen(skillsDao.insertList(response.skills))
                    })
            }.subscribeOn(Schedulers.io())
    }

    private fun insertMonstersFromResponse(response: MonsterMainResponse): Completable {
        return Flowable.fromIterable(response.monsters)
            .flatMap {monsterEntity->
                insertSingleMonster(monsterEntity)
                    .andThen(monsterDao.findMonster(monsterEntity.monsterId))
                    .toFlowable()
            }
            .flatMap {
                insertMonsterLevels(it.levels).toCompletable().andThen(Flowable.just(it))
            }.toList()
            .toCompletable()
    }

    fun getAllMonstersForVisiualize(): Single<List<MonsterEntity>> {
        return monsterDao.getAllMonsters()
            .map { it.reversed() }
            .doOnSuccess { Log.e(TAG,"${it.size}") }
            .flatMapPublisher { Flowable.fromIterable(it) }
            .toList()
            .map { it.sortedWith(compareBy { it.levels.get(1).hp }) }
    }

    fun getMonster(id: Int): Single<MonsterEntity> {
        return monsterDao.findMonster(id)
    }

    fun getTagById(id: Int): Single<TagEntity> {
        Log.e(TAG,id.toString())
        return tagsDao.getTagById(id)
    }


    fun getSkillById(skillId: Int): Single<SkillEntity> {
        Log.e(TAG,skillId.toString())
        return skillsDao.getSkillById(skillId)
    }

    fun getLocalMonsters(): Single<List<MonsterEntity>> {
        return monsterDao.getAllMonsters()
    }

    fun getLocalLevels(): Single<List<MonsterLevelEntity>> {
        return levelsDao.getLevels()
    }

    fun getMonsterLevelsByMonsterIdId(monsterId: Int): Single<List<MonsterLevelEntity>> {
        return levelsDao.getMonsterLevels(monsterId)
            .subscribeOn(Schedulers.io())
    }
}