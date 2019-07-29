package com.cnkaptan.tmonsterswiki.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.cnkaptan.tmonsterswiki.AppController
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.SkillEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.TagEntity
import com.cnkaptan.tmonsterswiki.data.repository.MonsterRepository
import com.cnkaptan.tmonsterswiki.ui.adapter.MonsterLevelAdapter
import com.cnkaptan.tmonsterswiki.ui.adapter.SkillsAdapter
import com.cnkaptan.tmonsterswiki.ui.adapter.TagsAdapter
import com.cnkaptan.tmonsterswiki.ui.base.BaseActivity
import com.cnkaptan.tmonsterswiki.ui.viewmodel.MonsterDetailViewModel
import com.squareup.picasso.Picasso
import javax.inject.Inject

class MonsterDetailActivity : BaseActivity() {
    override val TAG: String
        get() = MonsterDetailActivity::class.java.simpleName

    @BindView(R.id.rvLevels)
    lateinit var rvMonsterLevel: RecyclerView

    @BindView(R.id.rvTags)
    lateinit var rvTags: RecyclerView

    @BindView(R.id.rvSkills)
    lateinit var rvSkills: RecyclerView


    @BindView(R.id.ivMonster)
    lateinit var ivMonster: ImageView

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    internal lateinit var monsterRepository: MonsterRepository

    private lateinit var monsterDetailViewModel: MonsterDetailViewModel
    private lateinit var monsterLevelAdapter: MonsterLevelAdapter


    var monsterID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monster_detail)
        (applicationContext as AppController).appComponent.inject(this)

        monsterID = intent.getIntExtra(ARG_MONSTER_ID, 0)
        ButterKnife.bind(this)

        initView()
        initViewModel(monsterID)
    }

    private fun initView() {
        val lm = LinearLayoutManager(applicationContext)

        monsterLevelAdapter = MonsterLevelAdapter(applicationContext)
        rvMonsterLevel.apply {
            setHasFixedSize(true)
            layoutManager = lm
            adapter = monsterLevelAdapter
        }
    }

    private fun initSkillList(skillList: List<SkillEntity>) {
        rvSkills.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = SkillsAdapter(context,skillList) {
                Toast.makeText(context, it.description, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initTagsList(tags: List<TagEntity>) {
        rvTags.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = TagsAdapter(context, tags) {
                Toast.makeText(context, it.description, Toast.LENGTH_SHORT).show()
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
        val drawableId =
            applicationContext.resources.getIdentifier(
                monsterEntity.resourceCode.toLowerCase(),
                "drawable", applicationContext.packageName
            )
        if (drawableId > 0) {
            Picasso.get().load(drawableId).into(ivMonster)
        } else {
            ivMonster.setImageResource(R.drawable.tm_splash_image)
        }
        val frameColor = when (monsterEntity.rarity) {
            1 -> R.drawable.common_frame
            2 -> R.drawable.epic_frame
            3 -> R.drawable.monstrous_frame
            else -> R.drawable.legendary_frame
        }
        ivMonster.setBackgroundResource(frameColor)
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
