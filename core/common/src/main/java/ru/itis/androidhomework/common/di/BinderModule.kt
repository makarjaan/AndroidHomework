package ru.itis.androidhomework.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.itis.androidhomework.common.ResManager
import ru.itis.androidhomework.common.ResManagerImpl
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface BinderModule {
    @Binds
    @Singleton
    fun bindResToImpl(impl: ResManagerImpl) : ResManager
}
