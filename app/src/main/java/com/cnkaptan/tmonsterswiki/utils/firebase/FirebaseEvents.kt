package com.cnkaptan.tmonsterswiki.utils.firebase

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class FirebaseEvents(
    val eventName: String,
    val priority: EventPriority? = null,
    val properties: MutableMap<String, String> = mutableMapOf()
) {

    companion object {

        const val PRIORTY = "Priority"
        const val YES = "Yes"
        const val NO = "No"
        const val CLOSED = "Closed"
        const val OPENED = "Opened"
        const val FROM = "From"
    }

    class Builder private constructor(private val eventName: String) {

        private constructor(eventName: String, eventPriority: EventPriority) : this(eventName) {
            setPriority(eventPriority)
        }

        private var priority: EventPriority? = null
        private val propMap: MutableMap<String, String> = mutableMapOf()


        fun setPriority(priority: EventPriority): Builder {
            this.priority = priority
            return this
        }

        fun addProperty(property: Pair<String, String>): Builder {
            propMap[property.first] = property.second
            return this
        }

        fun build(): FirebaseEvents {
            return FirebaseEvents(eventName, priority, propMap)
        }

        fun send(analytics: FirebaseAnalytics) {
            build().sendCustomEvent(analytics)
        }

        companion object {
            fun builder(eventName: String) = Builder(eventName)

            fun builder(eventName: String, eventPriority: EventPriority) = Builder(eventName)
        }
    }

}

private fun FirebaseEvents.sendCustomEvent(analytics: FirebaseAnalytics) {
    val firebaseEvent = eventName
    val bundle = Bundle().apply {

        priority?.let {
            putString(FirebaseEvents.PRIORTY,priority.toString())
        }

        if (!properties.isNullOrEmpty()){
            properties.forEach{
                putString(it.key,it.value)
            }
        }
    }

    analytics.logEvent(firebaseEvent,bundle)
}
