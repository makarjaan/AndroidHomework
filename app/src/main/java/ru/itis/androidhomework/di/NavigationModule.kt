package ru.itis.androidhomework.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.itis.androidhomework.nav.NavImpl
import ru.itis.androidhomework.nav.NavMainImpl
import ru.itis.androidhomework.navigation.Nav
import ru.itis.androidhomework.navigation.NavMain
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @Binds
    @Singleton
    fun bindNavToImpl(impl: NavImpl) : Nav

    @Binds
    @Singleton
    fun navMainToImpl(impl: NavMainImpl) : NavMain

}