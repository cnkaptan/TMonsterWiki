package com.cnkaptan.tmonsterswiki.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.cnkaptan.tmonsterswiki.AppController
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.databinding.ActivityMonsterCompareBinding
import com.cnkaptan.tmonsterswiki.ui.adapter.MonsterCompareAdapter
import com.cnkaptan.tmonsterswiki.ui.viewmodel.MonsterCompareRatio
import com.cnkaptan.tmonsterswiki.ui.viewmodel.MonsterCompareViewModel
import com.cnkaptan.tmonsterswiki.utils.NumberPicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject

class MonsterCompareFragment : Fragment(), RadioGroup.OnCheckedChangeListener {


    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: ActivityMonsterCompareBinding
    private lateinit var model: MonsterCompareViewModel
    private lateinit var monsterCompareAdapter: MonsterCompareAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as AppController).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.activity_monster_compare, container, false)
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        binding.radioButtonGroup.setOnCheckedChangeListener(this)
    }


    private fun init() {
        monsterCompareAdapter = MonsterCompareAdapter()
        binding.rvMonsterDexList.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = monsterCompareAdapter
        }


        model = ViewModelProviders.of(this, viewModelFactory).get(MonsterCompareViewModel::class.java)
        model.getMonsterWithLevels().observe(this, Observer {
            monsterCompareAdapter.submitList(it)
            binding.rvMonsterDexList.post { binding.rvMonsterDexList.scrollToPosition(0) }
        })

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
