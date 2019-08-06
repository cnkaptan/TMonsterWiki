package com.cnkaptan.tmonsterswiki.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.cnkaptan.tmonsterswiki.AppController
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.repository.MonsterRepository
import com.cnkaptan.tmonsterswiki.ui.base.BaseActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SplashActivity : BaseActivity() {
    override val TAG: String
        get() = SplashActivity::class.java.simpleName

    @Inject
    lateinit var monsterRepository: MonsterRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        (application as AppController).appComponent.inject(this)

        disposibleContainer.add(
            monsterRepository.downloadInitialInfos()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    startActivity(Intent(this, MonsterListActivity::class.java))
                    finish()
                }, { error ->
                    Log.e(TAG, error.message, error)
                })
        )
    }
}
