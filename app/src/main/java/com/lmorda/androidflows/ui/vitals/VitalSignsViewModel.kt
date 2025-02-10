package com.lmorda.androidflows.ui.vitals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lmorda.androidflows.data.UserDataRepository
import com.lmorda.androidflows.data.VitalSignsRepository
import com.lmorda.androidflows.models.AverageBloodPressure
import com.lmorda.androidflows.models.User
import com.lmorda.androidflows.models.VitalSignsEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VitalSignsViewModel @Inject constructor(
    vitalSignsRepository: VitalSignsRepository,
    private val userRepository: UserDataRepository
) : ViewModel() {

    private val user = MutableStateFlow(User("", ""))

    init {
        viewModelScope.launch {
            user.value = userRepository.getUser()
        }
    }

    private val latestVitalSigns: StateFlow<VitalSignsEntity?> =
        vitalSignsRepository.getLatestVitalSigns().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = null
        )

    private val averageHeartRate: StateFlow<Double?> =
        vitalSignsRepository.getAvgHeartRateToday().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = null
        )

    private val averageBloodPressure: StateFlow<AverageBloodPressure?> =
        vitalSignsRepository.getAvgBloodPressureToday().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = null
        )

    private val vitalSignsHistory: StateFlow<List<VitalSignsEntity>> =
        vitalSignsRepository.getAllVitalSigns()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList()
            )

    val state: StateFlow<VitalSignsUiState> = combine(
        user,
        latestVitalSigns,
        averageHeartRate,
        averageBloodPressure,
        vitalSignsHistory
    ) { user, latest, heartRate, bloodPressure, history ->
        VitalSignsUiState(
            user = user,
            latestVitalSigns = latest,
            averageHeartRate = heartRate,
            averageBloodPressure = bloodPressure,
            vitalSignsHistory = history
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = VitalSignsUiState()
    )
}