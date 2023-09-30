package hr.vlahov.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import hr.vlahov.data.usecases.NewsUseCaseImpl
import hr.vlahov.data.usecases.ProfileUseCaseImpl
import hr.vlahov.domain.usecases.NewsUseCase
import hr.vlahov.domain.usecases.ProfileUseCase

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideProfileUseCase(impl: ProfileUseCaseImpl): ProfileUseCase = impl

    @Provides
    fun provideNewsUseCase(impl: NewsUseCaseImpl): NewsUseCase = impl
}