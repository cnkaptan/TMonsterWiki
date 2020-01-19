package com.cnkaptan.tmonsterswiki.utils.firebase

enum class EventPriority(private val priority: String) {
    HIGH("High"), MEDIUM("Medium"), LOW("Low");

    override fun toString(): String {
        return priority
    }
}