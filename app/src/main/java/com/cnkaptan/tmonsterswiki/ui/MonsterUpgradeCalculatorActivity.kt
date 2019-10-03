package com.cnkaptan.tmonsterswiki.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.cnkaptan.tmonsterswiki.AppController
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.MonsterUpgradeModel
import com.cnkaptan.tmonsterswiki.ui.base.BaseActivity
import com.cnkaptan.tmonsterswiki.ui.viewmodel.MonsterUpgradeViewModel
import javax.inject.Inject

class MonsterUpgradeCalculatorActivity : BaseActivity(), AdapterView.OnItemSelectedListener {
    override val TAG: String
        get() = MonsterUpgradeCalculatorActivity::class.java.simpleName
    @BindView(R.id.spnFromMonster)
    lateinit var spnFromMonster: Spinner

    @BindView(R.id.spnToMonster)
    lateinit var spnToMonster: Spinner

    @BindView(R.id.spnRarity)
    lateinit var spnRarity: Spinner

    @BindView(R.id.btnCalculate)
    lateinit var btnCalculate: Button

    @BindView(R.id.rvFirst)
    lateinit var rvFirst: RelativeLayout

    @BindView(R.id.rvSecond)
    lateinit var rvSecond: RelativeLayout

    @BindView(R.id.rvInfoFirst)
    lateinit var rvFirstInfo: RelativeLayout

    @BindView(R.id.rvInfoSecond)
    lateinit var rvSecondInfo: RelativeLayout

    @BindView(R.id.txtRarity)
    lateinit var tvRarity: TextView

    @BindView(R.id.txtFromLevel)
    lateinit var tvFromLevel: TextView

    @BindView(R.id.txtToLevel)
    lateinit var tvToLevel: TextView

    @BindView(R.id.txtCardNumber)
    lateinit var tvCardNumber: TextView

    @BindView(R.id.txtExpNumber)
    lateinit var tvExpNumber: TextView

    @BindView(R.id.txtGoldNumber)
    lateinit var tvGoldNumber: TextView


    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var monsterUpgradeViewModel: MonsterUpgradeViewModel

    lateinit var upgradeInfoLists: MonsterUpgradeModel
    private var fromNumber: Int = 1
    private var toNumber: Int = 1

    private var totalCard: Int = 0
    private var totalGold: Int = 0
    private var totalExp: Int = 0
    private var totalCp: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upgrade_calculator)
        (application as AppController).appComponent.inject(this)
        ButterKnife.bind(this)
        initViewModel(0)
        initRaritySpinner()
        spnRarity.setOnItemSelectedListener(this)
        spnFromMonster.setOnItemSelectedListener(this)
        spnToMonster.setOnItemSelectedListener(this)

    }

    @OnClick(R.id.btnCalculate)
    fun calculate() {
        calculateUpgradeCost()
        rvFirstInfo.visibility=View.VISIBLE
        rvSecondInfo.visibility=View.VISIBLE
    }

    private fun initViewModel(rarity: Int) {
        monsterUpgradeViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(MonsterUpgradeViewModel::class.java)
    }

    private fun initSpinners(rarity: Int) {

        when (rarity) {
            1 -> {
                val numbers = resources.getStringArray(R.array.common_numbers)
                val aa = ArrayAdapter(this, R.layout.item_spinner, numbers)
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spnFromMonster.adapter = aa
                spnToMonster.adapter = aa
                tvRarity.text="Common"
            }
            2 -> {
                val numbers = resources.getStringArray(R.array.epic_numbers)
                val aa = ArrayAdapter(this, R.layout.item_spinner, numbers)
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spnFromMonster.adapter = aa
                spnToMonster.adapter = aa
                tvRarity.text="Epic"
            }
            3 -> {
                val numbers = resources.getStringArray(R.array.monst_numbers)
                val aa = ArrayAdapter(this, R.layout.item_spinner, numbers)
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spnFromMonster.adapter = aa
                spnToMonster.adapter = aa
                tvRarity.text="Monstrous"
            }
            4 -> {
                val numbers = resources.getStringArray(R.array.legendary_numbers)
                val aa = ArrayAdapter(this, R.layout.item_spinner, numbers)
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spnFromMonster.adapter = aa
                spnToMonster.adapter = aa
                tvRarity.text="Legendary"
            }
        }

    }

    private fun initRaritySpinner() {
        val numbers = resources.getStringArray(R.array.rarity_numbers)
        val aa = ArrayAdapter(this, R.layout.item_spinner, numbers)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnRarity.adapter = aa
    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        when (parent!!.id) {

            R.id.spnRarity -> {
                if (p2 == 0) {
                    Toast.makeText(applicationContext, "Please Select Rarity", Toast.LENGTH_LONG)
                        .show()
                    rvFirst.visibility = View.INVISIBLE

                }
                else {
                    monsterUpgradeViewModel.loadMonster(p2)
                    initSpinners(p2)
                    monsterUpgradeViewModel.getUpgradeInfo().observe(this, Observer {
                        Log.e(TAG, it.toString())
                        upgradeInfoLists = it
                        rvFirst.visibility = View.VISIBLE
                    })
                }
            }
            R.id.spnFromMonster -> {
                fromNumber = p2 - 1
                rvSecond.visibility=View.VISIBLE
                Log.e("FIRST FROM NUMBER", fromNumber.toString())
            }
            R.id.spnToMonster -> {
                toNumber = p2 - 1
                btnCalculate.visibility=View.VISIBLE
                Log.e("FIRST TO NUMBER", toNumber.toString())
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun calculateUpgradeCost() {
        totalCard = 0
        totalGold = 0
        totalExp = 0
        totalCp = 0

        for (i in fromNumber + 1 until toNumber + 1) {
            totalCard = totalCard.plus(upgradeInfoLists.cardList!!?.get(i))
            totalGold = totalGold.plus(upgradeInfoLists.goldList!!?.get(i))
            totalExp = totalExp.plus(upgradeInfoLists.explist!!?.get(i))
            totalCp = totalCp.plus(upgradeInfoLists.cpList!!?.get(i))
        }

        tvFromLevel.text=fromNumber.plus(1).toString()
        tvToLevel.text=toNumber.plus(1).toString()
        tvCardNumber.text=totalCard.toString()
        tvExpNumber.text=totalExp.toString()
        tvGoldNumber.text=totalGold.toString()
    }
}