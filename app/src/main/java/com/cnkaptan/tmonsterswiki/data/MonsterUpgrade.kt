package com.cnkaptan.tmonsterswiki.data

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.*
import java.util.*
import java.util.Arrays.asList
import kotlin.collections.ArrayList


object MonsterUpgrade {

    var commonCardlist: List<Int> = listOf( 280, 360, 460, 560, 700, 1000, 1300, 1600, 2000, 2500, 3000, 5000, 8000, 20000)
    var epicCardlist: List<Int> = listOf(70, 90, 120, 140, 180, 250, 350, 400, 500, 600, 800, 1300, 2100, 5200)
    var monstrousCardlist: List<Int> = listOf(10, 14, 20, 25, 30, 40, 50, 60, 80, 100, 120, 200, 320, 800)
    var legendaryCardlist: List<Int> = listOf(2, 4, 6, 8, 10, 12, 14, 16, 20, 22, 25, 45, 80, 200)

    var commonGoldlist: List<Int> = listOf(1600, 2500, 3600, 5000, 7000, 10000, 15000, 22000, 32000, 45000, 60000, 80000, 100000, 200000)
    var epicGoldlist: List<Int> = listOf(1600, 2500, 3600, 5000, 7000, 10000, 15000, 22000, 32000, 45000, 60000, 80000, 100000, 200000)
    var monstrousGoldlist: List<Int> = listOf(1600, 2500, 3600, 5000, 7000, 10000, 15000, 22000, 32000, 45000, 60000, 80000,100000,200000)
    var legendaryGoldlist: List<Int> = listOf(1600, 2500, 3600, 5000, 7000, 10000, 15000, 22000, 32000, 45000, 60000, 80000, 100000, 200000)

    var commonExplist: List<Int> = listOf(140, 180, 230, 280, 350, 500, 650, 800, 1000, 1250, 1500, 1800, 2050, 4000)
    var epicExplist: List<Int> = listOf(140, 180, 230, 280, 350, 500, 650, 800, 1000, 1250, 1500, 1800, 2050, 4000)
    var monstrousExplist: List<Int> = listOf(140, 180, 230, 280, 350, 500, 650, 800, 1000, 1250, 1500, 1800, 2050, 4000)
    var legendaryExplist: List<Int> = listOf(140, 180, 230, 280, 350, 500, 650, 800, 1000, 1250, 1500, 1800, 2050, 4000)

    var commonCplist: List<Int> = listOf(2, 2, 2, 2, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4)
    var epicCplist: List<Int> = listOf(20, 20, 20, 20, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40, 40)
    var monstrousCplist: List<Int> = listOf(200, 200, 200, 400, 400, 400, 400, 400, 400, 400, 400, 400, 400, 400, 400)
    var legendaryCplist: List<Int> = listOf(1200, 1200, 1600, 1600, 1600, 1600, 1600, 1600, 1600, 1600, 1600, 1600, 1600, 1600, 1600)


    open class CalculationDataGroup(val cardList: List<Int>, val goldList: List<Int>, val expList: List<Int>, val cpList: List<Int>)
    object CommonUpgradeDatas : CalculationDataGroup(commonCardlist, commonGoldlist, commonExplist, commonCplist)
    object EpicUpgradeDatas : CalculationDataGroup(epicCardlist, epicGoldlist, epicExplist, epicCplist)
    object MonstrousUpgradeDatas : CalculationDataGroup(monstrousCardlist, monstrousGoldlist, monstrousExplist, monstrousCplist)
    object LegendaryUpgradeDatas : CalculationDataGroup(legendaryCardlist, legendaryGoldlist, legendaryExplist, legendaryCplist)
}

