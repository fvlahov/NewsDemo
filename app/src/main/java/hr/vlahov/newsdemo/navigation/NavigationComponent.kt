package hr.vlahov.newsdemo.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
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
import hr.vlahov.newsdemo.presentation.news_module.NewsMainScreen
import hr.vlahov.newsdemo.presentation.news_module.everything.AllNewsScreen
import hr.vlahov.newsdemo.presentation.news_module.profile.ProfileScreen
import hr.vlahov.newsdemo.presentation.news_module.top_headlines.TopHeadlinesScreen
import hr.vlahov.newsdemo.presentation.splash.SplashScreen

@Composable
fun NavigationComponent(
    navController: NavHostController,
    navigator: Navigator,
) {
    LaunchedEffect(Unit) {
        subscribeToNavigation(
            navController = navController,
            targetFlow = navigator.mainNavigationFlow
        )
    }

    // Navigation Directions
    NavHost(
        navController = navController,
        startDestination = ModuleRoutes.MainModule.route
    ) {
        addMainGraph()
    }
}

fun NavGraphBuilder.addMainGraph() {
    navigation(
        startDestination = NavTarget.Splash.destination,
        route = ModuleRoutes.MainModule.route
    ) {
        composable(NavTarget.Splash.destination) {
            SplashScreen()
        }
        composable(NavTarget.ChooseProfile.destination) {
            ChooseProfileScreen()
        }
        composable(
            route = NavTarget.CreateProfile.destination,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(700)
                )
            }) {
            CreateProfileScreen()
        }
        composable(NavTarget.NewsModule.destination) {
            NewsMainScreen()
        }
    }
}

fun NavGraphBuilder.addNewsArticlesGraph() {
    navigation(
        startDestination = NavTarget.NewsModule.NewsNavItems.TOP_HEADLINES.destinationName,
        route = ModuleRoutes.NewsMainModule.route
    ) {

        composable(NavTarget.NewsModule.NewsNavItems.TOP_HEADLINES.destinationName) {
            TopHeadlinesScreen()
        }
        composable(NavTarget.NewsModule.NewsNavItems.ALL_NEWS.destinationName) {
            AllNewsScreen()
        }
        composable(NavTarget.NewsModule.NewsNavItems.PROFILE.destinationName) {
            ProfileScreen()
        }
    }
}