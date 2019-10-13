package com.cnkaptan.tmonsterswiki.ui

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
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
import com.cnkaptan.tmonsterswiki.utils.Constants
import com.tiper.MaterialSpinner
import javax.inject.Inject


class MonsterTDex : BaseActivity(),
    RadioGroup.OnCheckedChangeListener {
    override val TAG: String
        get() = MonsterTDex::class.java.simpleName

    @BindView(R.id.rvMonsterDexList)
    lateinit var rvMonsterDexList: RecyclerView

    @BindView(R.id.spn_monster_dex)
    lateinit var spnLevels: MaterialSpinner

    @BindView(R.id.radioButtonGroup)
    lateinit var rbGroup: RadioGroup

    @BindView(R.id.rbHealth)
    lateinit var rbHealth: RadioButton


    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var monsterTDexViewModel: MonsterTDexViewModel
    private lateinit var monsterTDexAdapter: MonsterTDexAdapter

    private var selectedLevel: Int = 0
    private var selectedRadio: Int = 0
    private var sortListByAsc: List<MonsterEntity> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monster_dex)
        (application as AppController).appComponent.inject(this)
        ButterKnife.bind(this)

        initViewModel()

        rbGroup.setOnCheckedChangeListener(this)

        initSpinner()


    }

    private fun initView(monsterEntity: List<MonsterEntity>) {
        spnLevels.onItemSelectedListener = listener
        monsterTDexAdapter = MonsterTDexAdapter(applicationContext)

        rvMonsterDexList.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = monsterTDexAdapter
            monsterTDexAdapter.updateMonster(monsterEntity)
        }

        val typeFace = Typeface.createFromAsset(assets, Constants.MONSTERDEXNAMEFONT)
        spnLevels.typeface=typeFace


    }

    private fun initViewModel() {
        monsterTDexViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(MonsterTDexViewModel::class.java)
        monsterTDexViewModel.loadMonstersWithLevels()
        monsterTDexViewModel.getMonsterWithLevels().observe(this, Observer {
            initView(it)
            sortListByAsc = it
            rbHealth.isChecked=true
        })
    }

    private fun initSpinner() {
        val numbers = resources.getStringArray(R.array.level_numbers)
        val aa = ArrayAdapter(this, R.layout.item_spinner, numbers)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnLevels.adapter = aa
    }

    override fun onCheckedChanged(p0: RadioGroup?, id: Int) {
        when (id) {
            R.id.rbHealth -> {
                selectedRadio = 0
                sortHealthList(selectedLevel)
            }
            R.id.rbDamage -> {
                selectedRadio = 1
                sortDamageList(selectedLevel)
            }
            R.id.rbSpeed ->{
                selectedRadio = 2
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

    private val listener by lazy {
        object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(
                parent: MaterialSpinner,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.e("MaterialSpinner", "onItemSelected parent=${parent.id}, position=$position")
                monsterTDexAdapter.updateLevel(position)

                when (selectedRadio) {
                    0 -> {
                        sortHealthList(position)
                    }
                    1 -> {
                        sortDamageList(position)
                    }
                    2 -> {
                        sortSpeedList(position)
                    }
                }
                selectedLevel = position
                parent.focusSearch(View.FOCUS_UP)?.requestFocus()
            }

            override fun onNothingSelected(parent: MaterialSpinner) {
            }
        }
    }
}
