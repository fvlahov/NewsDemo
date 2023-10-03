package hr.vlahov.newsdemo.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import hr.vlahov.newsdemo.R

sealed class NavTarget(val destination: String) {
    object Splash : NavTarget("splashscreen")
    object ChooseProfile : NavTarget("chooseprofilescreen")
    object CreateProfile : NavTarget("createprofilescreen")

    object NewsModule : NavTarget("newsmainscreen") {
        enum class NewsNavItems(
            @StringRes val labelId: Int,
            @DrawableRes val iconId: Int,
            val destinationName: String,
        ) {
            TOP_HEADLINES(
                labelId = R.string.top_stories,
                iconId = R.drawable.ic_local_fire,
                destinationName = "topheadlinesscreen"
            ),
            ALL_NEWS(
                labelId = R.string.news,
                iconId = R.drawable.ic_newspaper,
                destinationName = "allnewsscreen"
            ),
            PROFILE(
                labelId = R.string.profile,
                iconId = R.drawable.ic_person,
                destinationName = "profilescreen"
            ),
        }

        object LikedNewsArticles : NavTarget("likednewsarticlesscreen")
    }
}

enum class ModuleRoutes(val route: String) {
    MainModule("mainmodule"),
    NewsMainModule("newsmainmodule"),
}