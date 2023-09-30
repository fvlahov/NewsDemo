package hr.vlahov.domain.repositories

interface CredentialLocalRepository {
    fun fetchCurrentProfileName(): String?

    fun saveCurrentProfileName(profileName: String)
}