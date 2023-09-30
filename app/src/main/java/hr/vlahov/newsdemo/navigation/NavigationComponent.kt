package hr.vlahov.newsdemo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import hr.vlahov.newsdemo.navigation.navigator.Navigator
import hr.vlahov.newsdemo.presentation.choose_profile.ChooseProfileScreen
import hr.vlahov.newsdemo.presentation.create_profile.CreateProfileScreen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun NavigationComponent(
    navController: NavHostController,
    navigator: Navigator,
) {
    /**
     * With LaunchedEffect we create a CoroutineScope that is started as soon as our composable component is createdtestSTATE
     * and canceled as soon as the composition is removed.
     * As a result, whenever Navigator.navigateTo() is called, this snippet listens to it and performs the actual transition.
     */
    LaunchedEffect(Unit) {
        navigator.navigationFlow.onEach {
            navController.navigate(it.label) {
                popUpTo(it.label)
            }
        }.launchIn(this)
    }

    // Navigation Directions
    NavHost(
        navController = navController,
        startDestination = ModuleRoutes.ChooseProfileModule.label
    ) {
        addMainGraph()
        addNewsArticlesGraph()
    }
}

fun NavGraphBuilder.addMainGraph() {
    navigation(
        startDestination = NavTarget.ChooseProfile.label,
        route = ModuleRoutes.ChooseProfileModule.label
    ) {
        composable(NavTarget.ChooseProfile.label) {
            ChooseProfileScreen()
        }
        composable(NavTarget.CreateProfile.label) {
            CreateProfileScreen()
        }
    }
}

fun NavGraphBuilder.addNewsArticlesGraph() {

}