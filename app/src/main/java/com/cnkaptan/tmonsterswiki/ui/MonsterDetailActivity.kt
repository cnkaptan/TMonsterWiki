package com.cnkaptan.tmonsterswiki.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cnkaptan.tmonsterswiki.AppController
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.repository.MonsterRepository
import com.cnkaptan.tmonsterswiki.remote.api.MonstersApi
import com.cnkaptan.tmonsterswiki.ui.adapter.MonsterLevelAdapter
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
    private lateinit var ivMonster: ImageView


    var monsterID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monster_detail)
        (applicationContext as AppController).appComponent.inject(this)

        monsterID = intent.getIntExtra(ARG_MONSTER_ID, 0)

        rvMonsterLevel = findViewById(R.id.rvLevels)
        ivMonster = findViewById(R.id.ivMonster)


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
                    { error -> Log.e(TAG, error.message, error) }
                )
        )

        disposibleContainer.add(
            monsterRepository.getMonster(monsterID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ initImage(it.resourceCode.toLowerCase()) }
                    , { error -> Log.e(TAG, error.message, error) })
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

    fun initImage(resourceName: String) {
        val drawableId = applicationContext.resources
            .getIdentifier(resourceName, "drawable", applicationContext.packageName)
        if (drawableId > 0) {
            Picasso.get().load(drawableId).into(ivMonster)
        } else {
            ivMonster.setImageResource(R.drawable.tm_splash_image)
        }
    }
}
