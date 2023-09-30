package hr.vlahov.newsdemo.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hr.vlahov.data.di.PreferencesModule
import hr.vlahov.data.di.RepositoryModule
import hr.vlahov.data.di.UseCaseModule

@Module(
    includes = [
        RepositoryModule::class,
        UseCaseModule::class,
        PreferencesModule::class,
    ]
)
@InstallIn(SingletonComponent::class)
object AppModule