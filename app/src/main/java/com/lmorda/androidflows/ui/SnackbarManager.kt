package com.lmorda.androidflows.ui

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SnackbarManager @Inject constructor() {

    private val _snackbarMessages = MutableSharedFlow<String>(replay = 1)
    val snackbarMessages: SharedFlow<String> = _snackbarMessages.asSharedFlow()

    suspend fun showMessage(message: String) {
        _snackbarMessages.emit(message)
    }
}