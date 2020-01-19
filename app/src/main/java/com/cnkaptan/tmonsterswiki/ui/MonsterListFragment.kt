package com.cnkaptan.tmonsterswiki.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.databinding.FragmentMonsterListBinding
import com.cnkaptan.tmonsterswiki.ui.adapter.MonsterAdapter
import com.cnkaptan.tmonsterswiki.ui.adapter.SearchMonsterAdapter
import com.cnkaptan.tmonsterswiki.ui.viewmodel.MonsterListViewModel
import com.cnkaptan.tmonsterswiki.utils.DataBindingFragment
import com.cnkaptan.tmonsterswiki.utils.playAnimation
import com.cnkaptan.tmonsterswiki.utils.viewModel
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class MonsterListFragment : DataBindingFragment<FragmentMonsterListBinding>(R.layout.fragment_monster_list) {
    override val TAG: String
        get() = MonsterListFragment::class.java.simpleName

    private lateinit var monsterAdapter: MonsterAdapter
    private lateinit var searchMonsterAdapter: SearchMonsterAdapter

    private val monsterListViewModel by viewModel<MonsterListViewModel>(true)

    override fun onBindingCreated(binding: FragmentMonsterListBinding, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)
        binding.viewModel = monsterListViewModel

        monsterAdapter = MonsterAdapter(requireContext()) { id, imageView ->
            val activityOptionsCompat = ActivityOptionsCompat
                .makeSceneTransitionAnimation(activity!!, imageView, "imageMain")

            val detailIntent = MonsterDetailActivity.newIntent(requireContext(), id)
            detailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(detailIntent, activityOptionsCompat.toBundle())
        }

        searchMonsterAdapter =
            SearchMonsterAdapter(requireContext(), mutableListOf()) { id, imageView ->
                val activityOptionsCompat = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity!!, imageView, "imageMain")

                val detailIntent = MonsterDetailActivity.newIntent(requireContext(), id)
                detailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(detailIntent, activityOptionsCompat.toBundle())
            }

        binding.rvMonsterList.apply {
            setHasFixedSize(true)
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
                    if (searchText.length < 2) {
                        openCategorizedMenu()
                    } else {
                        if (binding.rvSearchMonsterList.visibility == View.VISIBLE){
                            monsterListViewModel.querySearch(searchText)
                        }else{
                            openSearchList(searchText)
                        }
                    }
                }, {
                    openCategorizedMenu()
                })
        )


        binding.ivSearchStatusIcon.setOnClickListener {
            binding.etSearchMonsters.setText("")
            openCategorizedMenu()
        }
    }


    private fun openCategorizedMenu() {
        binding.rvMonsterList.playAnimation(R.anim.fade_in, onAnimationEnd = {
            binding.rvMonsterList.visibility = View.VISIBLE
        })
        binding.rvSearchMonsterList.playAnimation(R.anim.fade_out, onAnimationEnd = {
            binding.rvSearchMonsterList.visibility = View.GONE
        })

        binding.etSearchMonsters.clearFocus()
    }

    private fun openSearchList(searchText: String) {
        monsterListViewModel.querySearch(searchText)

        binding.rvSearchMonsterList.playAnimation(R.anim.fade_in, onAnimationEnd = {
            binding.rvSearchMonsterList.visibility = View.VISIBLE
        })

        binding.rvMonsterList.playAnimation(R.anim.fade_out, onAnimationEnd = {
            binding.rvMonsterList.visibility = View.GONE
        })
    }

}