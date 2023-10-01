package hr.vlahov.data.repositories

import android.content.SharedPreferences
import androidx.core.content.edit
import hr.vlahov.domain.repositories.CredentialLocalRepository
import javax.inject.Inject

class CredentialLocalRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) : CredentialLocalRepository {
    override fun fetchCurrentProfileName(): String? =
        sharedPreferences.getString(PROFILE_NAME_PREF_KEY, null)


    override fun saveCurrentProfileName(profileName: String?) =
        sharedPreferences.edit {
            putString(PROFILE_NAME_PREF_KEY, profileName)
        }

    companion object {
        private const val PROFILE_NAME_PREF_KEY = "pref_profile_name"
    }
}