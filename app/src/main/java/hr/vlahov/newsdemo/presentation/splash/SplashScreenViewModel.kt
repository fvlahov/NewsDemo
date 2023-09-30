package hr.vlahov.newsdemo.presentation.splash

import dagger.hilt.android.lifecycle.HiltViewModel
import hr.vlahov.domain.usecases.ProfileUseCase
import hr.vlahov.newsdemo.base.BaseViewModel
import hr.vlahov.newsdemo.navigation.NavTarget
import hr.vlahov.newsdemo.navigation.navigator.Navigator
import hr.vlahov.newsdemo.navigation.popUpToInclusive
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    private val navigator: Navigator,
) : BaseViewModel() {
    init {
        launchIn {
            delay(2000)
            //Navigate to CreateNewProfile if list of profiles is empty
            profileUseCase.allProfiles.stateIn(this).value.takeIf { it.isEmpty() }?.run {
                navigateFromSplashScreen(NavTarget.CreateProfile)
            }

            if (profileUseCase.isProfileSelected())
                navigateFromSplashScreen(NavTarget.NewsModule)
            else
                navigateFromSplashScreen(NavTarget.ChooseProfile)
        }
    }

    private fun navigateFromSplashScreen(navTarget: NavTarget) {
        navigator.navigateMainTo(navTarget) {
            popUpToInclusive(NavTarget.Splash.destination)
        }
    }

}