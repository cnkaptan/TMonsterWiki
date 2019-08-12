package com.cnkaptan.tmonsterswiki.ui.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.data.repository.MonsterRepository
import com.cnkaptan.tmonsterswiki.ui.base.BaseViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MonsterListViewModel @Inject constructor(private val monsterRepository: MonsterRepository) : BaseViewModel() {

    private var monsterGroups: MutableLiveData<List<Map.Entry<Int, List<MonsterEntity>>>> = MutableLiveData()
    private var monsterList: MutableLiveData<List<MonsterEntity>?> = MutableLiveData()

    fun loadMonsters() {
        disposibleContainer.add(
            monsterRepository.getAllMonsters()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { monsterList.postValue(it) }
                .subscribe({
                    val sortedBy = it.groupBy(MonsterEntity::rarity).entries.toList()
                        .sortedBy { rarityList -> -rarityList.key }
                    monsterGroups.postValue(sortedBy)
                }, { error -> Log.e(TAG, error.message, error) })
        )
    }

    fun getMonsterList(): LiveData<List<MonsterEntity>?> {
        return monsterList
    }

    fun getMonsterGroups(): MutableLiveData<List<Map.Entry<Int, List<MonsterEntity>>>> {
        return monsterGroups
    }

}