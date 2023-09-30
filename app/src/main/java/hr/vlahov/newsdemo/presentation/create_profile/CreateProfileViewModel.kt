package hr.vlahov.newsdemo.presentation.create_profile

import dagger.hilt.android.lifecycle.HiltViewModel
import hr.vlahov.domain.models.profile.Profile
import hr.vlahov.domain.usecases.ProfileUseCase
import hr.vlahov.newsdemo.R
import hr.vlahov.newsdemo.base.BaseViewModel
import hr.vlahov.newsdemo.base.errors.GenericError
import hr.vlahov.newsdemo.navigation.NavTarget
import hr.vlahov.newsdemo.navigation.navigator.Navigator
import javax.inject.Inject

@HiltViewModel
class CreateProfileViewModel @Inject constructor(
    private val navigator: Navigator,
    private val profileUseCase: ProfileUseCase,
) : BaseViewModel() {

    fun confirm(profileName: String) {
        launchIn {
            if (profileUseCase.doesProfileExist(profileName)) {
                errors.emitError(GenericError(R.string.user_already_exists))
                return@launchIn
            }

            profileUseCase.createNewProfile(Profile(name = profileName))
            navigator.navigateTo(NavTarget.NewsMain)
        }
    }
}