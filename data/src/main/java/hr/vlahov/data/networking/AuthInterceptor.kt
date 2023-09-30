package hr.vlahov.data.networking

import hr.vlahov.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request()
            .url
            .newBuilder()
            .addQueryParameter(
                "apiKey",
                BuildConfig.NEWS_API_KEY
            )
            .build()
        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()

        return chain.proceed(request)


        //Logic for handling access/refresh tokens
        /*        val accessToken = credentialLocalRepository.fetchAccessToken()

                val response = chain.proceed(request.withAccessToken(accessToken))

                if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    val refreshToken =
                        credentialLocalRepository.fetchRefreshToken()
                    val newAccessToken = runBlocking {
                        authRepository.fetchAccessToken(
                            refreshToken = refreshToken
                        ).also(credentialLocalRepository::saveAccessToken)
                    } ?: return chain.proceed(request)

                    response.close()
                    return chain.proceed(request.withAccessToken(newAccessToken))
                } else
                    return response*/
    }

    /*    private fun Request.withAccessToken(accessToken: String?): Request =
            newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()*/
}