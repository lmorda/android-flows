package com.lmorda.androidflows.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lmorda.androidflows.models.AverageBloodPressure
import com.lmorda.androidflows.models.VitalSignsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VitalSignsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vitalSigns: VitalSignsEntity)

    @Query("SELECT * FROM vital_signs ORDER BY timestamp ASC")
    fun getAllVitalSigns(): Flow<List<VitalSignsEntity>>

    @Query("SELECT * FROM vital_signs ORDER BY timestamp DESC LIMIT 1")
    fun getLatestVitalSigns(): Flow<VitalSignsEntity?>

    @Query(
        value = "SELECT AVG(bpm) FROM vital_signs " +
                "WHERE date(timestamp / 1000, 'unixepoch') = date('now')"
    )
    fun getAverageHeartRateForToday(): Flow<Double?>

    @Query(
        "SELECT AVG(systolic) as avgSystolic, AVG(diastolic) as avgDiastolic FROM vital_signs " +
                "WHERE date(timestamp / 1000, 'unixepoch') = date('now')"
    )
    fun getAverageBloodPressureForToday(): Flow<AverageBloodPressure?>

}