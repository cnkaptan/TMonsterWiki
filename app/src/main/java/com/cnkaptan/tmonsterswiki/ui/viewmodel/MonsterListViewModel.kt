package com.cnkaptan.tmonsterswiki.ui.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.data.repository.MonsterRepository
import com.cnkaptan.tmonsterswiki.ui.base.BaseViewModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MonsterListViewModel @Inject constructor(private val monsterRepository: MonsterRepository) : BaseViewModel() {

    private var monstersGroups: MutableLiveData<List<Map.Entry<Int, List<MonsterEntity>>>> = MutableLiveData()

    fun loadMonsters() {
        disposibleContainer.add(
            monsterRepository.getAllMonsters()
//                .flatMapPublisher { Flowable.fromIterable(it) }
//                .doOnNext { Log.e("MonsterListViewModel","${it.name} --> ${it.resourceCode}") }
//                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val sortedBy = it.groupBy(MonsterEntity::rarity).entries.toList()
                        .sortedBy { rarityList -> -rarityList.key }
                    monstersGroups.postValue(sortedBy)
                }, { error -> Log.e(TAG, error.message, error) })
        )
    }

    fun getMonsterGroups(): MutableLiveData<List<Map.Entry<Int, List<MonsterEntity>>>> {
        return monstersGroups
    }

}