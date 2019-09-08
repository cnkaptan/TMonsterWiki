package com.cnkaptan.tmonsterswiki.data.local.entity

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = BaseEntity.SKILLS_TABLE)
data class SkillEntity(
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    var skillId: Int = 0,
    @Nullable
    var name: String? = "",
    @Nullable
    var description: String? = "",
    @Nullable
    var params: String? = "",
    @Nullable
    var resourceCode: String = ""
) : BaseEntity(){
    fun getDrawResName(): String {
        return resourceCode.toLowerCase().replace('ı','i')
    }
}