package com.cnkaptan.tmonsterswiki.data.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.cnkaptan.tmonsterswiki.data.local.db.IdTypeConverter
import com.cnkaptan.tmonsterswiki.data.local.db.LevelTypeConverter
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = BaseEntity.MONSTERS_TABLE)
@TypeConverters(IdTypeConverter::class, LevelTypeConverter::class)
@Parcelize
data class MonsterEntity(
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    var monsterId: Int = 0,
    @NonNull
    var name: String = "",
    @NonNull
    var rarity: Int = 1,
    @NonNull
    var attackType: Int = 1,
    @NonNull
    var damageType: Int = 1,
    @NonNull
    @SerializedName("intro")
    var monsterIntro: String = "",
    @NonNull
    var resourceCode: String = "",
    @NonNull
    var tags: List<Int> = mutableListOf(),
    @NonNull
    var levels: List<MonsterLevelEntity> = mutableListOf()
) : BaseEntity(), Parcelable {
    @Ignore
    fun getMonsterDrawCode(): String {
        return resourceCode.toLowerCase().replace('ı', 'i')
    }
}