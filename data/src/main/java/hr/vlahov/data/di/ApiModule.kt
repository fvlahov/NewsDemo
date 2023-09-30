package hr.vlahov.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hr.vlahov.data.BuildConfig
import hr.vlahov.data.networking.AnonymousInterceptor
import hr.vlahov.data.networking.AuthInterceptor
import hr.vlahov.data.networking.NetworkContract
import hr.vlahov.data.networking.apis.ApiService
import hr.vlahov.data.networking.apis.NewsApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideGson(): Gson = GsonBuilder().setLenient().create()


    @Provides
    fun provideAnonymousInterceptor(impl: AnonymousInterceptor): Interceptor = impl

    @Provides
    fun provideAuthInterceptor(impl: AuthInterceptor): Interceptor = impl

    private fun provideOkHttpClient(requestInterceptor: Interceptor): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)


        return OkHttpClient
            .Builder()
            .apply {
                if (BuildConfig.DEBUG)
                    addInterceptor(loggingInterceptor)
            }
            .connectTimeout(NetworkContract.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(NetworkContract.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(NetworkContract.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(requestInterceptor)
            .build()
    }


    @Provides
    fun provideAuthOkHttpClient(
        requestInterceptor: AuthInterceptor,
    ): OkHttpClient {
        return provideOkHttpClient(requestInterceptor)
    }


    private fun provideAnonymousOkHttpClient(
        requestInterceptor: AnonymousInterceptor,
    ): OkHttpClient {
        return provideOkHttpClient(requestInterceptor)
    }

    @Provides
    fun provideNewsApi(gson: Gson, authClient: OkHttpClient) =
        provideRetrofit<NewsApi>(gson, authClient)

    private inline fun <reified T : ApiService> provideRetrofit(
        gson: Gson,
        client: OkHttpClient,
    ): T =
        Retrofit.Builder()
            .baseUrl(NetworkContract.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(T::class.java)
}