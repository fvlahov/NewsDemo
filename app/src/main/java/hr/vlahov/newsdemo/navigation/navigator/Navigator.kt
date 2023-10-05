package hr.vlahov.newsdemo.navigation.navigator

import androidx.navigation.NavOptionsBuilder
import hr.vlahov.newsdemo.navigation.NavTarget
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor() {
    private val _mainNavigationFlow: MutableSharedFlow<NavigationIntent> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val mainNavigationFlow = _mainNavigationFlow.asSharedFlow()

    private val _newsNavigationFlow: MutableSharedFlow<NavigationIntent> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val newsNavigationFlow = _newsNavigationFlow.asSharedFlow()

    fun navigateMainTo(navTarget: NavTarget, options: NavOptionsBuilder.() -> Unit = {}) {
        _mainNavigationFlow.tryEmit(
            NavigationIntent.NavigateTo(
                navTarget = navTarget,
                options = options
            )
        )
    }

    fun navigateNewsTo(navTarget: NavTarget, options: NavOptionsBuilder.() -> Unit = {}) {
        _newsNavigationFlow.tryEmit(
            NavigationIntent.NavigateTo(
                navTarget = navTarget,
                options = options
            )
        )
    }

    fun popNewsBackStack(popBackStackTo: NavTarget? = null, inclusive: Boolean = true) {
        _newsNavigationFlow.tryEmit(
            NavigationIntent.PopBackStack(popBackStackTo, inclusive)
        )
    }

    sealed class NavigationIntent {
        data class PopBackStack(val popBackStackTo: NavTarget?, val inclusive: Boolean) :
            NavigationIntent()

        data class NavigateTo(
            val navTarget: NavTarget,
            val options: NavOptionsBuilder.() -> Unit = {},
        ) : NavigationIntent()
    }
}