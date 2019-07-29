package com.cnkaptan.tmonsterswiki.ui

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.ui.base.BaseActivity

class MonsterTDex : BaseActivity() {
    override val TAG: String
        get() = MonsterTDex::class.java.simpleName

    @BindView(R.id.rvMonsterDexList)
    lateinit var rvMonsterDexList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monster_dex)
        ButterKnife.bind(this)
    }
}
