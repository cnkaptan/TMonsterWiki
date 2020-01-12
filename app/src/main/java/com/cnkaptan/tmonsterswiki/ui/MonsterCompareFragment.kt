package com.cnkaptan.tmonsterswiki.ui

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
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
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.databinding.ActivityMonsterCompareBinding
import com.cnkaptan.tmonsterswiki.ui.adapter.MonsterCompareAdapter
import com.cnkaptan.tmonsterswiki.ui.viewmodel.MonsterCompareViewModel
import com.cnkaptan.tmonsterswiki.utils.Constants
import com.tiper.MaterialSpinner
import kotlinx.android.synthetic.main.activity_monster_compare.*
import javax.inject.Inject

class MonsterCompareFragment : Fragment(),RadioGroup.OnCheckedChangeListener {


    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: ActivityMonsterCompareBinding
    private lateinit var monsterCompareViewModel: MonsterCompareViewModel
    private lateinit var monsterCompareAdapter: MonsterCompareAdapter

    private var selectedLevel: Int = 0
    private var selectedRadio: Int = 0
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
        binding = DataBindingUtil.inflate(inflater,R.layout.activity_monster_compare,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        binding.radioButtonGroup.setOnCheckedChangeListener(this)
        initSpinner()
        initTextFont()
    }

    private fun initView(monsterEntity: List<MonsterEntity>) {
        binding.spnMonsterDex.onItemSelectedListener = listener
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
            rbHealth.isChecked=true
        })
    }

    private fun initSpinner() {
        val numbers = resources.getStringArray(R.array.level_numbers)
        val aa = ArrayAdapter(requireContext(), R.layout.item_spinner, numbers)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spnMonsterDex.adapter = aa
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
        monsterCompareAdapter.updateMonster(sortedListHp)
    }

    private fun sortDamageList(level: Int) {
        val sortedListDmg = sortListByAsc.sortedWith(compareBy { it.levels.get(level).dmg })
            .reversed()
        monsterCompareAdapter.updateMonster(sortedListDmg)
    }

    private fun sortSpeedList(level: Int) {
        val sortedListSpeed = sortListByAsc.sortedWith(compareBy { it.levels.get(level).speed })
            .reversed()
        monsterCompareAdapter.updateMonster(sortedListSpeed)
    }

    private fun initTextFont(){
        val typeFace = Typeface.createFromAsset(requireActivity().assets, Constants.MONSTERNAMEFONT)
        binding.spnMonsterDex.typeface=typeFace
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
                monsterCompareAdapter.updateLevel(position)

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
