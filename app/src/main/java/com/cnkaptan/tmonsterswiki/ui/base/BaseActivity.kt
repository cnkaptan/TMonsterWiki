package com.cnkaptan.tmonsterswiki.ui.base

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.cnkaptan.tmonsterswiki.R
import com.cnkaptan.tmonsterswiki.data.*
import com.cnkaptan.tmonsterswiki.ui.custom.LoadingView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.disposables.DisposableContainer

abstract class BaseActivity : AppCompatActivity() {
    abstract val TAG: String
    private lateinit var loadingView: LoadingView
    lateinit var disposableContainer: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disposableContainer = CompositeDisposable()
        loadingView = LoadingView.newInstance()
    }


    fun showLoading() {
        val loading = supportFragmentManager.findFragmentByTag(LOADING_TAG)
        if (loading != null) {
            dismissLoading()
        }

        loadingView.show(supportFragmentManager, LOADING_TAG)
    }

    fun dismissLoading() {
        val loading = supportFragmentManager.findFragmentByTag(LOADING_TAG)
        loading?.let {
            supportFragmentManager.beginTransaction()
                .remove(loading)
                .commitAllowingStateLoss()
        }
    }

    fun showErrorMessage(apiError: ApiError) {
        dismissLoading()
        val alertDialog = AlertDialog.Builder(this, R.style.AppTheme)
            .setTitle(R.string.title_error)
            .setPositiveButton(R.string.btn_okey) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .setMessage(apiError.message)
            .create()
        alertDialog.show()
    }

    fun getHandler(): Observer<in State> {
        return Observer {
            handleState(it)
        }
    }

    private fun handleState(state: State) {
        when (state) {
            is LoadingShow -> showLoading()
            is LoadingHide -> dismissLoading()
            is ErrorState -> showErrorMessage(state.apiError)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposableContainer.clear()
    }

    companion object {
        private const val LOADING_TAG = "LOADING_TAG"
    }
}