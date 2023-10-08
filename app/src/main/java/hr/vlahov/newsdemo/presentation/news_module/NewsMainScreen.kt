package hr.vlahov.newsdemo.presentation.news_module

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import hr.vlahov.domain.models.news.NewsSource
import hr.vlahov.newsdemo.R
import hr.vlahov.newsdemo.navigation.ModuleRoutes
import hr.vlahov.newsdemo.navigation.NavTarget
import hr.vlahov.newsdemo.navigation.subscribeToNavigation
import hr.vlahov.newsdemo.presentation.news_module.everything.AllNewsScreen
import hr.vlahov.newsdemo.presentation.news_module.liked_news_articles.LikedNewsArticlesScreen
import hr.vlahov.newsdemo.presentation.news_module.profile.ProfileScreen
import hr.vlahov.newsdemo.presentation.news_module.shared.NewsFilters
import hr.vlahov.newsdemo.presentation.news_module.single_news_article.SingleNewsArticleScreen
import hr.vlahov.newsdemo.presentation.news_module.top_headlines.TopHeadlinesScreen
import hr.vlahov.newsdemo.ui.theme.NewsDemoTheme
import hr.vlahov.newsdemo.utils.dummyNewsSources

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

    BackToExitHandler()

    val currentBackstackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackstackEntry?.destination?.route

    val newsSources = viewModel.newsSources.collectAsStateWithLifecycle(emptyList()).value
    val selectedNewsSources =
        viewModel.selectedNewsSources.collectAsStateWithLifecycle(emptyList()).value

    NewsMainBody(
        currentRoute = currentRoute,
        allNewsSources = newsSources,
        selectedNewsSources = selectedNewsSources,
        onNavigateTo = { newsNavItem ->
            navController.navigate(newsNavItem.destinationName) {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        },
        onSearchQueryCommitted = viewModel::setSearchQuery,
        onDateRangeConfirmed = viewModel::setDateRange,
        onNewsOrderChanged = viewModel::setNewsOrder,
        onNewsSourcesChanged = viewModel::selectNewsSources
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
    allNewsSources: List<NewsSource>,
    selectedNewsSources: List<NewsSource>,
    onNewsSourcesChanged: (selectedNewsSources: List<NewsSource>) -> Unit,
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
            if (listOf(
                    NavTarget.NewsModule.NewsNavItems.TOP_HEADLINES.destinationName,
                    NavTarget.NewsModule.NewsNavItems.ALL_NEWS.destinationName
                ).contains(currentRoute)
            )
                NewsMainTopAppBar(
                    allNewsSources = allNewsSources,
                    selectedNewsSources = selectedNewsSources,
                    onSearchQueryCommitted = onSearchQueryCommitted,
                    onDateRangeConfirmed = onDateRangeConfirmed,
                    onNewsOrderChanged = onNewsOrderChanged,
                    onNewsSourcesChanged = onNewsSourcesChanged
                )
        },
        modifier = Modifier.semantics { contentDescription = "News main" },
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

        composable(
            route = NavTarget.NewsModule.LikedNewsArticles.destination,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = enterTransition,
            popExitTransition = exitTransition
        ) {
            LikedNewsArticlesScreen()
        }

        composable(
            route = NavTarget.NewsModule.SingleNewsArticle.Info.Destination,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = enterTransition,
            popExitTransition = exitTransition,
            arguments = listOf(navArgument(NavTarget.NewsModule.SingleNewsArticle.Info.OriginalNewsArticleParam) {
                type = NavType.StringType
            })
        ) {
            SingleNewsArticleScreen()
        }
    }
}

@Composable
private fun BackToExitHandler() {
    val currentBackCount = rememberSaveable { mutableIntStateOf(0) }
    val context = LocalContext.current
    BackHandler {
        currentBackCount.intValue = currentBackCount.intValue + 1
        if (currentBackCount.intValue >= 2) {
            (context as? Activity)?.finish()
            return@BackHandler
        }
        Toast.makeText(context, R.string.press_back_again_to_leave, Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            currentBackCount.intValue = 0
        }, 2000)
    }
}

@Composable
@Preview
private fun NewsMainScreenPreview() {
    NewsDemoTheme {
        NewsMainBody(
            currentRoute = NavTarget.NewsModule.NewsNavItems.TOP_HEADLINES.destinationName,
            allNewsSources = dummyNewsSources,
            selectedNewsSources = dummyNewsSources.take(2),
            onNavigateTo = {},
            onSearchQueryCommitted = {},
            onDateRangeConfirmed = { _, _ -> },
            onNewsOrderChanged = {},
            onNewsSourcesChanged = {},
            content = { }
        )
    }
}