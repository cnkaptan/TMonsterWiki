package com.cnkaptan.tmonsterswiki.ui.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cnkaptan.tmonsterswiki.data.MonsterUpgradeModel
import com.cnkaptan.tmonsterswiki.data.repository.MonsterRepository
import com.cnkaptan.tmonsterswiki.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MonsterUpgradeViewModel @Inject constructor(private val monsterRepository: MonsterRepository) :
    BaseViewModel() {

    private var monsterUpgradeInfo: MutableLiveData<MonsterUpgradeModel> = MutableLiveData()

    fun loadMonster(rarity: Int) {
        disposibleContainer.add(
            monsterRepository.getMonsterCommonUpgradeInfo(rarity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({monsterUpgradeInfo.postValue(it)
               // Log.e(TAG,it.toString())
                },
                    { error -> Log.e(TAG, error.message, error) })
        )
    }

    fun getUpgradeInfo(): LiveData<MonsterUpgradeModel> {
        return monsterUpgradeInfo
    }
}