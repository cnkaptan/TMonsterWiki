package com.cnkaptan.tmonsterswiki.remote.api

import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterLevelEntity
import com.cnkaptan.tmonsterswiki.remote.model.MonsterMainResponse
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MonstersApi {

    @GET("main_info")
    fun fetchFetchMainInfos(): Single<MonsterMainResponse>

    @GET("levels/{monsterId}")
    fun fetchMonsterLevelsById(@Path("monsterId") id: Int): Single<List<MonsterLevelEntity>>
}