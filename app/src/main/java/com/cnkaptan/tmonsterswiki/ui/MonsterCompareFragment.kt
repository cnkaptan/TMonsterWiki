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
import com.addisonelliott.segmentedbutton.SegmentedButtonGroup
import com.cnkaptan.tmonsterswiki.AppController
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.databinding.ActivityMonsterDexBinding
import com.cnkaptan.tmonsterswiki.ui.adapter.MonsterCompareAdapter
import com.cnkaptan.tmonsterswiki.ui.viewmodel.MonsterCompareViewModel
import javax.inject.Inject



class MonsterCompareFragment : Fragment(), AdapterView.OnItemSelectedListener,SegmentedButtonGroup.OnPositionChangedListener {


    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: ActivityMonsterDexBinding
    private lateinit var monsterCompareViewModel: MonsterCompareViewModel
    private lateinit var monsterCompareAdapter: MonsterCompareAdapter

    private var selectedLevel: Int = 0
    private var sortListByAsc: List<MonsterEntity> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as AppController).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.activity_monster_dex,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initSpinner()

        binding.segmentedButtonGroup.onPositionChangedListener = this
    }

    private fun initView(monsterEntity: List<MonsterEntity>) {
        binding.spnMonsterDex.onItemSelectedListener = this
        monsterCompareAdapter = MonsterCompareAdapter(requireContext())
        binding.rvMonsterDexList.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = monsterCompareAdapter
            monsterCompareAdapter.updateMonster(monsterEntity)
        }
    }

    private fun initViewModel() {
        monsterCompareViewModel = ViewModelProviders.of(this, viewModelFactory).get(MonsterCompareViewModel::class.java)
        monsterCompareViewModel.loadMonstersWithLevels()
        monsterCompareViewModel.getMonsterWithLevels().observe(this, Observer {
            initView(it)
            sortListByAsc = it
        })
    }

    private fun initSpinner() {
        val numbers = resources.getStringArray(R.array.level_numbers)
        val aa = ArrayAdapter(requireContext(), R.layout.item_spinner, numbers)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnMonsterDex.adapter = aa
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if(p1!!.equals("Select Level")){
            monsterCompareAdapter.updateLevel(0)
            selectedLevel=0
        }else{
            monsterCompareAdapter.updateLevel(p2)
            selectedLevel = p2
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onPositionChanged(checkedId: Int) {
        when (checkedId) {
            0 -> {
                val sortedListHp = sortListByAsc.sortedWith(compareBy { it.levels.get(selectedLevel).hp })
                monsterCompareAdapter.updateMonster(sortedListHp)
            }
            1 -> {
                val sortedListDmg = sortListByAsc.sortedWith(compareBy { it.levels.get(selectedLevel).dmg })
                monsterCompareAdapter.updateMonster(sortedListDmg)
            }
            else -> {
                val sortedListMove = sortListByAsc.sortedWith(compareBy { it.levels.get(selectedLevel).speed })
                monsterCompareAdapter.updateMonster(sortedListMove)
            }
        }
    }

}
