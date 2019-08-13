package com.cnkaptan.tmonsterswiki.ui

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
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
import com.cnkaptan.tmonsterswiki.data.local.entity.MonsterLevelEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.SkillEntity
import com.cnkaptan.tmonsterswiki.data.local.entity.TagEntity
import com.cnkaptan.tmonsterswiki.data.repository.MonsterRepository
import com.cnkaptan.tmonsterswiki.di.module.ApiModule
import com.cnkaptan.tmonsterswiki.ui.adapter.MonsterLevelAdapter
import com.cnkaptan.tmonsterswiki.ui.adapter.SkillEvoulationAdapter
import com.cnkaptan.tmonsterswiki.ui.adapter.SkillsAdapter
import com.cnkaptan.tmonsterswiki.ui.adapter.TagsAdapter
import com.cnkaptan.tmonsterswiki.ui.base.BaseActivity
import com.cnkaptan.tmonsterswiki.ui.viewmodel.MonsterDetailViewModel
import com.cnkaptan.tmonsterswiki.utils.Constants
import com.crashlytics.android.Crashlytics
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import org.json.JSONObject
import java.lang.Exception
import java.lang.StringBuilder
import javax.inject.Inject
import kotlin.math.abs

class MonsterDetailActivity : BaseActivity() {
    override val TAG: String
        get() = MonsterDetailActivity::class.java.simpleName

    @BindView(R.id.rvLevels)
    lateinit var rvMonsterLevel: RecyclerView

    @BindView(R.id.rvTags)
    lateinit var rvTags: RecyclerView

    @BindView(R.id.rvSkills)
    lateinit var rvSkills: RecyclerView

    @BindView(R.id.rvSkillChanges)
    lateinit var rvSkillChanges: RecyclerView

    @BindView(R.id.cv_container_skill_tree)
    lateinit var cvSkillTree: RelativeLayout

    @BindView(R.id.ivMonster)
    lateinit var ivMonster: ImageView

    @BindView(R.id.ivMonsterArtwork)
    lateinit var ivMonsterArtwork: ImageView

    @BindView(R.id.ll_container_info)
    lateinit var llContainerInfo: LinearLayout

    @BindView(R.id.tvSkill)
    lateinit var tvSkill: TextView

    @BindView(R.id.tvMonsterName)
    lateinit var tvMonsterName: TextView

    @BindView(R.id.btnClose)
    lateinit var btnClose: Button

    @BindView(R.id.tvSkillName)
    lateinit var tvSkillName: TextView

    @BindView(R.id.tvSkillDesc)
    lateinit var tvSkillDesc: TextView

    @BindView(R.id.tvSubSkillDesc)
    lateinit var tvSubSkillDesc: TextView

    @BindView(R.id.btnSkillDetailClose)
    lateinit var btnSkillDetailClose: Button

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

        btnClose.setOnClickListener {
            closeInfoView()
        }
    }

    private fun closeInfoView() {
        llContainerInfo.visibility = View.GONE
    }

    private fun initView() {
        val lm = LinearLayoutManager(applicationContext)

        monsterLevelAdapter = MonsterLevelAdapter(applicationContext)
        rvMonsterLevel.apply {
            setHasFixedSize(true)
            layoutManager = lm
            adapter = monsterLevelAdapter
        }
        val typeFace = Typeface.createFromAsset(applicationContext.assets, Constants.MONSTERNAMEFONT)
        tvMonsterName.typeface = typeFace


        rvSkillChanges.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rvSkillChanges.hasFixedSize()
    }

    private fun initSkillList(skillList: List<SkillEntity>) {
        rvSkills.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = SkillsAdapter(context, skillList) {
                printSkillTree(it)
            }
        }
    }

    private fun openInfoView(description: String) {
        tvSkill.text = description
        llContainerInfo.visibility = View.VISIBLE
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
        rvTags.apply {
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
        tvMonsterName.text = monsterEntity.name
        val monsterDrawRes = monsterEntity.getMonsterDrawCode()
        val monsterImageUrl = "${ApiModule.BASE_IMAGE_URL}/$monsterDrawRes.png"

        Picasso.get().load(monsterImageUrl)
            .placeholder(R.drawable.splash_logo)
            .into(ivMonster)

        val frameColor = when (monsterEntity.rarity) {
            1 -> R.drawable.common_frame
            2 -> R.drawable.epic_frame
            3 -> R.drawable.monstrous_frame
            else -> R.drawable.legendary_frame
        }
        ivMonster.setBackgroundResource(frameColor)

        val monsterArtworkDrawRes = "artwork_$monsterDrawRes"
        val monsterArtImageURl = "${ApiModule.BASE_IMAGE_URL}/$monsterArtworkDrawRes.png"

        Picasso.get()
            .load(monsterArtImageURl)
            .into(ivMonsterArtwork, object : Callback {
                override fun onSuccess() {

                }

                override fun onError(e: Exception?) {
                    Picasso.get()
                        .load(R.drawable.artwork_default)
                        .into(ivMonsterArtwork)
                }

            })
    }


    fun printSkillTree(skill: SkillEntity) {
        tvSkillName.text = skill.name
        tvSkillDesc.text = getFormattedDescription(skill)
        btnSkillDetailClose.setOnClickListener {
            cvSkillTree.visibility = View.GONE
        }

        disposibleContainer.add(
            monsterDetailViewModel.getEvoluationSkillSet(skillId = skill.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ skillChangesList ->
                    val adapter = SkillEvoulationAdapter(skillChangesList){
                        if (it.id == skill.id){
                            tvSubSkillDesc.text = ""
                            tvSubSkillDesc.visibility = View.GONE
                        }else{
                            tvSubSkillDesc.text = getFormattedDescription(it)
                            tvSubSkillDesc.visibility = View.VISIBLE
                        }

                    }
                    rvSkillChanges.adapter = adapter
                    cvSkillTree.visibility = View.VISIBLE
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
