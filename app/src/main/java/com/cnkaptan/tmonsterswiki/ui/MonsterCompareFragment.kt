package com.cnkaptan.tmonsterswiki.ui

import android.os.Bundle
import android.widget.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.databinding.FragmentMonsterCompareBinding
import com.cnkaptan.tmonsterswiki.ui.adapter.MonsterCompareAdapter
import com.cnkaptan.tmonsterswiki.ui.viewmodel.MonsterCompareRatio
import com.cnkaptan.tmonsterswiki.ui.viewmodel.MonsterCompareViewModel
import com.cnkaptan.tmonsterswiki.utils.DataBindingFragment
import com.cnkaptan.tmonsterswiki.utils.NumberPicker
import com.cnkaptan.tmonsterswiki.utils.viewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class MonsterCompareFragment : DataBindingFragment<FragmentMonsterCompareBinding>(R.layout.fragment_monster_compare), RadioGroup.OnCheckedChangeListener {
    private val model by viewModel<MonsterCompareViewModel>(true)

    private lateinit var monsterCompareAdapter: MonsterCompareAdapter

    override val TAG: String
        get() = "MonsterCompareFragment"

    override fun onBindingCreated(binding: FragmentMonsterCompareBinding, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)
        binding.radioButtonGroup.setOnCheckedChangeListener(this)

        monsterCompareAdapter = MonsterCompareAdapter {
            startActivity(MonsterDetailActivity.newIntent(requireContext(), it.monsterId))
        }

        binding.rvMonsterDexList.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = monsterCompareAdapter
        }

        model.getMonsterWithLevels().observe(this, Observer {
            monsterCompareAdapter.updateList(it)
            binding.rvMonsterDexList.post { binding.rvMonsterDexList.scrollToPosition(0) }
        })

        binding.viewModel = model

        binding.tilSelectLevelInput.setOnClickListener {
            pickMonsterLevel()
        }

        binding.etSelectLevelInput.setOnClickListener {
            pickMonsterLevel()
        }
    }

    override fun onCheckedChanged(p0: RadioGroup?, id: Int) {
        val selectedRatio = when (id) {
            R.id.rbHealth -> {
                MonsterCompareRatio.HP
            }
            R.id.rbDamage -> {
                MonsterCompareRatio.DMG
            }
            R.id.rbSpeed -> {
                MonsterCompareRatio.SPEED
            }
            else -> {
                MonsterCompareRatio.HP
            }
        }
        model.setSelectedSortingRatio(selectedRatio)
    }

    private fun pickMonsterLevel() {
        val mBottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.AppTheme_BottomSheet_Dialog)

        val numberPicker = NumberPicker(requireContext())
        val data = (6..23).map { it.toString() }

        numberPicker.setData(data)
        numberPicker.setItemSelectedListener { position ->
            val selectedLevel = data[position].toInt()
            model.setSelectedLevel(selectedLevel)
            monsterCompareAdapter.updateLevel(selectedLevel)
            mBottomSheetDialog.dismiss()
        }

        val selectedItem = model.selectedLevel.value!!
        selectedItem.let { si ->
            val position = data.indexOf(si.toString())
            numberPicker.setSelectedItemPosition(position)
        }

        mBottomSheetDialog.setContentView(numberPicker)
        mBottomSheetDialog.show()
    }

}
