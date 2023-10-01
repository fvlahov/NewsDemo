package hr.vlahov.newsdemo.utils

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey

fun <T : Any> LazyListScope.items(
    items: LazyPagingItems<T>,
    key: ((T) -> Any)? = null,
    contentType: ((T) -> Any)? = null,
    itemContent: @Composable LazyItemScope.(T) -> Unit,
) {
    items(
        items.itemCount,
        key = items.itemKey(key),
        contentType = items.itemContentType(contentType)
    ) loop@{ i ->
        val item = items[i] ?: return@loop
        itemContent(item)
    }
}

fun LazyPagingItems<*>.isLoading() =
    this.loadState.append is LoadState.Loading || this.isRefreshing()

fun LazyPagingItems<*>.isRefreshing() = this.loadState.refresh is LoadState.Loading