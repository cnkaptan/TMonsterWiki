package com.cnkaptan.tmonsterswiki.data

class ApiError(message: String): Throwable(message)

sealed class State

object LoadingShow: State()
object LoadingHide: State()

class ErrorState(val apiError: ApiError): State()