package hr.vlahov.newsdemo.presentation.news_module.profile

import dagger.hilt.android.lifecycle.HiltViewModel
import hr.vlahov.domain.models.news.NewsArticle
import hr.vlahov.domain.models.profile.Profile
import hr.vlahov.domain.usecases.ProfileUseCase
import hr.vlahov.newsdemo.base.BaseViewModel
import hr.vlahov.newsdemo.navigation.NavTarget
import hr.vlahov.newsdemo.navigation.navigator.Navigator
import hr.vlahov.newsdemo.navigation.popUpToInclusive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase,
    private val navigator: Navigator,
) : BaseViewModel() {

    private val _profile = MutableStateFlow<Profile?>(null)
    val profile = _profile.asStateFlow()

    val likedArticles = profileUseCase.likedNewsArticles

    init {
        launchWithProgressIn {
            profileUseCase.fetchCurrentProfile().also { _profile.emit(it) }
        }
    }

    fun changeProfile() {
        launchWithProgressIn {
            profileUseCase.signOut()
            navigator.navigateMainTo(NavTarget.ChooseProfile) {
                popUpToInclusive(NavTarget.NewsModule.destination)
            }
        }
    }

    fun navigateToAllLikedArticles() {
        navigator.navigateNewsTo(NavTarget.NewsModule.LikedNewsArticles)
    }

    fun navigateToLikedArticle(newsArticle: NewsArticle) {
        navigator.navigateNewsTo(
            NavTarget.NewsModule.SingleNewsArticle.WithNewsArticleUrl(
                newsArticle.originalArticleUrl
            )
        )
    }
}