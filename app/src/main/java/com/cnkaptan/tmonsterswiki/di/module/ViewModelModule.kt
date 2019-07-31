package com.cnkaptan.tmonsterswiki.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cnkaptan.tmonsterswiki.di.ViewModelKey
import com.cnkaptan.tmonsterswiki.factory.ViewModelFactory
import com.cnkaptan.tmonsterswiki.ui.viewmodel.MonsterDetailViewModel
import com.cnkaptan.tmonsterswiki.ui.viewmodel.MonsterListViewModel
import com.cnkaptan.tmonsterswiki.ui.viewmodel.MonsterTDexViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MonsterListViewModel::class)
    protected abstract fun movieListViewModel(moviesListViewModel: MonsterListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MonsterDetailViewModel::class)
    protected abstract fun movideDetailViewModel(movieDetailViewModel: MonsterDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MonsterTDexViewModel::class)
    protected abstract fun movieTDexViewModel(movieTDexViewModel: MonsterTDexViewModel): ViewModel

}