package hr.vlahov.newsdemo.presentation.news_module

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import hr.vlahov.newsdemo.navigation.ModuleRoutes
import hr.vlahov.newsdemo.navigation.NavTarget
import hr.vlahov.newsdemo.navigation.addNewsArticlesGraph
import hr.vlahov.newsdemo.navigation.subscribeToNavigation

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

    Scaffold(
        bottomBar = {
            val currentBackstackEntry = navController.currentBackStackEntryAsState().value
            val currentRoute = currentBackstackEntry?.destination?.route
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ) {
                NavTarget.NewsModule.NewsNavItems.values().forEach { newsNavItem ->
                    NavigationBarItem(
                        selected = currentRoute == newsNavItem.destinationName,
                        onClick = { navController.navigate(newsNavItem.destinationName) },
                        icon = {
                            Icon(
                                painter = painterResource(id = newsNavItem.iconId),
                                contentDescription = "Nav item icon"
                            )
                        },
                        label = { Text(text = stringResource(id = newsNavItem.labelId)) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    )

                }
            }
        }
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