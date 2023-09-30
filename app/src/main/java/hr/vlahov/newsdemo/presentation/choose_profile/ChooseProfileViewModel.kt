package hr.vlahov.newsdemo.presentation.choose_profile

import dagger.hilt.android.lifecycle.HiltViewModel
import hr.vlahov.domain.usecases.ProfileUseCase
import hr.vlahov.newsdemo.base.BaseViewModel
import hr.vlahov.newsdemo.navigation.NavTarget
import hr.vlahov.newsdemo.navigation.navigator.Navigator
import javax.inject.Inject

@HiltViewModel
class ChooseProfileViewModel @Inject constructor(
    private val navigator: Navigator,
    private val profileUseCase: ProfileUseCase,
) : BaseViewModel() {

    val allProfiles = profileUseCase.allProfiles

    fun selectProfile(profileName: String) {
        launchIn {
            profileUseCase.signInAsProfile(profileName)
            navigator.navigateMainTo(NavTarget.NewsModule)
        }
    }

    fun createNewProfile() {
        navigator.navigateMainTo(NavTarget.CreateProfile)
    }
}