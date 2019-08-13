package com.cnkaptan.tmonsterswiki.remote.api

import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterLevelEntity
import com.cnkaptan.tmonsterswiki.remote.model.MonsterMainResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface MonstersApi {
    @GET("main_data")
    fun fetchFetchMainInfos(): Single<MonsterMainResponse>
}