package com.cnkaptan.tmonsterswiki.utils.transition

import androidx.transition.Transition
import com.cnkaptan.tmonsterswiki.utils.mLog

class LoggingTransitionListener(val logName: String = "Transition") : Transition.TransitionListener {
    override fun onTransitionEnd(transition: Transition) {
        mLog.d("[$logName]")
    }

    override fun onTransitionResume(transition: Transition) {
        mLog.d("[$logName]")
    }

    override fun onTransitionPause(transition: Transition) {
        mLog.d("[$logName]")
    }

    override fun onTransitionCancel(transition: Transition) {
        mLog.d("[$logName]")
    }

    override fun onTransitionStart(transition: Transition) {
        mLog.d("[$logName]")
    }
}