package hr.vlahov.newsdemo.presentation.news_module

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import hr.vlahov.newsdemo.navigation.ModuleRoutes
import hr.vlahov.newsdemo.navigation.NavTarget
import hr.vlahov.newsdemo.navigation.subscribeToNavigation
import hr.vlahov.newsdemo.presentation.news_module.everything.AllNewsScreen
import hr.vlahov.newsdemo.presentation.news_module.profile.ProfileScreen
import hr.vlahov.newsdemo.presentation.news_module.shared.NewsFilters
import hr.vlahov.newsdemo.presentation.news_module.top_headlines.TopHeadlinesScreen
import hr.vlahov.newsdemo.ui.theme.NewsDemoTheme

@Composable
fun NewsMainScreen(
    viewModel: NewsMainViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        subscribeToNavigation(
            navController = navController,
            targetFlow = viewModel.navigator.newsNavigationFlow
        )
    }
    val currentBackstackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackstackEntry?.destination?.route

    NewsMainBody(
        currentRoute = currentRoute,
        onNavigateTo = { newsNavItem ->
            try {
                //GetBackStackEntry throws exception if there is no backstack entry with route
                navController.getBackStackEntry(newsNavItem.destinationName).destination.route?.also {
                    navController.popBackStack(route = it, inclusive = false)
                }
            } catch (e: Exception) {
                navController.navigate(newsNavItem.destinationName) {
                    this.launchSingleTop = true
                }
            }
        },
        onSearchQueryCommitted = viewModel::setSearchQuery,
        onDateRangeConfirmed = viewModel::setDateRange,
        onNewsOrderChanged = viewModel::setNewsOrder,
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = ModuleRoutes.NewsMainModule.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            addNewsArticlesGraph()
        }
    }
}

@Composable
private fun NewsMainBody(
    currentRoute: String?,
    onNavigateTo: (NavTarget.NewsModule.NewsNavItems) -> Unit,
    onSearchQueryCommitted: (String?) -> Unit,
    onDateRangeConfirmed: (Long?, Long?) -> Unit,
    onNewsOrderChanged: (NewsFilters.OrderBy) -> Unit,
    content: @Composable (paddingValues: PaddingValues) -> Unit,
) {
    Scaffold(
        bottomBar = {
            NewsMainBottomNavigation(
                currentRoute = currentRoute,
                onNavigateTo = onNavigateTo
            )
        },
        topBar = {
            AnimatedVisibility(
                visible = currentRoute != NavTarget.NewsModule.NewsNavItems.PROFILE.destinationName,
                enter = fadeIn(animationSpec = tween(delayMillis = 250)),
                exit = fadeOut(animationSpec = tween(delayMillis = 250))
            ) {
                NewsMainTopAppBar(
                    onSearchQueryCommitted = onSearchQueryCommitted,
                    onDateRangeConfirmed = onDateRangeConfirmed,
                    onNewsOrderChanged = onNewsOrderChanged
                )
            }

        },
        content = content
    )
}

@Composable
private fun NewsMainBottomNavigation(
    currentRoute: String?,
    onNavigateTo: (NavTarget.NewsModule.NewsNavItems) -> Unit,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
    ) {
        NavTarget.NewsModule.NewsNavItems.values().forEach { newsNavItem ->
            NavigationBarItem(
                selected = currentRoute == newsNavItem.destinationName,
                onClick = {
                    onNavigateTo(newsNavItem)
                },
                icon = {
                    Icon(
                        painter = painterResource(id = newsNavItem.iconId),
                        tint = Color(0xFFF3F9FF),
                        contentDescription = "Nav item icon"
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = newsNavItem.labelId),
                        color = Color(0xFFE6E0E9)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            )
        }
    }
}

fun NavGraphBuilder.addNewsArticlesGraph() {
    navigation(
        startDestination = NavTarget.NewsModule.NewsNavItems.TOP_HEADLINES.destinationName,
        route = ModuleRoutes.NewsMainModule.route
    ) {
        val animationSpec: FiniteAnimationSpec<Float> = tween(500)
        val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
            {
                fadeIn(
                    animationSpec = animationSpec
                )
            }
        val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
            {
                fadeOut(
                    animationSpec = animationSpec
                )
            }


        composable(
            route = NavTarget.NewsModule.NewsNavItems.TOP_HEADLINES.destinationName,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = enterTransition,
            popExitTransition = exitTransition
        ) {
            TopHeadlinesScreen()
        }
        composable(
            route = NavTarget.NewsModule.NewsNavItems.ALL_NEWS.destinationName,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = enterTransition,
            popExitTransition = exitTransition
        ) {
            AllNewsScreen()
        }
        composable(
            route = NavTarget.NewsModule.NewsNavItems.PROFILE.destinationName,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = enterTransition,
            popExitTransition = exitTransition
        ) {
            ProfileScreen()
        }
    }
}

@Composable
@Preview
private fun NewsMainScreenPreview() {
    NewsDemoTheme {
        NewsMainBody(
            currentRoute = NavTarget.NewsModule.NewsNavItems.TOP_HEADLINES.destinationName,
            onNavigateTo = {},
            onSearchQueryCommitted = {},
            onDateRangeConfirmed = { _, _ -> },
            onNewsOrderChanged = {},
            content = { }
        )
    }
}