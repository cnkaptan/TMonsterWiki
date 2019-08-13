package com.cnkaptan.tmonsterswiki.ui.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterLevelEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.SkillEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.TagEntity
import com.cnkaptan.tmonsterswiki.data.repository.MonsterRepository
import com.cnkaptan.tmonsterswiki.ui.ADD
import com.cnkaptan.tmonsterswiki.ui.CHANGE
import com.cnkaptan.tmonsterswiki.ui.DONE
import com.cnkaptan.tmonsterswiki.ui.SkillChanges
import com.cnkaptan.tmonsterswiki.ui.base.BaseViewModel
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.SingleOperator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import kotlin.math.abs

class MonsterDetailViewModel @Inject constructor(private val monsterRepository: MonsterRepository) : BaseViewModel() {

    private var monster: MutableLiveData<MonsterEntity> = MutableLiveData()
    private var monsterLevelList: MutableLiveData<List<MonsterLevelEntity>> = MutableLiveData()
    private var skillList: MutableLiveData<List<SkillEntity>> = MutableLiveData()
    private var tagList: MutableLiveData<List<TagEntity>> = MutableLiveData()

    fun loadMonsterLevel(monsterID: Int) {

        disposibleContainer.add(
            monsterRepository.getMonsterLevelsByMonsterIdId(monsterID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ monsterLevelList.postValue(it.sortedByDescending { it.level }) }
                    , { error -> Log.e("MonsterDetailViewModel", error.message, error) })
        )

        disposibleContainer.add(
            monsterRepository.getMonster(monsterID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ monster.postValue(it) }
                    , { error -> Log.e("MonsterDetailViewModel", error.message, error) })
        )


        disposibleContainer.add(
            monsterRepository.getMonster(monsterID)
                .subscribeOn(Schedulers.io())
                .flattenAsFlowable { it.tags }
                .flatMapSingle { monsterRepository.getTagById(it) }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { tagList.postValue(it) },
                    { error -> Log.e(TAG, error.message, error) })
        )

        disposibleContainer.add(
            monsterRepository.getMonsterLevelsByMonsterIdId(monsterID)
                .subscribeOn(Schedulers.io())
                .flattenAsFlowable { it.last().skillIds }
                .flatMapSingle { monsterRepository.getSkillById(it) }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        skillList.postValue(it)
                    },
                    { error -> Log.e(TAG, error.message, error) })
        )

    }

    fun getSkillChangesRx(skillId: Int) = Single.fromCallable { getSkillChanges(skillId) }
        .flatMapPublisher { Flowable.fromIterable(it) }
        .map { it.skillId }
        .collectInto(mutableSetOf<Int>(),{ myset, itemSkillId->
            myset.add(itemSkillId!!)
        }).map {
            it.sortedBy { it }.toList()
        }
        .subscribeOn(Schedulers.io())


    fun getEvoluationSkillSet(skillId: Int): Single<MutableList<SkillEntity>> {
        return getSkillChangesRx(skillId)
        .flatMapPublisher { Flowable.fromIterable(it) }
            .flatMap { monsterRepository.getSkillById(it).toFlowable() }
            .toList()
            .subscribeOn(Schedulers.io())
    }

    fun getSkillChanges(skillId: Int): List<SkillChanges> {
        if (getMonsterLevelListLD().value != null) {
            val levelsList = getMonsterLevelListLD().value!!
            val skillsIDList: MutableList<Int> = mutableListOf()
            val skillChanges = mutableListOf<SkillChanges>()
            var inSkillId = skillId
            for (i in 0 until levelsList.size) {
                val levelEntity = levelsList[i]
//                Log.e(TAG, levelEntity.toString())
                if (levelEntity.level == 23) {
                    val skillIdsInLevel = levelEntity.skillIds
                    skillsIDList.addAll(skillIdsInLevel)

                    if (skillsIDList.find { it == inSkillId } != null) {
                        skillsIDList.clear()
                        skillsIDList.add(skillId)
                        skillChanges.add(
                            SkillChanges(
                                levelEntity = levelEntity,
                                skillId = inSkillId, inSkillId = null, outSkillId = null, Status = ADD
                            )
                        )
//                        Log.e(TAG, " $skillChanges")
                    }
                } else if ((levelEntity.level > 1) and (levelEntity.level < 23)) {
                    val skillIdsInLevel = levelEntity.skillIds
                    if (skillIdsInLevel.find { it == inSkillId } == null) {
//                        Log.e(TAG, "inSkillId = $inSkillId, ${levelsList[i]}")
                        val preLevelEntity = skillChanges.find { it.skillId == inSkillId }!!.levelEntity
                        val subList = skillIdsInLevel - preLevelEntity.skillIds
                        if (subList.isNotEmpty()) {
                            val outSkillId = inSkillId
                            if (subList.size == 1) {
                                if (abs(subList[0] - inSkillId) < 3) {
                                    inSkillId = subList[0]
                                    skillChanges.add(
                                        SkillChanges(
                                            CHANGE, inSkillId, outSkillId, inSkillId,
                                            levelEntity
                                        )
                                    )
                                } else {
                                    val tmp = skillChanges.find { it.Status == ADD }!!
                                    skillChanges.add(SkillChanges(DONE, tmp.skillId, null, tmp.skillId, levelEntity))
                                    return skillChanges
                                }
                            } else {
                                val newInSkillId = subList.map {
                                    Pair(it, abs(it - inSkillId))
                                }.filter {
                                    it.second < 3
                                }

                                if (newInSkillId.size == 1){
                                    skillChanges.add(SkillChanges(CHANGE, newInSkillId[0].first, inSkillId, newInSkillId[0].first,levelEntity))
                                    inSkillId = newInSkillId[0].first
                                }else if(newInSkillId.isNullOrEmpty()){
                                    val tmp = skillChanges.find { it.Status == ADD }!!
                                    skillChanges.add(SkillChanges(DONE, tmp.skillId, null, tmp.skillId, levelEntity))
                                    return skillChanges
                                }

                            }

                        } else {
                            val tmp = skillChanges[skillChanges.size - 1]
                            val changedOne = SkillChanges(DONE, tmp.skillId, null, null, tmp.levelEntity)
                            skillChanges.add(changedOne)
                            return skillChanges
                        }
                        skillsIDList.clear()
                        skillsIDList.add(skillId)
                    }
                } else if (levelEntity.level == 1) {
                    if (skillChanges.find { it.Status == CHANGE } != null) {
                        val tmp = skillChanges[skillChanges.size - 1]
                        val changedOne = SkillChanges(DONE, tmp.skillId, null, null, tmp.levelEntity)
                        skillChanges.add(changedOne)
                    }
                }
            }
            skillChanges.forEach {
//                Log.e("skillChanges", it.toString())
            }
            return skillChanges
        }

        return emptyList()

    }

    fun getMonsterLevelListLD(): LiveData<List<MonsterLevelEntity>> {
        return monsterLevelList
    }

    fun getMonsterLD(): LiveData<MonsterEntity> {
        return monster
    }

    fun getSkillListLD(): LiveData<List<SkillEntity>> {
        return skillList
    }

    fun getTagListLD(): LiveData<List<TagEntity>> {
        return tagList
    }

}