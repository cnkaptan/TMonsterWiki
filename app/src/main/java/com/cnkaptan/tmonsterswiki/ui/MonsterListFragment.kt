package com.cnkaptan.tmonsterswiki.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cnkaptan.tmonsterswiki.AppController
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.databinding.ActivityMonsterListBinding
import com.cnkaptan.tmonsterswiki.ui.adapter.MonsterAdapter
import com.cnkaptan.tmonsterswiki.ui.adapter.SearchMonsterAdapter
import com.cnkaptan.tmonsterswiki.ui.base.BaseFragment
import com.cnkaptan.tmonsterswiki.ui.viewmodel.MonsterListViewModel
import com.cnkaptan.tmonsterswiki.utils.playAnimation
import com.google.firebase.analytics.FirebaseAnalytics
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MonsterListFragment : BaseFragment() {
    private lateinit var binding: ActivityMonsterListBinding
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics
    override val TAG: String
        get() = MonsterListFragment::class.java.simpleName

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var monsterListViewModel: MonsterListViewModel

    private lateinit var monsterAdapter: MonsterAdapter
    private lateinit var searchMonsterAdapter: SearchMonsterAdapter
    private var monsterList: List<MonsterEntity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ((activity!!.applicationContext) as AppController).appComponent.inject(this)
        monsterListViewModel = ViewModelProviders.of(this, viewModelFactory).get(MonsterListViewModel::class.java)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = DataBindingUtil.inflate(inflater, R.layout.activity_monster_list, container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initView()
    }

    private fun initView() {
        monsterAdapter = MonsterAdapter(requireContext()) { id, imageView ->
            val activityOptionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity!!, imageView, "imageMain")

            val detailIntent = MonsterDetailActivity.newIntent(requireContext(), id)
            detailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(detailIntent, activityOptionsCompat.toBundle())
        }

        searchMonsterAdapter = SearchMonsterAdapter(requireContext(), mutableListOf()) { id, imageView ->
            val activityOptionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity!!, imageView, "imageMain")

            val detailIntent = MonsterDetailActivity.newIntent(requireContext(), id)
            detailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(detailIntent, activityOptionsCompat.toBundle())
        }

        binding.rvMonsterList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = monsterAdapter
        }

        binding.rvSearchMonsterList.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 4)
            adapter = searchMonsterAdapter
        }

        disposableContainer.add(
            RxTextView.textChanges(binding.etSearchMonsters)
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

        binding.ivSearchStatusIcon.setOnClickListener {
            binding.etSearchMonsters.setText("")
            openCategorizedMenu()
        }

    }

    private fun openCategorizedMenu() {
        binding.rvMonsterList.playAnimation(R.anim.fade_in,onAnimationEnd = {
            binding.rvMonsterList.visibility = View.VISIBLE
        })
        binding.rvSearchMonsterList.playAnimation(R.anim.fade_out,onAnimationEnd = {
            binding.rvSearchMonsterList.visibility = View.GONE
        })

        binding.etSearchMonsters.clearFocus()
    }

    private fun openSearchList(searchText: String) {
        if (!monsterList.isNullOrEmpty()) {
            val searchedList = monsterList!!.filter {
                it.name.toLowerCase().contains(searchText.toLowerCase(),ignoreCase = true)
            }.toList()

            searchMonsterAdapter.updateList(searchedList)
            binding.rvSearchMonsterList.playAnimation(R.anim.fade_in,onAnimationEnd = {
                binding.rvSearchMonsterList.visibility = View.VISIBLE
            })
            binding.rvMonsterList.playAnimation(R.anim.fade_out,onAnimationEnd = {
                binding.rvMonsterList.visibility = View.GONE
            })
        }
    }

    private fun initViewModel() {
        monsterListViewModel.getMonsterGroups().observe(this, Observer {
            monsterAdapter.updateList(it)
        })
    }
}