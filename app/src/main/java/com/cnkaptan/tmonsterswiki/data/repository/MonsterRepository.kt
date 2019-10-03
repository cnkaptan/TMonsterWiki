package com.cnkaptan.tmonsterswiki.data.repository

import android.util.Log
import com.cnkaptan.tmonsterswiki.data.MonsterUpgrade
import com.cnkaptan.tmonsterswiki.data.MonsterUpgradeModel
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
    private val skillsDao: SkillsDao,
    private val monsterUpgrade: MonsterUpgrade,
    private val monsterUpgradeModel: MonsterUpgradeModel
) {


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

    fun getMonsterCommonUpgradeInfo(rarity: Int): Single<MonsterUpgradeModel> {

        when (rarity) {
            1 -> return Single.just(
                monsterUpgradeModel.singleMonsterUpgrade(
                    monsterUpgrade.commonCardlist, monsterUpgrade.commonGoldlist,
                    monsterUpgrade.commonExplist, monsterUpgrade.commonCplist
                )
            )
            2 -> return Single.just(
                monsterUpgradeModel.singleMonsterUpgrade(
                    monsterUpgrade.epicCardlist, monsterUpgrade.epicGoldlist,
                    monsterUpgrade.epicExplist, monsterUpgrade.epicCplist
                )
            )
            3 -> return Single.just(
                monsterUpgradeModel.singleMonsterUpgrade(
                    monsterUpgrade.monstrousCardlist, monsterUpgrade.monstrousGoldlist,
                    monsterUpgrade.monstrousExplist, monsterUpgrade.monstrousCplist
                )
            )
            4-> return Single.just(
                monsterUpgradeModel.singleMonsterUpgrade(
                    monsterUpgrade.legendaryCardlist, monsterUpgrade.legendaryGoldlist,
                    monsterUpgrade.legendaryExplist, monsterUpgrade.legendaryCplist
                )
            )
            else->
                return Single.just(
                monsterUpgradeModel.singleMonsterUpgrade(
                    monsterUpgrade.commonCardlist, monsterUpgrade.commonGoldlist,
                    monsterUpgrade.commonExplist, monsterUpgrade.commonCplist
                ))
        }


    }


    fun getInitialAppData(): Completable {
        return getLocalMonsters()
            .filter { t: List<MonsterEntity> -> t.isEmpty() }
            .flatMapCompletable {
                Log.e("MonsterRepository", it.toString())
                (monstersApi.fetchFetchMainInfos()
                    .doOnSuccess { (Log.e("Monster Repository", "Check")) }
                    .flatMapCompletable { response ->
                        insertMonstersFromResponse(response)
                            .andThen(tagsDao.insertList(response.tags))
                            .andThen(skillsDao.insertList(response.skills))
                    })
            }.subscribeOn(Schedulers.io())
    }

    private fun insertMonstersFromResponse(response: MonsterMainResponse): Completable {
        return Flowable.fromIterable(response.monsters)
            .flatMap {
                insertSingleMonster(it)
                    .andThen(Flowable.just(it))
            }
            .flatMap {
                insertMonsterLevels(it.levels).toCompletable().andThen(Flowable.just(it))
            }.toList()
            .toCompletable()
    }

    fun getAllMonstersForVisiualize(): Single<List<MonsterEntity>> {
        return monsterDao.getAllMonsters()
            .map { it.reversed() }
            .flatMapPublisher { Flowable.fromIterable(it) }
            .toList()
            .map { it.sortedWith(compareBy { it.levels.get(1).hp }) }
    }

    fun getMonster(id: Int): Single<MonsterEntity> {
        return monsterDao.findMonster(id)
    }

    fun getTagById(id: Int): Single<TagEntity> {
        return tagsDao.getTagById(id)
    }


    fun getSkillById(skillId: Int): Single<SkillEntity> {
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