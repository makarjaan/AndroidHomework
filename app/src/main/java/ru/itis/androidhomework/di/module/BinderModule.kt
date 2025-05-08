package ru.itis.androidhomework.di.module

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.itis.androidhomework.data.repository.FeatureDataRepositoryImpl
import ru.itis.androidhomework.data.repository.SearchFeaturesRepositoryImpl
import ru.itis.androidhomework.domain.repository.FeatureDataRepository
import ru.itis.androidhomework.domain.repository.SearchFeaturesRepository
import ru.itis.androidhomework.utils.ResManager
import ru.itis.androidhomework.utils.ResManagerImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface BinderModule {

    @Binds
    @Singleton
    fun bindSearchRepository_to_Impl(impl: SearchFeaturesRepositoryImpl): SearchFeaturesRepository

    @Binds
    @Singleton
    fun bindResManager_to_Impl(impl: ResManagerImpl): ResManager

    @Binds
    @Singleton
    fun bindDetailsRepository_to_Impl(impl: FeatureDataRepositoryImpl): FeatureDataRepository

}