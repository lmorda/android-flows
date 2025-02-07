package com.lmorda.androidflows.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lmorda.androidflows.models.VitalSignsEntity

@Database(entities = [VitalSignsEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vitalSignsDao(): VitalSignsDao
}