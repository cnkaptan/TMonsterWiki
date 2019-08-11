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
import com.addisonelliott.segmentedbutton.SegmentedButtonGroup.OnPositionChangedListener



class MonsterTDex : BaseActivity(), AdapterView.OnItemSelectedListener,SegmentedButtonGroup.OnPositionChangedListener {
    override val TAG: String
        get() = MonsterTDex::class.java.simpleName

    @BindView(R.id.rvMonsterDexList)
    lateinit var rvMonsterDexList: RecyclerView

    @BindView(R.id.spn_monster_dex)
    lateinit var spnLevels: Spinner

   // @BindView(R.id.rgSkill)
   // lateinit var rgSkills: RadioGroup


    @BindView(R.id.segmentedButtonGroup)
    lateinit var sbGroup: SegmentedButtonGroup

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var monsterTDexViewModel: MonsterTDexViewModel
    private lateinit var monsterTDexAdapter: MonsterTDexAdapter

    private var selectedLevel: Int = 0
    private var sortListByAsc: List<MonsterEntity> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monster_dex)
        (application as AppController).appComponent.inject(this)
        ButterKnife.bind(this)
        initViewModel()
      //  rgSkills.setOnCheckedChangeListener(this)
        sbGroup.setOnPositionChangedListener(this)

        initSpinner()
    }

    private fun initView(monsterEntity: List<MonsterEntity>) {
        spnLevels.setOnItemSelectedListener(this)
        monsterTDexAdapter = MonsterTDexAdapter(applicationContext)
        rvMonsterDexList.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = monsterTDexAdapter
            monsterTDexAdapter.updateMonster(monsterEntity)
            //For commit
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
        val aa = ArrayAdapter(this, R.layout.spinner_item, numbers)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnLevels.adapter = aa
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        monsterTDexAdapter.updateLevel(p2)
        selectedLevel = p2
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

   /* override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.rbHealth -> {
                val sortedListHp = sortListByAsc.sortedWith(compareBy { it.levels.get(selectedLevel).hp })
                monsterTDexAdapter.updateMonster(sortedListHp)
            }
            R.id.rbDamage -> {
                val sortedListDmg = sortListByAsc.sortedWith(compareBy { it.levels.get(selectedLevel).dmg })
                monsterTDexAdapter.updateMonster(sortedListDmg)
            }
            else -> {
                val sortedListMove = sortListByAsc.sortedWith(compareBy { it.levels.get(selectedLevel).speed })
                monsterTDexAdapter.updateMonster(sortedListMove)
            }
        }
    }*/


    override fun onPositionChanged(checkedId: Int) {
        when (checkedId) {
            R.id.rbHealth -> {
                val sortedListHp = sortListByAsc.sortedWith(compareBy { it.levels.get(selectedLevel).hp })
                monsterTDexAdapter.updateMonster(sortedListHp)
            }
            R.id.rbDamage -> {
                val sortedListDmg = sortListByAsc.sortedWith(compareBy { it.levels.get(selectedLevel).dmg })
                monsterTDexAdapter.updateMonster(sortedListDmg)
            }
            else -> {
                val sortedListMove = sortListByAsc.sortedWith(compareBy { it.levels.get(selectedLevel).speed })
                monsterTDexAdapter.updateMonster(sortedListMove)
            }
        }
    }


}
