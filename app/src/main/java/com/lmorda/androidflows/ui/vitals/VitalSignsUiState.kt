package com.lmorda.androidflows.ui.vitals

import com.lmorda.androidflows.models.AverageBloodPressure
import com.lmorda.androidflows.models.User
import com.lmorda.androidflows.models.VitalSignsEntity

data class VitalSignsUiState(
    val user: User? = null,
    val latestVitalSigns: VitalSignsEntity? = null,
    val averageHeartRate: Double? = null,
    val averageBloodPressure: AverageBloodPressure? = null,
    val vitalSignsHistory: List<VitalSignsEntity> = emptyList(),
)
