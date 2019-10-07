package com.cnkaptan.tmonsterswiki.ui.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cnkaptan.tmonsterswiki.data.MonsterUpgrade
import com.cnkaptan.tmonsterswiki.data.repository.MonsterRepository
import com.cnkaptan.tmonsterswiki.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MonsterUpgradeViewModel @Inject constructor(private val monsterRepository: MonsterRepository) :
    BaseViewModel() {

    private var monsterUpgradeInfo: MutableLiveData<MonsterUpgrade.CalculationDataGroup> = MutableLiveData()

    fun loadMonster(rarity: Int): MonsterUpgrade.CalculationDataGroup {
        val dataGroup =  when(rarity){
            1 -> MonsterUpgrade.CommonUpgradeDatas
            2 -> MonsterUpgrade.EpicUpgradeDatas
            3 -> MonsterUpgrade.MonstrousUpgradeDatas
            4 -> MonsterUpgrade.LegendaryUpgradeDatas
            else -> MonsterUpgrade.LegendaryUpgradeDatas
        }

        monsterUpgradeInfo.postValue(dataGroup)
        return dataGroup
    }

    fun getUpgradeInfo(): LiveData<MonsterUpgrade.CalculationDataGroup> {
        return monsterUpgradeInfo
    }
}