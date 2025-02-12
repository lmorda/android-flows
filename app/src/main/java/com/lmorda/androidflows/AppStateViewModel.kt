package com.lmorda.androidflows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lmorda.androidflows.data.VitalSignsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppStateViewModel @Inject constructor(
    repository: VitalSignsRepository
) : ViewModel() {

    // Use a hot flow to broadcast errors immediately to multiple consumers
    val errorMessages: SharedFlow<String> = repository.errorMessageFlow

    init {
        repository.getVitals(viewModelScope)
    }

}
