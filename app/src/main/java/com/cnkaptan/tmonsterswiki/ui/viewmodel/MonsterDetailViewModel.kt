package com.cnkaptan.tmonsterswiki.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterLevelEntity
import com.cnkaptan.tmonsterswiki.data.repository.MonsterRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MonsterDetailViewModel @Inject constructor(monsterRepository: MonsterRepository) : ViewModel() {

    private var monster: MutableLiveData<MonsterEntity> = MutableLiveData()
    private var monsterLevelList: MutableLiveData<List<MonsterLevelEntity>> = MutableLiveData()

    private val subscriptions = CompositeDisposable()
    private val monsterRepository: MonsterRepository = monsterRepository

    fun loadMonsterLevel(monsterID: Int) {

        subscriptions.add(
            monsterRepository.getMonsterLevel(monsterID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ getMonsterLevelList().postValue(it) }
                    , { error -> Log.e("MonsterDetailViewModel", error.message, error) })
        )

        subscriptions.add(
            monsterRepository.getMonster(monsterID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ getMonster().postValue(it) }
                    , { error -> Log.e("MonsterDetailViewModel", error.message, error) })
        )

    }

    fun getMonsterLevelList(): MutableLiveData<List<MonsterLevelEntity>> {
        return monsterLevelList
    }

    fun getMonster(): MutableLiveData<MonsterEntity> {
        return monster
    }

}