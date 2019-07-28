package com.cnkaptan.tmonsterswiki.ui.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.data.repository.MonsterRepository
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MonsterListViewModel @Inject constructor(monsterRepository: MonsterRepository) : ViewModel() {

    private var monstersGroups: MutableLiveData<List<Map.Entry<Int, List<MonsterEntity>>>> = MutableLiveData()

    private val subscriptions = CompositeDisposable()
    private val monsterRepository: MonsterRepository = monsterRepository

    fun loadMonsters() {
        subscriptions.add(
            monsterRepository.getAllMonsters()
                .flatMapPublisher { Flowable.fromIterable(it) }
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val sortedBy = it.groupBy(MonsterEntity::rarity).entries.toList()
                        .sortedBy { rarirtyList -> -rarirtyList.key }
                    monstersGroups.postValue(sortedBy)
                }, { error -> Log.e(TAG, error.message, error) })
        )
    }

    fun getMonsterGroups(): MutableLiveData<List<Map.Entry<Int, List<MonsterEntity>>>> {
        return monstersGroups
    }

}