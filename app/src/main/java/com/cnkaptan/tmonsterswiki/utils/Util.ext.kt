@file:JvmName("Util")
package com.cnkaptan.tmonsterswiki.utils

import android.app.Activity
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.text.Spannable
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.collection.LongSparseArray
import androidx.databinding.ObservableList
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import androidx.navigation.*
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.transition.Transition
import androidx.transition.TransitionSet
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.reflect.TypeToken
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.SingleSource
import io.reactivex.disposables.Disposable
import com.cnkaptan.tmonsterswiki.utils.transition.SimpleTransitionListener
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.min

// TODO bunlari update et ve grupla
inline fun<reified M:ViewModel> FragmentActivity.viewModel():Lazy<M> = lazy {
    ViewModelProviders.of(this).get(M::class.java)
}

inline fun<reified M:ViewModel> Fragment.viewModel(bindToActivityLifecycle:Boolean=false):Lazy<M> = lazy {
    val provider=if(bindToActivityLifecycle)ViewModelProviders.of(activity!!)
    else ViewModelProviders.of(this)
    provider.get(M::class.java)
}

fun <T> Observable<T>.toLiveData():LiveData<T> = object : LiveData<T>(){
    private var disposable:Disposable?=null
    override fun onActive() {
        disposable?.dispose()
        disposable=subscribe(this::postValue)
    }

    override fun onInactive() {
        disposable?.dispose()
        disposable=null
    }
}

fun <T> Observable<T>.toSingleLiveEvent(): SingleLiveEvent<T> = object: SingleLiveEvent<T>(){
    private var disposable:Disposable?=null
    override fun onActive() {
        disposable?.dispose()
        disposable = subscribe(this::postValue)
    }
    override fun onInactive() {
        disposable?.dispose()
        disposable = null
    }
}

fun EditText.showSoftKeyboard() = runCatching {
    post(
        object : Runnable{
            override fun run() {
                if(requestFocus()){
                    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.showSoftInput(this@runCatching,InputMethodManager.SHOW_FORCED)
                }else postDelayed(this,100)
            }
        }
    )
}.getOrDefault(false)

fun <T,R> LiveData<T>.map(mapper:(T)->R):LiveData<R> = Transformations.map(this,mapper)
fun <T,R> LiveData<T>.switchMap(mapper:(T)->LiveData<R>):LiveData<R> = Transformations.switchMap(this,mapper)
fun <T,R> LiveData<T>.distinctUntilChanged(selector:(T?)->R?):LiveData<T>{
    val result=MediatorLiveData<T>()
    result.addSource(this){
        val current=result.value
        if(selector(current)!=selector(it))result.value=it
    }
    return result
}

fun <T> LiveData<T>.distinctUntilChanged():LiveData<T>{
    val result=MediatorLiveData<T>()
    result.addSource(this){if(it!=result.value)result.value=it}
    return result
}

fun <T> LiveData<T>.debounce(timeout:Long,timeUnit: TimeUnit):LiveData<T> = object : MediatorLiveData<T>(){
    private val handler=Handler()
    private var setValueAction:Runnable?=null

    init {
        addSource(this@debounce){
            setValueAction?.let { handler.removeCallbacks(it) }
            setValueAction = Runnable { value=it }
            handler.postDelayed(setValueAction!!,timeUnit.toMillis(timeout))
        }
    }

    override fun onInactive() {
        super.onInactive()
        handler.removeCallbacksAndMessages(null)
        setValueAction=null
    }
}

fun transitionSetOf(vararg transitions: Transition):TransitionSet{
    val set=TransitionSet()
    transitions.forEach { set.addTransition(it) }
    return set
}

fun TransitionSet.playTogether(): TransitionSet {
    ordering = TransitionSet.ORDERING_TOGETHER
    return this
}

fun TransitionSet.playSequentialy(): TransitionSet {
    ordering = TransitionSet.ORDERING_SEQUENTIAL
    return this
}

inline fun Transition.doOnStart(crossinline action: (Transition) -> Unit): Transition =
    this.also {
        addListener(object : SimpleTransitionListener() {
            override fun onTransitionStart(transition: Transition) {
                action.invoke(transition)
                removeListener(this)
            }
        })
    }


inline fun Transition.doOnEnd(crossinline action: (Transition) -> Unit): Transition =
    this.also {
        addListener(object : SimpleTransitionListener() {
            override fun onTransitionEnd(transition: Transition) {
                action.invoke(transition)
                removeListener(this)
            }
        })
    }


inline fun <reified T> getType(): Type {
    return object : TypeToken<T>(){}.type
}

interface Logger{
    fun log(message: String,throwable: Throwable)
    fun log(message: String)
}

