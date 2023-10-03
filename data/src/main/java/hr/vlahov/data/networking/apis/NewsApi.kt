package hr.vlahov.data.networking.apis

import hr.vlahov.data.models.api.news.ApiNewsCategory
import hr.vlahov.data.models.api.news.ApiNewsListResponse
import hr.vlahov.data.models.api.news.ApiNewsSourceResponse
import hr.vlahov.data.networking.NetworkContract
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi : ApiService {

    @GET(NetworkContract.News.TOP_HEADLINES)
    suspend fun fetchTopHeadlines(
        @Query("q") keyword: String?,
        @Query("country") country: String,
        @Query("category") category: ApiNewsCategory?,
        @Query("sources") sources: String?,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
    ): ApiNewsListResponse

    @GET(NetworkContract.News.EVERYTHING)
    suspend fun fetchEverything(
        @Query("q") keyword: String?,
        @Query("from") from: String?,
        @Query("to") to: String?,
        @Query("sources") sources: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
    ): ApiNewsListResponse

    @GET(NetworkContract.News.SOURCES)
    suspend fun fetchNewsSources(
        @Query("language") language: String,
    ): ApiNewsSourceResponse
}