package hr.vlahov.newsdemo.utils

import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.compose.LazyPagingItems

fun LoadStates.exceptionOrNull() = (append as? LoadState.Error)?.error
    ?: (refresh as? LoadState.Error)?.error

fun LoadStates.isLoading() =
    ((append as? LoadState.Loading) ?: refresh as? LoadState.Loading) != null

fun LoadStates.isInError() = exceptionOrNull() != null
fun LazyPagingItems<*>.isInErrorOrLoading() =
    this.loadState.source.isInError() || this.loadState.source.isLoading()