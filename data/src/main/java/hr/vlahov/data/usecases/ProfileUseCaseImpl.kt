package hr.vlahov.data.usecases

import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.domain.models.profile.Profile
import hr.vlahov.domain.repositories.CredentialLocalRepository
import hr.vlahov.domain.repositories.ProfileRepository
import hr.vlahov.domain.usecases.ProfileUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProfileUseCaseImpl @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val credentialLocalRepository: CredentialLocalRepository,
) : ProfileUseCase {
    override val allProfiles: Flow<List<Profile>> = profileRepository.allProfiles

    override val likedNewsArticles: Flow<List<NewsArticle>> = profileRepository.likedNewsArticles

    override suspend fun fetchCurrentProfile(): Profile? =
        profileRepository.fetchCurrentProfile()

    override suspend fun createNewProfile(profile: Profile) =
        profileRepository.createNewProfile(profile)

    override suspend fun doesProfileExist(profileName: String): Boolean =
        profileRepository.doesProfileExist(profileName)

    override suspend fun signInAsProfile(profileName: String) {
        credentialLocalRepository.saveCurrentProfileName(profileName)
    }

    override suspend fun signOut() {
        credentialLocalRepository.saveCurrentProfileName(null)
    }

    override suspend fun isProfileSelected(): Boolean =
        credentialLocalRepository.fetchCurrentProfileName() != null

}