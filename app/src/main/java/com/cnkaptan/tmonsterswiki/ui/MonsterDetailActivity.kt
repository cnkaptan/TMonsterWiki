package com.cnkaptan.tmonsterswiki.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cnkaptan.tmonsterswiki.AppController
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.remote.api.MonstersApi
import com.cnkaptan.tmonsterswiki.ui.base.BaseActivity
import javax.inject.Inject

class MonsterDetailActivity : BaseActivity() {
    override val TAG: String
        get() = MonsterDetailActivity::class.java.simpleName

    @Inject
    lateinit var monstersApi: MonstersApi

    var monsterID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monster_detail)
        (applicationContext as AppController).appComponent.inject(this)

        monsterID = intent.getIntExtra(ARG_MONSTER_ID,0)

        disposibleContainer.add(
            monstersApi.fetchMonsterLevelsById(monsterID)
                .subscribe()
        )
    }


    companion object{
        const val ARG_MONSTER_ID = "ARG_MONSTER_ID"


        fun newIntent(context: Context, id: Int): Intent{
            return Intent(context, MonsterDetailActivity::class.java).apply {
                putExtra(ARG_MONSTER_ID,id)
            }
        }
    }
}
