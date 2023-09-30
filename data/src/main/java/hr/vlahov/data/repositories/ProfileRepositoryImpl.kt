package hr.vlahov.data.repositories

import hr.vlahov.data.database.AppDatabase
import hr.vlahov.data.models.converters.toProfileEntity
import hr.vlahov.data.models.converters.toProfiles
import hr.vlahov.domain.models.profile.Profile
import hr.vlahov.domain.repositories.CredentialLocalRepository
import hr.vlahov.domain.repositories.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    private val credentialLocalRepository: CredentialLocalRepository,
) : ProfileRepository {

    override val allProfiles: Flow<List<Profile>> = database.profileDao().getAll()
        .map { it.toProfiles() }

    override suspend fun fetchCurrentProfile(): Profile? {
        return credentialLocalRepository.fetchCurrentProfileName()?.let { name ->
            Profile(name = name)
        }
    }

    override suspend fun createNewProfile(profile: Profile) {
        database.profileDao().create(profile.toProfileEntity())
    }

    override suspend fun doesProfileExist(profileName: String): Boolean =
        database.profileDao().getByName(profileName = profileName) != null

}