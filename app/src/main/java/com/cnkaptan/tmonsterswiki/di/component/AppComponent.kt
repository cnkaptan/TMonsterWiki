package com.cnkaptan.tmonsterswiki.di.component

import android.app.Application
import com.cnkaptan.tmonsterswiki.ui.MonsterListActivity
import com.cnkaptan.tmonsterswiki.di.module.ApiModule
import com.cnkaptan.tmonsterswiki.di.module.DbModule
import com.cnkaptan.tmonsterswiki.di.module.ViewModelModule
import com.cnkaptan.tmonsterswiki.ui.MonsterDetailActivity
import com.cnkaptan.tmonsterswiki.ui.SplashActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Component(
    modules = [
        ApiModule::class,
        ViewModelModule::class,
        DbModule::class
    ]
)
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(monsterListActivity: MonsterListActivity)
    fun inject(splashActivity: SplashActivity)
    fun inject(monsterDetailActivity: MonsterDetailActivity)

}