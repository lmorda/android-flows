package com.lmorda.androidflows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lmorda.androidflows.data.VitalSignsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppStateViewModel @Inject constructor(
    repository: VitalSignsRepository
) : ViewModel() {

    val errorMessages: SharedFlow<String> = repository.errorFlow

    init {
        viewModelScope.launch {
            repository.getVitals()
        }
    }

}
