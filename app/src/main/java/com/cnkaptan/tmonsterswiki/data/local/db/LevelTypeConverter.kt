package com.cnkaptan.tmonsterswiki.data.local.db

import androidx.room.TypeConverter
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterLevelEntity
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson


class LevelTypeConverter {
    @TypeConverter
    fun fromOptionValuesList(levelValues: List<MonsterLevelEntity>?): String? {
        if (levelValues == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<MonsterLevelEntity>>() {

        }.type
        return gson.toJson(levelValues, type)
    }

    @TypeConverter
    fun toOptionValuesList(levelValuesString: String?): List<MonsterLevelEntity>? {
        if (levelValuesString == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<List<MonsterLevelEntity>>() {

        }.type
        return gson.fromJson<List<MonsterLevelEntity>>(levelValuesString, type)
    }
}