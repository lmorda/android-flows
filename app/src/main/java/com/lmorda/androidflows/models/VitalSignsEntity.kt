package com.lmorda.androidflows.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vital_signs")
data class VitalSignsEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val bpm: Int,
    val systolic: Int,
    val diastolic: Int
)