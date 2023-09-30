package hr.vlahov.data.networking

object NetworkContract {
    const val BASE_URL = "https://newsapi.org/v2"
    const val CONNECT_TIMEOUT_SECONDS = 10L
    const val READ_TIMEOUT_SECONDS = 30L
    const val WRITE_TIMEOUT_SECONDS = 120L

    object News {
        const val TOP_HEADLINES = "top-headlines"
        const val EVERYTHING = "everything"
    }
}