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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VitalSignsViewModel @Inject constructor(
    repository: VitalSignsRepository,
    private val userRepository: UserDataRepository
) : ViewModel() {

    private val _user = MutableStateFlow(User("", ""))
    val user: StateFlow<User> = _user

    fun getUser() {
        viewModelScope.launch {
            _user.value = userRepository.getUser()
        }
    }

    val latestVitalSigns: StateFlow<VitalSignsEntity?> =
        repository.getLatestVitalSigns().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = null
        )

    val averageHeartRate: StateFlow<Double?> =
        repository.getAvgHeartRateToday().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = null
        )

    val averageBloodPressure: StateFlow<AverageBloodPressure?> =
        repository.getAvgBloodPressureToday().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = null
        )

    val vitalSignsHistory: StateFlow<List<VitalSignsEntity>> = repository.getAllVitalSigns()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )
}