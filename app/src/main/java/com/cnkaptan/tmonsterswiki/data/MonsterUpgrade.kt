package com.cnkaptan.tmonsterswiki.data

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.*
import java.util.*
import java.util.Arrays.asList
import kotlin.collections.ArrayList


class MonsterUpgrade {

   var commonCardlist: List<Int> = ArrayList(asList(0, 2, 4, 10, 20, 40, 80, 140,200, 280,360,460,560,700,1000,1300,1600,2000,2500,3000,5000,8000,20000))
    var epicCardlist: List<Int> = ArrayList(asList(0, 2, 4, 10, 20, 40, 50, 70,90,120,140,180,250,350,400,500,600,800,1300,2100,5200))
    var monstrousCardlist: List<Int> = ArrayList(asList(0, 2, 4, 10 , 14 , 20, 25, 30,40,50,60,80,100,120,200,320,800))
    var legendaryCardlist: List<Int> = ArrayList(asList(0, 2, 4, 6,8,10 ,12, 14 , 16, 20, 22,25,45,80,200))

    var commonGoldlist: List<Int> = ArrayList(asList(0, 5, 10, 30, 70, 150, 350, 700,1000, 1600,2500,3600,5000,7000,10000,15000,22000,32000,45000,60000,80000,100000,200000))
    var epicGoldlist: List<Int> = ArrayList(asList(0, 30, 70, 150, 350, 700, 1000, 1600,2500,3600,5000,7000,10000,15000,22000,32000,45000,60000,80000,100000,200000))
    var monstrousGoldlist: List<Int> = ArrayList(asList(0,350, 700, 1000, 1600,2500,3600,5000,7000,10000,15000,22000,32000,45000,60000,80000,100000,200000))
    var legendaryGoldlist: List<Int> = ArrayList(asList(0, 1600,2500,3600,5000,7000,10000,15000,22000,32000,45000,60000,80000,100000,200000))

    var commonExplist: List<Int> = ArrayList(asList(0, 4, 6, 8, 10, 20, 40, 70,100, 140,180,230,280,350,500,650,800,1000,1250,1500,1800,2050,4000))
    var epicExplist: List<Int> = ArrayList(asList(0, 6, 10, 20, 40, 70, 100, 140,180,230,280,350,500,650,800,1000,1250,1500,1800,2050,4000))
    var monstrousExplist: List<Int> = ArrayList(asList(0,20, 50, 100, 140,180,230,280,350,500,650,800,1000,1250,1500,1800,2050,4000))
    var legendaryExplist: List<Int> = ArrayList(asList(0,100, 140,180,230,280,350,500,650,800,1000,1250,1500,1800,2050,4000))

    var commonCplist: List<Int> = ArrayList(asList(2, 2, 2,2, 2 , 2, 2, 2,2, 2,2,4,4,4,4,4,4,4,4,4,4,4))
    var epicCplist: List<Int> = ArrayList(asList(0,20, 20, 20,20, 20 , 20, 20,40,40,40,40,40,40,40,40,40,40,40))
    var monstrousCplist: List<Int> = ArrayList(asList(0,200, 200, 200,200, 200,400,400,400,400,400,400,400,400,400,400,400,400))
    var legendaryCplist: List<Int> = ArrayList(asList(0,1200, 1200, 1600,1600, 1600 , 1600, 1600,1600,1600,1600,1600,1600,1600,1600))

}