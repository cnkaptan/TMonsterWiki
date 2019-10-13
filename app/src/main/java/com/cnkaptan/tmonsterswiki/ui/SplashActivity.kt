package com.cnkaptan.tmonsterswiki.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import butterknife.BindView
import butterknife.ButterKnife
import com.cnkaptan.tmonsterswiki.AppController
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.repository.MonsterRepository
import com.cnkaptan.tmonsterswiki.ui.base.BaseActivity
import com.white.progressview.HorizontalProgressView
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class SplashActivity : BaseActivity() {
    override val TAG: String
        get() = SplashActivity::class.java.simpleName

    @BindView(R.id.progressBar)
    lateinit var mProgressBar:HorizontalProgressView

    @Inject
    lateinit var monsterRepository: MonsterRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        (application as AppController).appComponent.inject(this)
        ButterKnife.bind(this)

        mProgressBar.reachBarSize=10
        mProgressBar.progressPosition=HorizontalProgressView.CENTRE



        disposibleContainer.add(
            monsterRepository.getInitialAppData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    mProgressBar.setProgressInTime(0,50,500)
                    mProgressBar.setProgressInTime(50,100,500)
                    startActivity(Intent(this, MonsterTDex::class.java))
                    finish()
                }, { error ->
                    Log.e(TAG, error.message, error)
                })
        )
    }
}
