package com.vlahov.data.utils

import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

fun <T, R> Continuation<Result<R>>.failure(result: Result<T>) {
    result.exceptionOrNull()?.let { this.resume(Result.failure(it)) } ?: this.resume(
        Result.failure(
            UnknownError()
        )
    )
}