class Logcat(tag:String){
    private val log=Log(tag)
    val verbose:Logger = object : Logger{
        override fun log(message: String) = log.v(message)
        override fun log(message: String, throwable: Throwable) = log.v(message,throwable)
    }
    val debug:Logger = object : Logger{
        override fun log(message: String) = log.d(message)
        override fun log(message: String, throwable: Throwable) = log.d(message,throwable)
    }
    val warning:Logger = object : Logger{
        override fun log(message: String) = log.w(message)
        override fun log(message: String, throwable: Throwable) = log.w(message,throwable)
    }
    val error:Logger = object : Logger{
        override fun log(message: String) = log.e(message)
        override fun log(message: String, throwable: Throwable) = log.e(message,throwable)
    }
    val assert:Logger = object : Logger{
        override fun log(message: String) = log.a(message)
        override fun log(message: String, throwable: Throwable) = log.a(message,throwable)
    }
}

val Any.logcat:Logcat get() = Logcat(javaClass.simpleName)
fun Any.logcat(prefix: String) = Logcat("$prefix${javaClass.simpleName}")

fun EditText.doOnEnter(cb: (String) -> Boolean) {
    setOnKeyListener { v, keyCode, event ->
        if(event.action == KeyEvent.ACTION_UP && event.keyCode == KeyEvent.KEYCODE_ENTER){
            cb(text.toString())
        } else false
    }
}

fun Float.fromDpToPx(r: Resources): Float{
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, r.getDisplayMetrics());
}


fun <T,L:ObservableList<T>> L.toLiveData():LiveData<L> = object : LiveData<L>(){
    private val callback=object : ObservableList.OnListChangedCallback<L>(){
        override fun onChanged(sender: L) = postValue(sender)
        override fun onItemRangeChanged(sender: L, positionStart: Int, itemCount: Int) = onChanged(sender)
        override fun onItemRangeInserted(sender: L, positionStart: Int, itemCount: Int) = onChanged(sender)
        override fun onItemRangeMoved(sender: L, fromPosition: Int, toPosition: Int, itemCount: Int) = onChanged(sender)
        override fun onItemRangeRemoved(sender: L, positionStart: Int, itemCount: Int) = onChanged(sender)
    }
    override fun onActive() {
        value=this@toLiveData
        addOnListChangedCallback(callback)
    }

    override fun onInactive() {
        super.onInactive()
        removeOnListChangedCallback(callback)
    }
}

fun Fragment.navigateTo(actionId: Int, vararg views: View){
    runCatching {
        findNavController().navigateSafe(
            actionId,
            null, null, FragmentNavigator.Extras.Builder().apply {
                views.forEach { view ->
                    addSharedElement(view, view.transitionName)
                }
            }.build()
        )
    }.getOrElse { logcat.warning.log("Navigation error: $it", it) }
}

/**
 * @return highlighted words count
 */
fun Spannable.highlight(words:List<String>,vararg span:Any):Boolean{
    span.forEach { removeSpan(it) }
    var result=false
    Regex("\\w+").findAll(this).forEach {matchResult->
        if(!words.contains(matchResult.value.toLowerCase(Locale.getDefault())))return@forEach
        result=true
        span.forEach { setSpan(it,matchResult.range.first,matchResult.range.last+1,Spannable.SPAN_USER) }
    }
    return result
}

fun Iterable<*>.containsAny(items:Iterable<*>):Boolean{
    items.forEach { if(contains(it))return true }
    return false
}

inline fun <T> Completable.andThenSingle(action:()->SingleSource<T>):Single<T>{
    return andThen(action())
}

fun <T> List<T>.subListOrLess(start:Int,end:Int):List<T> = runCatching { subList(start,end) }.getOrElse { subList(start,size) }

fun <T> List<T>.trySubList(endIndex: Int) = subList(0, min(size, endIndex))

fun Fragment.invalidateDecorView() {
    activity?.window?.decorView?.findViewById<View>(android.R.id.content)?.invalidate()
}

fun <T> Observable<T>.throttleDistinct(timeoutMs:Long=200L): Observable<T> = this.throttleLast(timeoutMs, TimeUnit.MILLISECONDS).distinctUntilChanged()

inline fun <T> MutableList<T>.removeWhen(condition:(T)->Boolean):Int{
    val list=ArrayList(this)
    var count=0
    list.forEach { if(condition(it)&&remove(it))count++ }
    return count
}

fun FirebaseAnalytics.logEvent(event: String) {
    logEvent(event, null)
}

fun Transition.addTargets(vararg views: View): Transition {
    views.forEach { v -> addTarget(v) }
    return this
}

fun <T> LongSparseArray<T>.forEach(observer: (Long, T)->Unit) {
    for(i in 0..size()){
        val k = keyAt(i)
        get(k)?.let { observer.invoke(k, it) }
    }
}


// https://stackoverflow.com/a/54146679
fun NavController.navigateSafe(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navExtras: Navigator.Extras? = null
) {
    val action = currentDestination?.getAction(resId) ?: graph.getAction(resId)
    if (action != null && currentDestination?.id != action.destinationId) {
        navigate(resId, args, navOptions, navExtras)
    } else {
        logcat.debug.log("Warning: destination was not found for this controller")
    }
}

fun NavController.navigateSafe(
    dest: NavDirections, navigatorExtras: Navigator.Extras? = null
) = navigateSafe(dest.actionId, dest.arguments, null, navigatorExtras)
