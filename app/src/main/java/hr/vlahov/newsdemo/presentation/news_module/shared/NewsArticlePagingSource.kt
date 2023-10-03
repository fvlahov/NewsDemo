package hr.vlahov.newsdemo.presentation.news_module.shared

import androidx.paging.PagingSource
import androidx.paging.PagingState
import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.domain.models.news.NewsArticlePage
import okio.IOException
import retrofit2.HttpException

class NewsArticlePagingSource(
    private val fetchNewsArticles: suspend (page: Int) -> NewsArticlePage,
) : PagingSource<Int, NewsArticle>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsArticle> {
        val currentPage = params.key ?: 1

        return try {
            val newsArticlePage = fetchNewsArticles(currentPage)
            LoadResult.Page(
                data = newsArticlePage.items,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (newsArticlePage.items.isEmpty()) null else currentPage + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {

            //426 - Upgrade required, meaning we've reached the page limit from the API
            //429 - Upgrade required, meaning we've reached the limit of requests (100 requests in 24 hours)
            if (exception.code() == 429)
                return LoadResult.Error(TooManyRequestsException)

            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NewsArticle>): Int? {
        return state.anchorPosition
    }

}