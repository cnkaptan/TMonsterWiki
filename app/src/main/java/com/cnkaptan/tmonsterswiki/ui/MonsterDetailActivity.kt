package com.cnkaptan.tmonsterswiki.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.GridView
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cnkaptan.tmonsterswiki.AppController
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.SkillEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.TagEntity
import com.cnkaptan.tmonsterswiki.data.repository.MonsterRepository
import com.cnkaptan.tmonsterswiki.remote.api.MonstersApi
import com.cnkaptan.tmonsterswiki.ui.adapter.MonsterLevelAdapter
import com.cnkaptan.tmonsterswiki.ui.adapter.SkillsAdapter
import com.cnkaptan.tmonsterswiki.ui.adapter.TagsAdapter
import com.cnkaptan.tmonsterswiki.ui.base.BaseActivity
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MonsterDetailActivity : BaseActivity() {
    override val TAG: String
        get() = MonsterDetailActivity::class.java.simpleName

    @Inject
    lateinit var monstersApi: MonstersApi

    @Inject
    lateinit var monsterRepository: MonsterRepository

    private lateinit var rvMonsterLevel: RecyclerView
    private lateinit var rvTags: RecyclerView
    private lateinit var rvSkills: RecyclerView
    private lateinit var ivMonster: ImageView
    private lateinit var gvSkills: GridView


    var monsterID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monster_detail)
        (applicationContext as AppController).appComponent.inject(this)

        monsterID = intent.getIntExtra(ARG_MONSTER_ID, 0)

        rvMonsterLevel = findViewById(R.id.rvLevels)
        rvTags = findViewById(R.id.rvTags)
        ivMonster = findViewById(R.id.ivMonster)
        rvSkills = findViewById(R.id.rvSkills)


        val lm = LinearLayoutManager(applicationContext)

        val monsterLevelAdapter = MonsterLevelAdapter(applicationContext)
        rvMonsterLevel.apply {
            setHasFixedSize(true)
            layoutManager = lm
            adapter = monsterLevelAdapter
        }

        disposibleContainer.add(
            monsterRepository.getMonsterLevels(monsterID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { monsterLevelAdapter.updateLevels(it) },
                    { error -> Log.e(TAG, error.message, error) })
        )

        disposibleContainer.add(
            monsterRepository.getMonster(monsterID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { initImage(it) },
                    { error -> Log.e(TAG, error.message, error) })
        )

        disposibleContainer.add(
            monsterRepository.getMonster(monsterID)
                .subscribeOn(Schedulers.io())
                .flattenAsFlowable { it.tags }
                .flatMapSingle { monsterRepository.getTagById(it) }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { initTagsList(it) },
                    { error -> Log.e(TAG, error.message, error) })
        )

        disposibleContainer.add(
            monsterRepository.getMonsterLevels(monsterID)
                .subscribeOn(Schedulers.io())
                .flattenAsFlowable { it.last().skillIds }
                .flatMapSingle { monsterRepository.getSkillById(it) }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { initSkillList(it) },
                    { error -> Log.e(TAG, error.message, error) })
        )
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


    companion object {
        const val ARG_MONSTER_ID = "ARG_MONSTER_ID"

        fun newIntent(context: Context, id: Int): Intent {
            return Intent(context, MonsterDetailActivity::class.java).apply {
                putExtra(ARG_MONSTER_ID, id)
            }
        }
    }

    fun initImage(monsterEntity: MonsterEntity) {
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
}
