package com.cnkaptan.tmonsterswiki.di.component

import android.app.Application
import com.cnkaptan.tmonsterswiki.AppController
import com.cnkaptan.tmonsterswiki.MainActivity
import com.cnkaptan.tmonsterswiki.di.module.ApiModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Component(
    modules = [
        ApiModule::class
    ]
)
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(mainActivity: MainActivity)

}