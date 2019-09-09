package com.cnkaptan.tmonsterswiki.ui.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.data.repository.MonsterRepository
import com.cnkaptan.tmonsterswiki.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

//{}
class MonsterMatchViewModel @Inject constructor(private val monsterRepository: MonsterRepository) :
    BaseViewModel() {

    private var monsterList: MutableLiveData<List<MonsterEntity>?> = MutableLiveData()

    fun loadMonster() {
        disposibleContainer.add(
            monsterRepository.getAllMonstersForVisiualize()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                        monsterList.postValue(it)
                }, { error -> Log.e(ContentValues.TAG, error.message, error) })
        )
    }

    fun getMonsterList(): LiveData<List<MonsterEntity>?>{
        return monsterList
    }
}