package com.cnkaptan.tmonsterswiki.ui

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.databinding.FragmentUpgradeCalculatorBinding
import com.cnkaptan.tmonsterswiki.ui.viewmodel.MonsterUpgradeViewModel
import com.cnkaptan.tmonsterswiki.utils.Constants
import com.cnkaptan.tmonsterswiki.utils.DataBindingFragment
import com.cnkaptan.tmonsterswiki.utils.ValidationUtils
import com.cnkaptan.tmonsterswiki.utils.viewModel
import com.jakewharton.rxbinding2.widget.RxAdapterView
import com.jakewharton.rxbinding2.widget.RxRadioGroup
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class CalculatorFragment : DataBindingFragment<FragmentUpgradeCalculatorBinding>(R.layout.fragment_upgrade_calculator), AdapterView.OnItemSelectedListener {
    override val TAG: String
        get() = CalculatorFragment::class.java.simpleName

    private val monsterUpgradeViewModel by viewModel<MonsterUpgradeViewModel>(true)

    override fun onBindingCreated(binding: FragmentUpgradeCalculatorBinding, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)
        binding.spnFromMonster.onItemSelectedListener = this
        binding.spnToMonster.onItemSelectedListener = this


        binding.btnCalculate.setOnClickListener {
            calculate()
        }

        disposableContainer.add(calculateButtonStatus().subscribe {
            binding.btnCalculate.visibility = if (it) {
                View.VISIBLE
            } else {
                View.GONE
            }

            if (!it) {
                binding.rlContainerCalculatorResult.visibility = View.INVISIBLE
            }
        })
        initTextFont()
        initSpinners()
    }

    private fun calculate() {
        calculateUpgradeCost()
    }

    private fun initSpinners() {
        val numbers = resources.getStringArray(R.array.calculator_levels)
        val aa = ArrayAdapter(requireContext(), R.layout.item_spinner, numbers)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnFromMonster.adapter = aa
        binding.spnToMonster.adapter = aa
    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}

    private fun calculateButtonStatus(): Observable<Boolean> {
        val rarity = RxRadioGroup.checkedChanges(binding.rgCards)
            .map { it > 0 }

        val obsFromLevel = RxAdapterView.itemSelections(binding.spnFromMonster)
        val obsToLevel = RxAdapterView.itemSelections(binding.spnToMonster)

        val levelsObs = Observable.combineLatest(
            obsToLevel,
            obsFromLevel,
            object : BiFunction<Int, Int, Boolean> {
                override fun apply(t1: Int, t2: Int): Boolean {
                    if (t1 > 0 && t2 > 0) {
                        return t1 > t2
                    }
                    return false
                }
            })

        return ValidationUtils.and(rarity, levelsObs)
    }

    private fun calculateUpgradeCost() {
        val rarity = when (binding.rgCards.checkedRadioButtonId) {
            R.id.rbCommonCards -> 1
            R.id.rbEpicCards -> 2
            R.id.rbMonstrousCards -> 3
            R.id.rbLegendaryCards -> 4
            else -> -1
        }

        val frLevelPosition = binding.spnFromMonster.selectedItemPosition
        val toLevelPosition = binding.spnToMonster.selectedItemPosition
        val monsterUpgradeDatas = monsterUpgradeViewModel.loadMonster(rarity)
        val levelsArray = resources.getStringArray(R.array.calculator_levels)
        val fromLevel = levelsArray[frLevelPosition].toInt()
        val toLevel = levelsArray[toLevelPosition].toInt()
        var totalCard = 0
        var totalGold = 0
        var totalExp = 0
        var totalCp = 0

        val fromArrayPosition = frLevelPosition - 1
        val toArrayPosition = toLevelPosition - 1
//        Log.e(TAG,"From Level = $fromLevel, To Level = $toLevel")
//        Log.e(TAG,"From Level Array Position= $fromArrayPosition, To Level Position = $toArrayPosition")
//        for (i in fromArrayPosition until toArrayPosition) {
//            totalCard += monsterUpgradeDatas.cardList[i]
//            totalGold += monsterUpgradeDatas.goldList[i]
//            totalExp += monsterUpgradeDatas.expList[i]
//            totalCp += monsterUpgradeDatas.cpList[i] * monsterUpgradeDatas.cardList[i]
//        }

        var numCardsHave = binding.etTotalCards.text.toString().toIntOrNull() ?: 0
        var numCpHave = binding.etTotalCp.text.toString().toIntOrNull() ?: 0

        Log.e(
            TAG,
            "From Array Position = ${fromArrayPosition}, toArrayPosition = ${toArrayPosition}"
        )
        for (i in fromArrayPosition until toArrayPosition) {

            var levelCards = monsterUpgradeDatas.cardList[i]
            val valueCpPerCard = monsterUpgradeDatas.cpList[i]
            if (numCardsHave > levelCards) {
                numCardsHave -= levelCards
                continue
            } else {
                if (numCardsHave > 0) {
                    levelCards -= numCardsHave
                }
            }

            val necCptoNextLevel = levelCards * valueCpPerCard
            if (numCpHave > necCptoNextLevel) {
                numCpHave -= necCptoNextLevel
                continue
            } else {
                if (numCpHave >= valueCpPerCard) {
                    val value = numCpHave / valueCpPerCard
                    levelCards -= value
                }
            }

            totalCard += levelCards
            totalCp += levelCards * valueCpPerCard
        }

        Log.e(TAG, "Total Nec Cards $totalCard")

        for (i in fromArrayPosition until toArrayPosition) {
            totalGold += monsterUpgradeDatas.goldList[i]
            totalExp += monsterUpgradeDatas.expList[i]
        }


        binding.tvValueGold.text = totalGold.toString()
        binding.tvValueCard.text = totalCard.toString()
        binding.tvValueCP.text = totalCp.toString()
        binding.rlContainerCalculatorResult.visibility = View.VISIBLE
    }

    private fun initTextFont(){
        val typeFace = Typeface.createFromAsset(requireActivity().assets, Constants.MONSTER_NAME_FONT)

        binding.tvFromLevelText.typeface=typeFace
        binding.tvToLevelText.typeface=typeFace
        binding.tvTitleCurrentTotalCards.typeface=typeFace
        binding.tvTitleCurrentTotalCp.typeface=typeFace

    }
}