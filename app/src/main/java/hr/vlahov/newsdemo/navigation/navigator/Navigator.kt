package hr.vlahov.newsdemo.navigation.navigator

import hr.vlahov.newsdemo.navigation.NavTarget
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor() {
    private val _navigationFlow: MutableSharedFlow<NavTarget> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val navigationFlow = _navigationFlow.asSharedFlow()

    fun navigateTo(navTarget: NavTarget) {
        _navigationFlow.tryEmit(navTarget)
    }
}