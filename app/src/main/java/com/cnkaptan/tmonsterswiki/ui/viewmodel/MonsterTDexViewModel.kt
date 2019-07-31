package com.cnkaptan.tmonsterswiki.ui.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.data.repository.MonsterRepository
import com.cnkaptan.tmonsterswiki.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MonsterTDexViewModel @Inject constructor(private val monsterRepository: MonsterRepository) : BaseViewModel() {

    private var monsterWtihLevels: MutableLiveData<List<MonsterEntity>> = MutableLiveData()

    fun loadMonstersWithLevels() {
        disposibleContainer.add(
            monsterRepository.getAllMonsters()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.e("MONSTERLEVELWITH", it.get(0).levels.toString())
                    monsterWtihLevels.postValue(it)
                }, { error -> Log.e(ContentValues.TAG, error.message, error) })
        )

    }

    fun getMonsterWithLevels(): MutableLiveData<List<MonsterEntity>> {
        return monsterWtihLevels
    }

}