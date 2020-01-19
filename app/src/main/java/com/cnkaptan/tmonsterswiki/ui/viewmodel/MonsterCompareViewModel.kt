package com.cnkaptan.tmonsterswiki.ui.viewmodel

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MonsterCompareViewModel @Inject constructor( val app: Application) :
    BaseViewModel(app) {

    private val _selectedLevel: MutableLiveData<Int> = MutableLiveData(23)

    val selectedLevel: LiveData<String> = Transformations.map(_selectedLevel){
        it.toString()
    }

    private val _selectedRatio: MutableLiveData<MonsterCompareRatio> =
        MutableLiveData(MonsterCompareRatio.HP)
    val selectedRatio: LiveData<MonsterCompareRatio> get() = _selectedRatio
    private var monsterWithLevels: MutableLiveData<List<MonsterEntity>> = MutableLiveData()

    init {
        loadMonstersWithLevels()
    }

    private fun loadMonstersWithLevels() {
        this["init_data"] = monsterRepository.getAllMonstersForVisiualize()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _selectedLevel.value = 23
                    _selectedRatio.value = MonsterCompareRatio.HP
                    updateMonsterCompareList(it)
                }, { error -> Log.e(ContentValues.TAG, error.message, error) })

    }

    fun getMonsterWithLevels(): LiveData<List<MonsterEntity>> {
        return monsterWithLevels
    }

    private fun updateMonsterCompareList(monsterEntityList: List<MonsterEntity>? = null) {
        val monsterList = monsterEntityList?: monsterWithLevels.value ?: return
        val request = MonsterCompareRequest(_selectedRatio.value!!, _selectedLevel.value!!)

        monsterWithLevels.value = monsterList
            .sortedWith(getMonsterComparator(request))
            .reversed()
    }

    fun setSelectedSortingRatio(monsterCompareRatio: MonsterCompareRatio) {
        _selectedRatio.value = monsterCompareRatio
        updateMonsterCompareList()
    }

    fun setSelectedLevel(selectedLevel: Int) {
        _selectedLevel.value = selectedLevel
        updateMonsterCompareList()
    }

    private fun getMonsterComparator(request: MonsterCompareRequest): Comparator<MonsterEntity> {
        return when (request.monsterCompareRatio) {
            MonsterCompareRatio.HP -> compareBy { it.levels[request.level - 1].hp }
            MonsterCompareRatio.DMG -> compareBy { it.levels[request.level - 1].dmg }
            MonsterCompareRatio.SPEED -> compareBy { it.levels[request.level - 1].speed }
        }
    }
}

data class MonsterCompareRequest(val monsterCompareRatio: MonsterCompareRatio = MonsterCompareRatio.HP, val level: Int = 23)

enum class MonsterCompareRatio {
    HP, DMG, SPEED
}
