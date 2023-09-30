package hr.vlahov.newsdemo

import dagger.hilt.android.lifecycle.HiltViewModel
import hr.vlahov.newsdemo.base.BaseViewModel
import hr.vlahov.newsdemo.navigation.navigator.Navigator
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val navigator: Navigator,
) : BaseViewModel()