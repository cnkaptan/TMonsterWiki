package com.cnkaptan.tmonsterswiki.ui

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cnkaptan.tmonsterswiki.AppController
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterLevelEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.SkillEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.TagEntity
import com.cnkaptan.tmonsterswiki.data.repository.MonsterRepository
import com.cnkaptan.tmonsterswiki.databinding.ActivityMonsterDetailBinding
import com.cnkaptan.tmonsterswiki.di.module.ApiModule
import com.cnkaptan.tmonsterswiki.ui.adapter.MonsterLevelAdapter
import com.cnkaptan.tmonsterswiki.ui.adapter.SkillEvoulationAdapter
import com.cnkaptan.tmonsterswiki.ui.adapter.SkillsAdapter
import com.cnkaptan.tmonsterswiki.ui.adapter.TagsAdapter
import com.cnkaptan.tmonsterswiki.ui.base.BaseActivity
import com.cnkaptan.tmonsterswiki.ui.viewmodel.MonsterDetailViewModel
import com.cnkaptan.tmonsterswiki.utils.Constants
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import org.json.JSONObject
import java.lang.Exception
import javax.inject.Inject

class MonsterDetailActivity : BaseActivity() {
    override val TAG: String
        get() = MonsterDetailActivity::class.java.simpleName

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    internal lateinit var monsterRepository: MonsterRepository
    private lateinit var binding: ActivityMonsterDetailBinding
    private lateinit var monsterDetailViewModel: MonsterDetailViewModel
    private lateinit var monsterLevelAdapter: MonsterLevelAdapter


    var monsterID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_monster_detail)
        (applicationContext as AppController).appComponent.inject(this)

        monsterID = intent.getIntExtra(ARG_MONSTER_ID, 0)

        initView()
        initViewModel(monsterID)

        binding.btnClose.setOnClickListener {
            closeInfoView()
        }
    }

    private fun closeInfoView() {
        binding.llContainerInfo.visibility = View.GONE
    }

    private fun initView() {
        val lm = LinearLayoutManager(applicationContext)

        monsterLevelAdapter = MonsterLevelAdapter(applicationContext)
        binding.rvLevels.apply {
            setHasFixedSize(true)
            layoutManager = lm
            adapter = monsterLevelAdapter
        }
        val typeFace = Typeface.createFromAsset(applicationContext.assets, Constants.MONSTERNAMEFONT)
        binding.tvMonsterName.typeface = typeFace


        binding.rvSkillChanges.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvSkillChanges.hasFixedSize()
    }

    private fun initSkillList(skillList: List<SkillEntity>) {
        binding.rvSkills.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = SkillsAdapter(context, skillList) {
                Log.e(TAG,it.getDrawResName())
                printSkillTree(it)
            }
        }
    }

    private fun openInfoView(description: String) {
        binding.tvSkill.text = description
        binding.llContainerInfo.visibility = View.VISIBLE
    }

    private fun getFormattedDescription(it: SkillEntity): String {
        var descripton = it.description

        if (!it.params.isNullOrEmpty()) {
            val paramJson = JSONObject(it.params)
            paramJson.keys()
                .forEach { key ->
                    descripton = descripton!!.replace("$<$key>", paramJson[key].toString(), true)
                }
        }
        return descripton!!
    }

    private fun initTagsList(tags: List<TagEntity>) {
        binding.rvTags.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = TagsAdapter(context, tags) {
                openInfoView(it.description)
            }
        }

    }

    private fun initViewModel(monsterID: Int) {
        monsterDetailViewModel = ViewModelProviders.of(this, viewModelFactory).get(MonsterDetailViewModel::class.java)
        monsterDetailViewModel.loadMonsterLevel(monsterID)
        monsterDetailViewModel.getMonsterLevelListLD().observe(this, Observer {
            monsterLevelAdapter.updateLevels(it)
        })

        monsterDetailViewModel.getMonsterLD().observe(this, Observer {
            initImage(it)
        })

        monsterDetailViewModel.getSkillListLD().observe(this, Observer {
            initSkillList(it)
        })

        monsterDetailViewModel.getTagListLD().observe(this, Observer {
            initTagsList(it)
        })

    }

    private fun initImage(monsterEntity: MonsterEntity) {
        binding.tvMonsterName.text = monsterEntity.name
        val monsterDrawRes = monsterEntity.getMonsterDrawCode()
        Log.e("main_icon",monsterDrawRes)
        val monsterImageUrl = "${ApiModule.BASE_IMAGE_URL}/$monsterDrawRes.png"

        Picasso.get().load(monsterImageUrl)
            .placeholder(R.drawable.splash_logo)
            .into(binding.ivMonster)

        val frameColor = when (monsterEntity.rarity) {
            1 -> R.drawable.common_frame
            2 -> R.drawable.epic_frame
            3 -> R.drawable.monstrous_frame
            else -> R.drawable.legendary_frame
        }
        binding.ivMonster.setBackgroundResource(frameColor)

        val monsterArtworkDrawRes = "artwork_$monsterDrawRes"
        Log.e("artwork_icon",monsterArtworkDrawRes)
        val monsterArtImageURl = "${ApiModule.BASE_IMAGE_URL}/$monsterArtworkDrawRes.png"

        Picasso.get()
            .load(monsterArtImageURl)
            .into(binding.ivMonsterArtwork, object : Callback {
                override fun onSuccess() {

                }

                override fun onError(e: Exception?) {
                    Picasso.get()
                        .load(R.drawable.artwork_default)
                        .into(binding.ivMonsterArtwork)
                }

            })
    }


    private fun printSkillTree(skill: SkillEntity) {
        binding.tvSkillName.text = skill.name
        binding.tvSkillDesc.text = getFormattedDescription(skill)
        binding.btnSkillDetailClose.setOnClickListener {
            binding.cvContainerSkillTree.visibility = View.GONE
        }

        disposableContainer.add(
            monsterDetailViewModel.getEvoluationSkillSet(skillId =skill.skillId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ skillChangesList ->
                    val adapter = SkillEvoulationAdapter(skillChangesList){
                        if (it.skillId == skill.skillId){
                            binding.tvSubSkillDesc.text = ""
                            binding.tvSubSkillDesc.visibility = View.GONE
                        }else{
                            binding.tvSubSkillDesc.text = getFormattedDescription(it)
                            binding.tvSubSkillDesc.visibility = View.VISIBLE
                        }

                    }
                    binding.rvSkillChanges.adapter = adapter
                    binding.cvContainerSkillTree.visibility = View.VISIBLE
                }, {
                    Log.e(TAG, it.message, it)
                })
        )
    }

    companion object {
        const val ARG_MONSTER_ID = "ARG_MONSTER_ID"

        fun newIntent(context: Context, id: Int): Intent {
            return Intent(context, MonsterDetailActivity::class.java).apply {
                putExtra(ARG_MONSTER_ID, id)
            }
        }
    }
}

sealed class Status

object ADD : Status()
object CHANGE : Status()
object DONE : Status()

data class SkillChanges(
    val Status: Status,
    val skillId: Int?,
    val outSkillId: Int?,
    val inSkillId: Int?,
    val levelEntity: MonsterLevelEntity
)
