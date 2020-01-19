package com.cnkaptan.tmonsterswiki.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import com.cnkaptan.tmonsterswiki.ui.base.BaseFragment
import com.google.firebase.analytics.FirebaseAnalytics

abstract class DataBindingFragment<B:ViewDataBinding>(@LayoutRes private val layoutResId:Int): BaseFragment(){
    private var mBinding:B?=null

    val analytics: FirebaseAnalytics get() = FirebaseAnalytics.getInstance(context!!)
    val hasBinding get() = mBinding != null

    val binding get() = mBinding?:throw IllegalStateException("Fragment $this is ${lifecycle.currentState}")
    open val instanceVariableId:Int= BR.fragment

    protected open fun onBindingCreated(binding: B,savedInstanceState: Bundle?)=Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding=DataBindingUtil.inflate(inflater,layoutResId,container,false)
        mBinding?.setLifecycleOwner(this)
        binding.setVariable(instanceVariableId,this)
        return mBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runCatching { activity?.let {analytics.setCurrentScreen(it, this::class.simpleName, null) } }
        onBindingCreated(mBinding?: throw IllegalStateException("Binding is null!!!"),savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding=null
    }

}