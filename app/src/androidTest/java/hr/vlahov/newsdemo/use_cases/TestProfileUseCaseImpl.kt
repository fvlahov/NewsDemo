package hr.vlahov.newsdemo.use_cases

import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.domain.models.profile.Profile
import hr.vlahov.domain.usecases.ProfileUseCase
import hr.vlahov.newsdemo.utils.dummyNewsArticles
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class TestProfileUseCaseImpl @Inject constructor() : ProfileUseCase {
    override val allProfiles: Flow<List<Profile>> = flowOf(emptyList())
    override val likedNewsArticles: Flow<List<NewsArticle>> = flowOf(dummyNewsArticles)

    override suspend fun fetchCurrentProfile(): Profile? {
        return null
    }

    override suspend fun createNewProfile(profile: Profile) {
    }

    override suspend fun doesProfileExist(profileName: String): Boolean {
        return true
    }

    override suspend fun signInAsProfile(profileName: String) {
    }

    override suspend fun signOut() {
    }

    override suspend fun isProfileSelected(): Boolean {
        return false
    }
}