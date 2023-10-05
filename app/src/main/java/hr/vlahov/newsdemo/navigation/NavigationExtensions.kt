package hr.vlahov.newsdemo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import hr.vlahov.newsdemo.navigation.navigator.Navigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun CoroutineScope.subscribeToNavigation(
    navController: NavController,
    targetFlow: Flow<Navigator.NavigationIntent>,
) {
    targetFlow.onEach { navIntent ->
        when (navIntent) {
            is Navigator.NavigationIntent.NavigateTo -> {
                navController.navigate(navIntent.navTarget.destination) {
                    popUpTo(navIntent.navTarget.destination)
                    navIntent.options.invoke(this)
                }
            }

            is Navigator.NavigationIntent.PopBackStack -> {
                if (navIntent.popBackStackTo == null)
                    navController.popBackStack()
                else
                    navController.popBackStack(
                        navIntent.popBackStackTo.destination,
                        navIntent.inclusive
                    )
            }
        }

    }.launchIn(this)
}

fun NavOptionsBuilder.popUpToInclusive(route: String) = popUpTo(route) {
    inclusive = true
}