package ru.itis.androidhomework.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.itis.androidhomework.data.repository.TripRepositoryImpl
import ru.itis.androidhomework.domain.repository.TripRepository
import ru.itis.androidhomework.utils.ResManager
import ru.itis.androidhomework.utils.ResManagerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BinderModule {

    @Binds
    @Singleton
    fun bindTripRepository_to_Impl(impl: TripRepositoryImpl): TripRepository

    @Binds
    @Singleton
    fun bindResManager_to_Impl(impl: ResManagerImpl): ResManager

}