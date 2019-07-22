package com.cnkaptan.tmonsterswiki.remote.model

import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.SkillEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.TagEntity

data class MonsterMainResponse(
    val monsters: List<MonsterEntity>,
    val tags: List<TagEntity>,
    val skills: List<SkillEntity>)
