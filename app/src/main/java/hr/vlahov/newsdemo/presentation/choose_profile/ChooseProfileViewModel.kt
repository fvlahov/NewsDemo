package hr.vlahov.newsdemo.presentation.choose_profile

import dagger.hilt.android.lifecycle.HiltViewModel
import hr.vlahov.domain.usecases.ProfileUseCase
import hr.vlahov.newsdemo.base.BaseViewModel
import hr.vlahov.newsdemo.navigation.NavTarget
import hr.vlahov.newsdemo.navigation.navigator.Navigator
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ChooseProfileViewModel @Inject constructor(
    private val navigator: Navigator,
    private val profileUseCase: ProfileUseCase,
) : BaseViewModel() {

    val allProfiles = profileUseCase.allProfiles

    init {
        launchIn {
            //Navigate to CreateNewProfile if list of profiles is empty
            allProfiles.stateIn(this).value.takeIf { it.isEmpty() }?.run {
                createNewProfile()
            }
        }
    }

    fun selectProfile(profileName: String) {
        launchIn {
            profileUseCase.signInAsProfile(profileName)
            navigator.navigateTo(NavTarget.NewsMain)
        }
    }

    fun createNewProfile() {
        navigator.navigateTo(NavTarget.CreateProfile)
    }
}