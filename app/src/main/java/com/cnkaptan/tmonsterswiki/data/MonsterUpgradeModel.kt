package com.cnkaptan.tmonsterswiki.data

import io.reactivex.Single
 data class MonsterUpgradeModel (
    var cardList: List<Int>?,
    var goldList: List<Int>?,
    var explist: List<Int>?,
    var cpList: List<Int>?
){
        constructor() : this(null,null,null,null)

      fun singleMonsterUpgrade( cardList: List<Int>,goldList: List<Int>, explist: List<Int>, cpList: List<Int>):MonsterUpgradeModel{
            return MonsterUpgradeModel(cardList,goldList,explist,cpList)
      }

}