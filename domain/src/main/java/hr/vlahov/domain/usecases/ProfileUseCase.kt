package hr.vlahov.domain.usecases

import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.domain.models.profile.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileUseCase {
    val allProfiles: Flow<List<Profile>>

    val likedNewsArticles: Flow<List<NewsArticle>>

    suspend fun fetchCurrentProfile(): Profile?

    suspend fun createNewProfile(profile: Profile)

    suspend fun doesProfileExist(profileName: String): Boolean

    suspend fun signInAsProfile(profileName: String)

    suspend fun signOut()

    suspend fun isProfileSelected(): Boolean
}