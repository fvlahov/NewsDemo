package hr.vlahov.newsdemo.navigation

sealed class NavTarget(val label: String) {
    object ChooseProfile : NavTarget("chooseprofilescreen")
    object CreateProfile : NavTarget("createprofilescreen")
    object NewsMain : NavTarget(ModuleRoutes.NewsMainModule.label)

    sealed class SecondFeatureModule {
        data class SecondModuleWithParams(val id: String) :
            NavTarget("${ModuleRoutes.SecondModule}/$id")

        data class SecondFeatureWithParams(val id: String) :
            NavTarget("${ModuleRoutes.SecondFeature.label}/$id")
    }
}

enum class ModuleRoutes(val label: String) {
    ChooseProfileModule("chooseprofilemodule"),
    NewsMainModule("newsmainmodule"),
    SecondModule("secondmodule"),
    SecondFeature("secondfeature"),
}