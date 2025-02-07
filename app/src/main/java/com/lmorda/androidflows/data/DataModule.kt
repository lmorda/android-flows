package com.lmorda.androidflows.data

import android.content.Context
import android.content.SharedPreferences
import com.lmorda.androidflows.db.VitalSignsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providesVitalSignsRepository(
        dao: VitalSignsDao,
        fakeVitalSignsDataSource: FakeVitalSignsDataSource
    ): VitalSignsRepository {
        return VitalSignsRepository(dao, fakeVitalSignsDataSource)
    }

    @Provides
    @Singleton
    fun providesUserDataRepository(fakeUserDataSource: FakeUserDataSource): UserDataRepository {
        return UserDataRepository(fakeUserDataSource)
    }

    @Provides
    @Singleton
    fun providesFakeUserDataSource(userPreferences: UserPreferences): FakeUserDataSource {
        return FakeUserDataSource(userPreferences)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }
}