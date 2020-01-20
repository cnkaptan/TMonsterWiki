package com.cnkaptan.tmonsterswiki.ui

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.databinding.FragmentUpgradeCalculatorBinding
import com.cnkaptan.tmonsterswiki.ui.viewmodel.MonsterUpgradeViewModel
import com.cnkaptan.tmonsterswiki.utils.Constants
import com.cnkaptan.tmonsterswiki.utils.DataBindingFragment
import com.cnkaptan.tmonsterswiki.utils.NumberPicker
import com.cnkaptan.tmonsterswiki.utils.ValidationUtils
import com.cnkaptan.tmonsterswiki.utils.viewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jakewharton.rxbinding2.widget.RxRadioGroup
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class CalculatorFragment :
    DataBindingFragment<FragmentUpgradeCalculatorBinding>(R.layout.fragment_upgrade_calculator) {
    override val TAG: String
        get() = CalculatorFragment::class.java.simpleName

    private val monsterUpgradeViewModel by viewModel<MonsterUpgradeViewModel>(true)


    private var selectedFromLevel: Int = 1

    private var selectedToLevel: Int = 0

    override fun onBindingCreated(
        binding: FragmentUpgradeCalculatorBinding,
        savedInstanceState: Bundle?
    ) {
        super.onBindingCreated(binding, savedInstanceState)

        binding.btnCalculate.setOnClickListener {
            calculate()
        }

        binding.etSelectFromLevelInput.setOnClickListener {
            pickMonsterFromLevel()
        }

        binding.flSelectFromLevelInput.setOnClickListener {
            pickMonsterFromLevel()
        }

        binding.etSelectToLevelInput.setOnClickListener {
            pickMonsterToLevel()
        }

        binding.flSelectToLevelInput.setOnClickListener {
            pickMonsterToLevel()
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
    }

    private fun calculate() {
        calculateUpgradeCost()
    }

    private fun calculateButtonStatus(): Observable<Boolean> {

        val rarity = RxRadioGroup.checkedChanges(binding.rgCards)
            .map { it > 0 }


        val obsFromLevel = RxTextView.textChanges(binding.etSelectFromLevelInput)
            .skipInitialValue()
            .map { it.toString().toInt() }
            .doOnNext { Log.e("ObsFromLevel", it.toString()) }

        val obsToLevel = RxTextView.textChanges(binding.etSelectToLevelInput)
            .skipInitialValue()
            .map { it.toString().toInt() }
            .doOnNext { Log.e("ObsFromLevel", it.toString()) }

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


        val frLevelPosition = selectedFromLevel
        val toLevelPosition = selectedToLevel
        val monsterUpgradeDatas = monsterUpgradeViewModel.loadMonster(rarity)


        var totalCard = 0
        var totalGold = 0
        var totalExp = 0
        var totalCp = 0

        val fromArrayPosition = frLevelPosition - 1
        val toArrayPosition = toLevelPosition - 1


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

    private fun pickMonsterFromLevel() {
        val mBottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.AppTheme_BottomSheet_Dialog)

        val numberPicker = NumberPicker(requireContext())
        val data = (9..23).map { it.toString() }

        numberPicker.setData(data)
        numberPicker.setItemSelectedListener { position ->
            val selectedPosition = position
            val selectedLevel = data[position].toInt()
            selectedFromLevel = selectedPosition + 1
            binding.etSelectFromLevelInput.setText(selectedLevel.toString())
            Log.e("From Selected Level", selectedPosition.toString())
            mBottomSheetDialog.dismiss()
        }

        mBottomSheetDialog.setContentView(numberPicker)
        mBottomSheetDialog.show()
    }

    private fun pickMonsterToLevel() {
        val mBottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.AppTheme_BottomSheet_Dialog)

        val numberPicker = NumberPicker(requireContext())
        val data = (9..23).map { it.toString() }

        numberPicker.setData(data)
        numberPicker.setItemSelectedListener { position ->
            val selectedPosition = position
            val selectedLevel = data[position].toInt()
            selectedToLevel = selectedPosition + 1
            binding.etSelectToLevelInput.setText(selectedLevel.toString())
            Log.e("To Selected Level", selectedPosition.toString())
            mBottomSheetDialog.dismiss()
        }

        mBottomSheetDialog.setContentView(numberPicker)
        mBottomSheetDialog.show()
    }

    private fun initTextFont() {
        val typeFace =
            Typeface.createFromAsset(requireActivity().assets, Constants.MONSTER_NAME_FONT)

        binding.tvFromLevelText.typeface = typeFace
        binding.tvToLevelText.typeface = typeFace
        binding.tvTitleCurrentTotalCards.typeface = typeFace
        binding.tvTitleCurrentTotalCp.typeface = typeFace

    }
}