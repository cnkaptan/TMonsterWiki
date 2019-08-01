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
import com.cnkaptan.tmonsterswiki.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MonsterDetailViewModel @Inject constructor(private val monsterRepository: MonsterRepository) : BaseViewModel() {

    private var monster: MutableLiveData<MonsterEntity> = MutableLiveData()
    private var monsterLevelList: MutableLiveData<List<MonsterLevelEntity>> = MutableLiveData()
    private var skillList: MutableLiveData<List<SkillEntity>> = MutableLiveData()
    private var tagList: MutableLiveData<List<TagEntity>> = MutableLiveData()

    fun loadMonsterLevel(monsterID: Int) {

        disposibleContainer.add(
            monsterRepository.getMonsterLevels(monsterID)
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
            monsterRepository.getMonsterLevels(monsterID)
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