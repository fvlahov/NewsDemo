package hr.vlahov.domain.repositories

import hr.vlahov.domain.models.profile.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    val allProfiles: Flow<List<Profile>>

    suspend fun fetchCurrentProfile(): Profile?

    suspend fun createNewProfile(profile: Profile)

    suspend fun doesProfileExist(profileName: String): Boolean
}