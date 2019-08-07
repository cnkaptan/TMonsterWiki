package com.cnkaptan.tmonsterswiki.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.cnkaptan.tmonsterswiki.AppController
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.data.repository.MonsterRepository
import com.cnkaptan.tmonsterswiki.remote.api.MonstersApi
import com.cnkaptan.tmonsterswiki.ui.adapter.MonsterAdapter
import com.cnkaptan.tmonsterswiki.ui.base.BaseActivity
import com.cnkaptan.tmonsterswiki.ui.viewmodel.MonsterListViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MonsterListActivity : BaseActivity() {
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics

    override val TAG: String
        get() = MonsterListActivity::class.java.simpleName

    @BindView(R.id.rvMonsterList)
    lateinit var rvMonsterList: RecyclerView

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var monsterListViewModel: MonsterListViewModel
    private lateinit var monsterAdapter: MonsterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monster_list)
        (application as AppController).appComponent.inject(this)
        ButterKnife.bind(this)

        initView()
        initViewModel()

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

    private fun initView() {
        monsterAdapter = MonsterAdapter(applicationContext){ id, imageView->
            val activityOptionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this,imageView,"imageMain")

            val detailIntent = MonsterDetailActivity.newIntent(this, id)
            detailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(detailIntent,activityOptionsCompat.toBundle())
        }

        rvMonsterList = findViewById(R.id.rvMonsterList)
        rvMonsterList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = monsterAdapter
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