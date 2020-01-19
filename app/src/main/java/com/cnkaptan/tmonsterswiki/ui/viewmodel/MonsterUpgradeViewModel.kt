package com.cnkaptan.tmonsterswiki.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cnkaptan.tmonsterswiki.data.MonsterUpgrade
import com.cnkaptan.tmonsterswiki.ui.base.BaseViewModel
import javax.inject.Inject

class MonsterUpgradeViewModel @Inject constructor(app: Application) : BaseViewModel(app) {

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