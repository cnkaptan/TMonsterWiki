package com.cnkaptan.tmonsterswiki.ui

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
import butterknife.OnClick
import com.cnkaptan.tmonsterswiki.AppController
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.di.module.ApiModule
import com.cnkaptan.tmonsterswiki.ui.adapter.MonsterFirstLevelMatchAdapter
import com.cnkaptan.tmonsterswiki.ui.adapter.MonsterSecondLevelMatchAdapter
import com.cnkaptan.tmonsterswiki.ui.base.BaseActivity
import com.cnkaptan.tmonsterswiki.ui.dialog.MonsterDialogFirst
import com.cnkaptan.tmonsterswiki.ui.dialog.MonsterDialogSecond
import com.cnkaptan.tmonsterswiki.ui.viewmodel.MonsterMatchViewModel
import com.squareup.picasso.Picasso
import javax.inject.Inject

class MonsterMatchActivity : BaseActivity(), MonsterDialogFirst.OnFragmentInteractionListener,
    AdapterView.OnItemSelectedListener,
    MonsterDialogSecond.OnFragmentInteractionListener {
    override val TAG: String
        get() = MonsterMatchActivity::class.java.simpleName

    @BindView(R.id.ivMonsterFirst)
    lateinit var ivMonsterFirst: ImageView

    @BindView(R.id.ivMonsterSecond)
    lateinit var ivMonsterSecond: ImageView

    @BindView(R.id.tvMonsterNameFirst)
    lateinit var tvMonsterNameFirst: TextView

    @BindView(R.id.tvMonsterNameSecond)
    lateinit var tvMonsterNameSecond: TextView

    @BindView(R.id.rvLevelsFirst)
    lateinit var rvLevelsFirst: RecyclerView

    @BindView(R.id.rvLevelsSecond)
    lateinit var rvLevelsSecond: RecyclerView

    @BindView(R.id.spnMonsterFirst)
    lateinit var spnLevelsFirst: Spinner

    @BindView(R.id.spnMonsterSecond)
    lateinit var spnLevelsSecond: Spinner

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var monsterMatchViewModel: MonsterMatchViewModel
    private lateinit var monsterDialogFirst: MonsterDialogFirst
    private lateinit var monsterDialogSecond: MonsterDialogSecond
    private lateinit var monsterFirstLevelMatchAdapter: MonsterFirstLevelMatchAdapter
    private lateinit var monsterSecondLevelMatchAdapter: MonsterSecondLevelMatchAdapter

    private var selectedLevelFirst: Int = 0
    private var selectedLevelSecond: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monster_match)
        (application as AppController).appComponent.inject(this)
        ButterKnife.bind(this)
        initViewModel()
        initFirstSpinner()
        initSecondSpinner()
    }

    private fun initViewModel() {
        monsterMatchViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(MonsterMatchViewModel::class.java)
        monsterMatchViewModel.loadMonster()
        monsterMatchViewModel.getMonsterList().observe(this, Observer {
            monsterDialogFirst = MonsterDialogFirst.newInstance(it!!)
            monsterDialogSecond = MonsterDialogSecond.newInstance(it!!)
        })
    }

    @OnClick(R.id.ivMonsterFirst)
    fun onMonsterSelectFirst() {
        monsterDialogFirst.show(getSupportFragmentManager(), "dialogfirst")
    }

    @OnClick(R.id.ivMonsterSecond)
    fun onMonsterSelectSecond() {
        monsterDialogSecond.show(getSupportFragmentManager(), "dialogsecond")
    }

    override fun postFirstMonster(monster: MonsterEntity) {
        Log.e("MATCHACTIVITY", monster.levels.toString())
        val resourceName = monster.getMonsterDrawCode()
        val drawableId = "${ApiModule.BASE_IMAGE_URL}/$resourceName.png"
        Picasso.get()
            .load(drawableId)
            .into(ivMonsterFirst)
        tvMonsterNameFirst.text = monster.name

        monsterFirstLevelMatchAdapter = MonsterFirstLevelMatchAdapter(applicationContext)

        spnLevelsFirst.setOnItemSelectedListener(this)

        rvLevelsFirst.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = monsterFirstLevelMatchAdapter
            monsterFirstLevelMatchAdapter.updateLevel(selectedLevelFirst)
            monsterFirstLevelMatchAdapter.updateMonsterLevel(monster.levels)
        }

    }

    override fun postSecondMonster(monster: MonsterEntity) {
        val resourceName = monster.getMonsterDrawCode()
        val drawableId = "${ApiModule.BASE_IMAGE_URL}/$resourceName.png"
        Picasso.get()
            .load(drawableId)
            .into(ivMonsterSecond)
        tvMonsterNameSecond.text = monster.name

        monsterSecondLevelMatchAdapter = MonsterSecondLevelMatchAdapter(applicationContext)

        spnLevelsSecond.setOnItemSelectedListener(this)

        rvLevelsSecond.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = monsterSecondLevelMatchAdapter
            monsterSecondLevelMatchAdapter.updateLevel(selectedLevelSecond)
            monsterSecondLevelMatchAdapter.updateMonsterLevel(monster.levels)
        }
    }

    private fun initFirstSpinner() {
        val numbers = resources.getStringArray(R.array.level_numbers)
        val aa = ArrayAdapter(this, R.layout.item_spinner, numbers)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnLevelsFirst.adapter = aa
    }

    private fun initSecondSpinner() {
        val numbers = resources.getStringArray(R.array.level_numbers)
        val aa = ArrayAdapter(this, R.layout.item_spinner, numbers)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnLevelsSecond.adapter = aa
    }

    override fun onItemSelected(parent: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

        if (parent!!.id == R.id.spnMonsterFirst) {
            selectedLevelFirst = p2
            monsterFirstLevelMatchAdapter.updateLevel(p2)
        } else {
            selectedLevelSecond = p2
            monsterSecondLevelMatchAdapter.updateLevel(p2)
        }

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}