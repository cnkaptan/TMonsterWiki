package com.cnkaptan.tmonsterswiki.utils


import android.text.TextUtils
import com.cnkaptan.tmonsterswiki.BuildConfig

class Log(val TAG: String, val useReflection: Boolean = true, val isDebug: Boolean = BuildConfig.DEBUG) {
    /**
     * Debug
     */
    fun d(msg: String, tag: String = TAG){
        if(isDebug)
            android.util.Log.d(tag, if(useReflection) getLocation() + msg else msg)
    }
    /**
     * Debug
     */
    fun d(msg: String){
        if(isDebug)
            android.util.Log.d(TAG, if(useReflection) getLocation() + msg else msg)
    }

    /**
     * Debug
     */
    fun d(msg: String,throwable: Throwable){
        if(isDebug)
            android.util.Log.d(TAG, if(useReflection) getLocation() + msg else msg,throwable)
    }

    /**
     * Verbose
     */
    fun v(msg: String){
        if(isDebug)
            android.util.Log.v(TAG, if(useReflection) getLocation() + msg else msg)
    }

    /**
     * Verbose
     */
    fun v(msg: String, throwable: Throwable){
        if(isDebug)
            android.util.Log.v(TAG, if(useReflection) getLocation() + msg else msg,throwable)
    }

    /**
     * Warn
     */
    fun w(msg: String){
        if(isDebug)
            android.util.Log.w(TAG, if(useReflection) getLocation() + msg else msg)
    }

    /**
     * Warn
     */
    fun w(msg: String, throwable: Throwable){
        if(isDebug)
            android.util.Log.w(TAG, if(useReflection) getLocation() + msg else msg,throwable)
    }

    /**
     * Error
     */
    fun e(msg: String, err: Throwable?){
        android.util.Log.e(TAG, if(useReflection) getLocation() + msg else msg, err)
    }

    /**
     * Error
     */
    fun e(msg: String, tag: String = TAG){
        android.util.Log.e(tag, if(useReflection) getLocation() + msg else msg)
    }

    /**
     * Assert
     */
    fun a(msg: String, err: Throwable?){
        val message=if(useReflection) getLocation() + msg else msg
        android.util.Log.println(android.util.Log.ASSERT,TAG,"$message:\n${android.util.Log.getStackTraceString(err)}")
    }

    /**
     * Assert
     */
    fun a(msg: String){
        android.util.Log.println(android.util.Log.ASSERT,TAG, if(useReflection) getLocation() + msg else msg)
    }

    /**
     * Debug-Error
     */
    fun de(msg: String, err: Throwable?, tag: String = TAG){
        if(isDebug)
            android.util.Log.d(tag, if(useReflection) getLocation() + msg else msg, err)
    }


    private fun getLocation(): String {
        val className = Log::class.java.name
        val traces = Thread.currentThread().stackTrace
        var found = false

        for (i in traces.indices) {
            val trace = traces[i]

            try {
                if (found) {
                    if (!trace.className.startsWith(className)) {
                        val clazz = Class.forName(trace.className)
                        return "[" + getClassName(clazz) + ":" + trace.methodName + ":" + trace.lineNumber + "]: "
                    }
                } else if (trace.className.startsWith(className)) {
                    found = true
                    continue
                }
            } catch (e: ClassNotFoundException) {
            }

        }

        return "[]: "
    }

    private fun getClassName(clazz: Class<*>?): String {
        return if (clazz != null) {
            if (!TextUtils.isEmpty(clazz.simpleName)) {
                clazz.simpleName
            } else getClassName(clazz.enclosingClass)

        } else ""

    }

}


val mLog = Log("cnkaptan", BuildConfig.DEBUG, BuildConfig.DEBUG)