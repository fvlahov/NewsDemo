package hr.vlahov.newsdemo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hr.vlahov.data.di.PreferencesModule
import hr.vlahov.data.di.RepositoryModule
import hr.vlahov.data.di.UseCaseModule
import hr.vlahov.newsdemo.presentation.news_module.shared.NewsFilters
import hr.vlahov.newsdemo.presentation.news_module.shared.NewsFiltersImpl

@Module(
    includes = [
        RepositoryModule::class,
        UseCaseModule::class,
        PreferencesModule::class,
    ]
)
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideNewsFilters(impl: NewsFiltersImpl): NewsFilters = impl
}