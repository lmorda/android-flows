package com.lmorda.androidflows.db

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "vital_signs_db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesVitalSignsDao(db: AppDatabase): VitalSignsDao {
        return db.vitalSignsDao()
    }
}