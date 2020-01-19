package com.cnkaptan.tmonsterswiki.ui.viewmodel

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.data.repository.MonsterRepository
import com.cnkaptan.tmonsterswiki.ui.base.BaseViewModel
import com.cnkaptan.tmonsterswiki.utils.playAnimation
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MonsterListViewModel @Inject constructor(app: Application) :
    BaseViewModel(app) {

    private var monsterGroups: MutableLiveData<List<Map.Entry<Int, List<MonsterEntity>>>> =
        MutableLiveData()

    private var monsterList: MutableLiveData<List<MonsterEntity>?> = MutableLiveData()

    private var searchList: MutableLiveData<List<MonsterEntity>?> = MutableLiveData()

    init {
        loadMonsters()
    }

    private fun loadMonsters() {
        this += monsterRepository.getAllMonstersForVisiualize()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
//            .flatMapPublisher { Flowable.fromIterable(it) }
//            .toList()
            .doOnSuccess { monsterList.postValue(it) }
            .subscribe({
                val sortedBy = it.groupBy(MonsterEntity::rarity).entries.toList()
                    .sortedBy { rarityList -> -rarityList.key }
                monsterGroups.postValue(sortedBy)
            }, { error -> Log.e(TAG, error.message, error) })
    }


    fun getMonsterGroups(): LiveData<List<Map.Entry<Int, List<MonsterEntity>>>> {
        return monsterGroups
    }

    fun getSearchUpdates(): LiveData<List<MonsterEntity>?> {
        return searchList
    }

    fun querySearch(searchText: String) {
        if (monsterList.value.isNullOrEmpty()) return

        val searchedList = monsterList.value!!.filter {
            it.name.toLowerCase().startsWith(searchText.toLowerCase(), ignoreCase = true)
        }.toList()
        searchList.value = searchedList
    }
}