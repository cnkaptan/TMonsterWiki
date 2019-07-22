package com.cnkaptan.tmonsterswiki.data.local.db

import androidx.room.TypeConverter
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class IdTypeConverter {
    @TypeConverter
    fun fromIds(ids: List<Int>): String {
        val gson = GsonBuilder().create()
        return gson.toJson(ids)
    }

    @TypeConverter
    fun fromJson(json: String): List<Int> {
        val gson = GsonBuilder().create()
        val typeToken = object : TypeToken<ArrayList<Int>>() {}
        return gson.fromJson(json, typeToken.type)
    }

}