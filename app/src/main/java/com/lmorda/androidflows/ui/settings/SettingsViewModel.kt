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

    private val _user = MutableStateFlow(User("", ""))
    val user: StateFlow<User> = _user

    init {
        viewModelScope.launch {
            _user.value = userDataRepository.getUser()
        }
    }

    fun updateUser(firstName: String, lastName: String) {
        viewModelScope.launch {
            userDataRepository.updateUser(firstName, lastName)
        }
    }
}