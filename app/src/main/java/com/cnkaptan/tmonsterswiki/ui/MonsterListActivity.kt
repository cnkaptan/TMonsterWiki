package com.cnkaptan.tmonsterswiki.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.cnkaptan.tmonsterswiki.AppController
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.ui.adapter.ChildMonsterAdapter
import com.cnkaptan.tmonsterswiki.ui.adapter.MonsterAdapter
import com.cnkaptan.tmonsterswiki.ui.adapter.SearchMonsterAdapter
import com.cnkaptan.tmonsterswiki.ui.base.BaseActivity
import com.cnkaptan.tmonsterswiki.ui.viewmodel.MonsterListViewModel
import com.cnkaptan.tmonsterswiki.utils.playAnimation
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.analytics.FirebaseAnalytics
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.lang.IllegalStateException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MonsterListActivity : BaseActivity() {
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics
    override val TAG: String
        get() = MonsterListActivity::class.java.simpleName

    @BindView(R.id.rvMonsterList)
    lateinit var rvMonsterList: RecyclerView

    @BindView(R.id.rvSearchMonsterList)
    lateinit var rvSearchMonsterList: RecyclerView

    @BindView(R.id.tilSearchMonsters)
    lateinit var tilSearchMonsters: TextInputLayout

    @BindView(R.id.etSearchMonsters)
    lateinit var etSearchMonsters: TextInputEditText

    @BindView(R.id.ivSearchStatusIcon)
    lateinit var ivSearchStatusIcon: ImageView

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var monsterListViewModel: MonsterListViewModel

    private lateinit var monsterAdapter: MonsterAdapter
    private lateinit var searchMonsterAdapter: SearchMonsterAdapter
    private var monsterList: List<MonsterEntity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monster_list)
        (application as AppController).appComponent.inject(this)
        ButterKnife.bind(this)
        initViewModel()

        initView()

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

    private fun initView() {
        monsterAdapter = MonsterAdapter(applicationContext) { id, imageView ->
            val activityOptionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, imageView, "imageMain")

            val detailIntent = MonsterDetailActivity.newIntent(this, id)
            detailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(detailIntent, activityOptionsCompat.toBundle())
        }

        searchMonsterAdapter = SearchMonsterAdapter(applicationContext, mutableListOf()) { id, imageView ->
            val activityOptionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, imageView, "imageMain")

            val detailIntent = MonsterDetailActivity.newIntent(this, id)
            detailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(detailIntent, activityOptionsCompat.toBundle())
        }

        rvMonsterList = findViewById(R.id.rvMonsterList)
        rvMonsterList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = monsterAdapter
        }

        rvSearchMonsterList.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 4)
            adapter = searchMonsterAdapter
        }

        disposibleContainer.add(
            RxTextView.textChanges(etSearchMonsters)
                .debounce(300, TimeUnit.MILLISECONDS)
                .map { it.toString() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ searchText ->
                    if (searchText.length < 3){
                        openCategorizedMenu()
                    }else{
                        openSearchList(searchText)
                    }
                }, {
                    openCategorizedMenu()
                })
        )

        monsterListViewModel.getMonsterList().observe(this, Observer {
            this.monsterList = it
        })

        ivSearchStatusIcon.setOnClickListener {
            etSearchMonsters.setText("")
            openCategorizedMenu()
        }
    }

    private fun openCategorizedMenu() {
        rvMonsterList.playAnimation(R.anim.fade_in,onAnimationEnd = {
            rvMonsterList.visibility = View.VISIBLE
        })
        rvSearchMonsterList.playAnimation(R.anim.fade_out,onAnimationEnd = {
            rvSearchMonsterList.visibility = View.GONE
        })

        etSearchMonsters.clearFocus()
    }

    private fun openSearchList(searchText: String) {
        if (!monsterList.isNullOrEmpty()) {
            val searchedList = monsterList!!.filter {
                it.name.toLowerCase().contains(searchText.toLowerCase(),ignoreCase = true)
            }.toList()

            searchMonsterAdapter.updateList(searchedList)
            rvSearchMonsterList.playAnimation(R.anim.fade_in,onAnimationEnd = {
                rvSearchMonsterList.visibility = View.VISIBLE
            })
            rvMonsterList.playAnimation(R.anim.fade_out,onAnimationEnd = {
                rvMonsterList.visibility = View.GONE
            })
        }
    }

    private fun initViewModel() {
        monsterListViewModel = ViewModelProviders.of(this, viewModelFactory).get(MonsterListViewModel::class.java)
        monsterListViewModel.loadMonsters()
        monsterListViewModel.getMonsterGroups().observe(this, Observer {
            monsterAdapter.updateList(it)
        })
    }
}