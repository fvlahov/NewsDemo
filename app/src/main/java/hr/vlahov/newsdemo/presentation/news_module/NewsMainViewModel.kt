package hr.vlahov.newsdemo.presentation.news_module

import dagger.hilt.android.lifecycle.HiltViewModel
import hr.vlahov.newsdemo.base.BaseViewModel
import hr.vlahov.newsdemo.navigation.navigator.Navigator
import javax.inject.Inject

@HiltViewModel
class NewsMainViewModel @Inject constructor(
    val navigator: Navigator,
) : BaseViewModel() {
}