package com.cnkaptan.tmonsterswiki.data.local.entity

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = BaseEntity.TAGS_TABLE)
data class TagEntity(
    @PrimaryKey
    @SerializedName("id")
    var tadId: Int,
    @NonNull
    var name: String = "",
    @NonNull
    var description: String = "",
    @NonNull
    var resourceCode: String = ""
) : BaseEntity() {
    fun getDrawResName(): String {
        return resourceCode.toLowerCase().replace('ı','i')
    }
}