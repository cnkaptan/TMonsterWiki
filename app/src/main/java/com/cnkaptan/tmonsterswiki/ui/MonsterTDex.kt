package com.cnkaptan.tmonsterswiki.ui

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.addisonelliott.segmentedbutton.SegmentedButtonGroup
import com.cnkaptan.tmonsterswiki.AppController
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.ui.adapter.MonsterTDexAdapter
import com.cnkaptan.tmonsterswiki.ui.base.BaseActivity
import com.cnkaptan.tmonsterswiki.ui.viewmodel.MonsterTDexViewModel
import javax.inject.Inject


class MonsterTDex : BaseActivity(), AdapterView.OnItemSelectedListener, SegmentedButtonGroup.OnPositionChangedListener {
    override val TAG: String
        get() = MonsterTDex::class.java.simpleName

    @BindView(R.id.rvMonsterDexList)
    lateinit var rvMonsterDexList: RecyclerView

    @BindView(R.id.spn_monster_dex)
    lateinit var spnLevels: Spinner

    @BindView(R.id.segmentedButtonGroup)
    lateinit var sbGroup: SegmentedButtonGroup

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var monsterTDexViewModel: MonsterTDexViewModel
    private lateinit var monsterTDexAdapter: MonsterTDexAdapter

    private var selectedLevel: Int = 0
    private var selectedSegment: Int = 0
    private var sortListByAsc: List<MonsterEntity> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monster_dex)
        (application as AppController).appComponent.inject(this)
        ButterKnife.bind(this)
        initViewModel()

        sbGroup.setOnPositionChangedListener(this)

        initSpinner()
    }

    private fun initView(monsterEntity: List<MonsterEntity>) {
        spnLevels.setOnItemSelectedListener(this)
        monsterTDexAdapter = MonsterTDexAdapter(applicationContext)
        sbGroup.setPosition(0, true)
        rvMonsterDexList.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = monsterTDexAdapter
            monsterTDexAdapter.updateMonster(monsterEntity)
        }
    }

    private fun initViewModel() {
        monsterTDexViewModel = ViewModelProviders.of(this, viewModelFactory).get(MonsterTDexViewModel::class.java)
        monsterTDexViewModel.loadMonstersWithLevels()
        monsterTDexViewModel.getMonsterWithLevels().observe(this, Observer {
            initView(it)
            sortListByAsc = it
        })
    }

    private fun initSpinner() {
        val numbers = resources.getStringArray(R.array.level_numbers)
        val aa = ArrayAdapter(this, R.layout.item_spinner, numbers)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnLevels.adapter = aa
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (p1!!.equals("Select Level")) {
            monsterTDexAdapter.updateLevel(0)
            selectedLevel = 0
        } else {
            monsterTDexAdapter.updateLevel(p2)

            when (selectedSegment) {
                0 -> {
                    sortHealthList(p2)
                }
                1 -> {
                    sortDamageList(p2)
                }
                2 -> {
                    sortSpeedList(p2)
                }
            }
            selectedLevel = p2
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

    override fun onPositionChanged(checkedId: Int) {
        when (checkedId) {
            0 -> {
                selectedSegment = 0
                sortHealthList(selectedLevel)
            }
            1 -> {
                selectedSegment = 1
                sortDamageList(selectedLevel)
            }
            else -> {
                selectedSegment = 2
                sortSpeedList(selectedLevel)
            }
        }
    }

    private fun sortHealthList(level: Int) {
        val sortedListHp = sortListByAsc.sortedWith(compareBy { it.levels.get(level).hp })
            .reversed()
        monsterTDexAdapter.updateMonster(sortedListHp)
    }

    private fun sortDamageList(level: Int) {
        val sortedListDmg = sortListByAsc.sortedWith(compareBy { it.levels.get(level).dmg })
            .reversed()
        monsterTDexAdapter.updateMonster(sortedListDmg)
    }

    private fun sortSpeedList(level: Int) {
        val sortedListSpeed = sortListByAsc.sortedWith(compareBy { it.levels.get(level).speed })
            .reversed()
        monsterTDexAdapter.updateMonster(sortedListSpeed)
    }
}
