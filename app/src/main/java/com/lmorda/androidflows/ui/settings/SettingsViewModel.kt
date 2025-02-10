package com.lmorda.androidflows.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lmorda.androidflows.data.UserDataRepository
import com.lmorda.androidflows.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {

    // Use a StateFlow for Compose because it's hot and it always has the last value
    private val _state =
        MutableStateFlow(SettingsUiState(user = User(firstName = "", lastName = "")))
    val state: StateFlow<SettingsUiState> = _state

    init {
        viewModelScope.launch {
            // Emit the initial value on the hot StateFlow
            _state.value = SettingsUiState(user = userDataRepository.getUser())
        }
    }

    fun updateUser(firstName: String, lastName: String) {
        viewModelScope.launch {
            userDataRepository.updateUser(firstName, lastName)
        }
    }
}