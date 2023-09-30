package hr.vlahov.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import hr.vlahov.data.repositories.CredentialLocalRepositoryImpl
import hr.vlahov.data.repositories.NewsRepositoryImpl
import hr.vlahov.data.repositories.ProfileRepositoryImpl
import hr.vlahov.domain.repositories.CredentialLocalRepository
import hr.vlahov.domain.repositories.NewsRepository
import hr.vlahov.domain.repositories.ProfileRepository

@Module(
    includes = [
        RoomModule::class,
        ApiModule::class,
    ]
)
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    fun provideProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository = impl

    @Provides
    fun provideNewsRepository(impl: NewsRepositoryImpl): NewsRepository = impl

    @Provides
    fun provideCredentialLocalRepository(impl: CredentialLocalRepositoryImpl): CredentialLocalRepository =
        impl
}